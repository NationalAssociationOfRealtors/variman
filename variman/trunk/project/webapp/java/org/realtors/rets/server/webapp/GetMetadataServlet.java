/*
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.realtors.rets.server.SessionHelper;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.MetadataSegment;
import org.realtors.rets.server.metadata.ServerMetadata;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.Update;
import org.realtors.rets.server.metadata.UpdateType;
import org.realtors.rets.server.metadata.SearchHelp;
import org.realtors.rets.server.metadata.EditMask;
import org.realtors.rets.server.metadata.Lookup;
import org.realtors.rets.server.metadata.LookupType;
import org.realtors.rets.server.metadata.ValidationLookup;
import org.realtors.rets.server.metadata.ValidationLookupType;
import org.realtors.rets.server.metadata.ValidationExternal;
import org.realtors.rets.server.metadata.ValidationExternalType;
import org.realtors.rets.server.metadata.ValidationExpression;
import org.realtors.rets.server.metadata.format.MetadataFormatter;
import org.realtors.rets.server.metadata.format.MetadataSegmentFormatter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @web.servlet name="get-metadata-servlet"
 * @web.servlet-mapping  url-pattern="/rets/getMetadata"
 */
public class  GetMetadataServlet extends RetsServlet
{
    public void init() throws ServletException
    {
        mUseCache = true;
        mValidTypes = new HashSet();
        mValidTypes.add(MSystem.TABLE);
        mValidTypes.add(Resource.TABLE);
        mValidTypes.add(MClass.TABLE);
        mValidTypes.add(Table.TABLE);
        mValidTypes.add(Update.TABLE);
        mValidTypes.add(UpdateType.TABLE);
        mValidTypes.add(MObject.TABLE);
        mValidTypes.add(SearchHelp.TABLE);
        mValidTypes.add(EditMask.TABLE);
        mValidTypes.add(Lookup.TABLE);
        mValidTypes.add(LookupType.TABLE);
        mValidTypes.add(ValidationLookup.TABLE);
        mValidTypes.add(ValidationLookupType.TABLE);
        mValidTypes.add(ValidationExternal.TABLE);
        mValidTypes.add(ValidationExternalType.TABLE);
        mValidTypes.add(ValidationExpression.TABLE);
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();

        try
        {
            String type = request.getParameter("Type");
            String id = request.getParameter("ID");
            String formatString = request.getParameter("Format");
            LOG.debug("type=" + type + ", id=" + id + ", format=" +
                      formatString);

            type = cleanUpType(type);
            int format = parseFormat(formatString);
            String[] ids = StringUtils.split(id, ":");

            // Clean up id
            boolean recursive = false;
            String lastId = ids[ids.length - 1];
            if (lastId.equals("*"))
            {
                recursive = true;
                // chop off "*"
                ids = shrinkByOne(ids);
            }
            else if (lastId.equals("0"))
            {
                recursive = false;
                // chop off "0"
                ids = shrinkByOne(ids);
            }

            List metadataObjects = getMetadata(type, ids, recursive);

            out.println("<RETS ReplyCode=\"0\" " +
                        "ReplyText=\"Operation Successful\">");
            formatOutput(out, metadataObjects, format);
            out.println("</RETS>");
        }
        catch(RetsReplyException e)
        {
            out.println("<RETS ReplyCode=\"" + e.getReplyCode() +
                        "\" ReplyText=\"" + e.getMeaning() + "\"/>\n");
        }
        catch(Exception e)
        {
            LOG.error("Caught", e);
            out.println("<RETS ReplyCode=\"20513\" " +
                        "ReplyText=\"Miscellaneous error\"/>\n");
        }
    }

    private String cleanUpType(String type) throws RetsReplyException
    {
        type = type.toUpperCase();
        if (!type.startsWith("METADATA-"))
        {
            throw new RetsReplyException(20501, "Invalid Type");
        }

        type = type.substring("METADATA-".length());
        if (!mValidTypes.contains(type))
        {
            throw new RetsReplyException(20501, "Invalid Type");
        }
        return type;
    }

    private int parseFormat(String formatString)
    {
        formatString = formatString.toUpperCase();
        if ((formatString == null) || formatString.equals(COMPACT_FORMAT))
        {
            return MetadataFormatter.COMPACT;
        }
        else
        {
            throw new IllegalArgumentException("Unknown formatString: " +
                                               formatString);
        }
    }

    private static String[] shrinkByOne(String[] array)
    {
        String[] newArray = new String[array.length - 1];
        System.arraycopy(array, 0, newArray, 0, array.length - 1);
        return newArray;
    }

    private void formatOutput(PrintWriter out, List metadataSegments,
                              int format)
    {
        LOG.debug("Formatting " + metadataSegments.size() + " segments");
        long start = System.currentTimeMillis();
        MetadataSegmentFormatter formatter =
            new MetadataSegmentFormatter(out, format);
        for (int i = 0; i < metadataSegments.size(); i++)
        {
            MetadataSegment metadataSegment =
                (MetadataSegment) metadataSegments.get(i);
            formatter.format(metadataSegment);
        }
        LOG.debug("Formatting done: " + (System.currentTimeMillis() - start));
    }

    private List getMetadata(String type, String[] levels, boolean recursive)
        throws RetsReplyException
    {
        if (mUseCache)
        {
            return getMetadataFromCache(type, levels, recursive);
        }
        else
        {
            return getMetadataFromHibernate(type, levels, recursive);
        }
    }

    private List getMetadataFromCache(String type, String[] levels,
                                      boolean recursive)
    {
        // Always need system to get version and date
        MSystem system = findSystem();
        String version = system.getVersionString();
        Date date = system.getDate();
        List metadataResults = new ArrayList();

        MetadataManager manager = getMetadataManager();
        List metadata = manager.find(type, StringUtils.join(levels, ":"));
        metadataResults.add(new MetadataSegment(metadata, levels, version,
                                                date));
        if (recursive)
        {
            recurseChildren(metadata,  metadataResults, version, date);
        }
        return metadataResults;
    }

    private List getMetadataFromHibernate(String type, String[] levels,
                                          boolean recursive)
        throws RetsReplyException
    {
        // Always need system to get version and date
        MSystem system = findSystemFromHibernate();
        String version = system.getVersionString();
        Date date = system.getDate();
        List metadataResults = new ArrayList();

        MetadataFinder finder =
            (MetadataFinder) sMetadataFinders.get(type);
        if (finder != null)
        {
            LOG.debug("Using finder for type: " + type);
            long start = System.currentTimeMillis();
            List metadata = finder.findMetadata(levels);
            LOG.debug("End finder: " + (System.currentTimeMillis() - start));
            metadataResults.add(new MetadataSegment(metadata, levels, version,
                                                    date));
            if (recursive)
            {
                recurseChildren(metadata,  metadataResults, version, date);
            }
        }
        else
        {
            LOG.warn("Recieved query for unknown metadataResults type: " +
                     type + ", level=" + StringUtils.join(levels, ":"));
            throw new RetsReplyException(20501, "Invalid Type");
        }
        return metadataResults;
    }


    private void recurseChildren(List parents, List metadataResults,
                                 String version, Date date)
    {
        for (int i = 0; i < parents.size(); i++)
        {
            ServerMetadata metadata = (ServerMetadata) parents.get(i);
            List children = metadata.getChildren();
            String[] childLevels = metadata.getPathAsArray();
            for (int j = 0; j < children.size(); j++)
            {
                ServerMetadata[] child = (ServerMetadata[]) children.get(j);
                metadataResults.add(new MetadataSegment(child, childLevels,
                                                        version, date));
                recurseChildren(Arrays.asList(child), metadataResults,
                                version, date);
            }
        }
    }

    private MSystem findSystem()
    {
        MetadataManager manager = getMetadataManager();
        List systems = manager.find(MSystem.TABLE, "");
        return (MSystem) systems.get(0);
    }

    private MSystem findSystemFromHibernate()
    {
        SessionHelper helper = InitServlet.createHelper();
        MSystem system = null;
        try
        {
            Session session = helper.beginTransaction();
            List results = new ArrayList();
            Iterator iterator = session.iterate("from MSystem");
            while (iterator.hasNext())
            {
                results.add(iterator.next());
            }
            if (results.size() == 1)
            {
                system = (MSystem) results.get(0);
            }
        }
        catch (HibernateException e)
        {
            LOG.warn("Caught", e);
            helper.rollback(LOG);
        }
        finally
        {
            helper.close(LOG);
        }
        return system;
    }

    private static final Logger LOG =
        Logger.getLogger(GetMetadataServlet.class);
    public static final String COMPACT_FORMAT = "COMPACT";
    private static Map sMetadataFinders;
    private Set mValidTypes;
    private boolean mUseCache;

    static
    {
        sMetadataFinders = new HashMap();
        sMetadataFinders.put("SYSTEM",
                              new MetadataFinder("MSystem", 0));
        sMetadataFinders.put("RESOURCE",
                              new MetadataFinder("Resource", 0));
        sMetadataFinders.put("CLASS",
                              new MetadataFinder("MClass", 1));
        sMetadataFinders.put("TABLE",
                             new MetadataFinder("Table", 2));
        sMetadataFinders.put("UPDATE",
                             new MetadataFinder("Update", 2));
        sMetadataFinders.put("UPDATE_TYPE",
                             new MetadataFinder("UpdateType", 3));
        sMetadataFinders.put("OBJECT",
                             new MetadataFinder("MObject", 1));
        sMetadataFinders.put("SEARCH_HELP",
                             new MetadataFinder("SearchHelp", 1));
        sMetadataFinders.put("EDIT_MASK",
                             new MetadataFinder("EditMask", 1));
        sMetadataFinders.put("LOOKUP",
                             new MetadataFinder("Lookup", 1));
        sMetadataFinders.put("LOOKUP_TYPE",
                             new MetadataFinder("LookupType", 2));
        sMetadataFinders.put("VALIDATION_LOOKUP",
                             new MetadataFinder("ValidationLookup", 1));
        sMetadataFinders.put("VALIDATION_LOOKUP_TYPE",
                             new MetadataFinder("ValidatoinLookupType", 2));
        sMetadataFinders.put("VALIDATION_EXTERNAL",
                             new MetadataFinder("ValidationExternal", 1));
        sMetadataFinders.put("VALIDATION_EXTERNAL_TYPE",
                             new MetadataFinder("ValidationExternalType", 2));
        sMetadataFinders.put("VALIDATION_EXPRESSION",
                             new MetadataFinder("ValidationExpression", 1));
    }
}
