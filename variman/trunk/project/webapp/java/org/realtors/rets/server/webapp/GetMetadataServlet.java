/*
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.realtors.rets.server.SessionHelper;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.MetadataSegment;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.ServerMetadata;
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.format.FormattingVisitor;
import org.realtors.rets.server.metadata.format.MetadataFormatter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @web.servlet name="get-metadata-servlet"
 * @web.servlet-mapping  url-pattern="/rets/getMetadata"
 */
public class  GetMetadataServlet extends RetsServlet
{
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException
    {
        String type = request.getParameter("Type");
        String id = request.getParameter("ID");
        String formatString = request.getParameter("Format");
        LOG.debug("type=" + type + ", id=" + id + ", format=" + formatString);

        // Clean up format
        if (formatString == null)
        {
            formatString = COMPACT_FORMAT;
        }

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

        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        out.println("<RETS ReplyCode=\"0\" " +
                    "ReplyText=\"Operation Successful\">");
        formatOutput(out, metadataObjects, format);
        out.println("</RETS>");
    }

    private int parseFormat(String formatString)
    {
        if (formatString.equals(COMPACT_FORMAT))
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
        LOG.info("Formatting " + metadataSegments.size() + " segments");
        FormattingVisitor visitor = new FormattingVisitor(out, format);
        for (int i = 0; i < metadataSegments.size(); i++)
        {
            MetadataSegment metadataSegment =
                (MetadataSegment) metadataSegments.get(i);
            visitor.visit(metadataSegment);
        }
    }

    private List getMetadata(String type, String[] levels, boolean recursive)
    {
        // Always need system to get version and date
        LOG.debug("Getting system");
        MSystem system = findSystem();
        LOG.debug("Got system");
        String version = system.getVersionString();
        Date date = system.getDate();
        List metadata = new ArrayList();
        String level = StringUtils.join(levels, ":");

        if (type.equals("METADATA-SYSTEM"))
        {
            assertLength(levels, 0);
            metadata.add(new MetadataSegment(new MSystem[] {system}, levels,
                                             "", null));

            System.out.println(system.getDescription());
            if (recursive)
            {
//                recurseSystem(system, metadata, levels, version, date);
                List systemList = new ArrayList();
                systemList.add(system);
                recurseChildren(systemList, metadata, version, date);
            }
        }
        else if (type.equals("METADAT-RESOURCE"))
        {
            assertLength(levels, 0);
            Resource[] resources = findResources();
            metadata.add(new MetadataSegment(resources, levels, version, date));
        }
        else if (type.equals("METADATA-CLASS"))
        {
            assertLength(levels, 1);
            MClass[] classes = findClasses(level);
            metadata.add(classes);
            for (int i = 0; i < classes.length; i++)
            {
                MClass aClass = classes[i];
                System.out.println("Class: " + aClass.getClassName());
            }
        }
        else if (type.equals("METADATA-TABLE"))
        {
            assertLength(levels, 2);
            Table[] tables = findTables(level);
            metadata.add(tables);

        }
        else
        {
            LOG.warn("Recieved query for unknown metadata type: " + type +
                     ", level=" + level);
        }
        return metadata;
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
    private void assertLength(String[] array, int length)
        throws IllegalArgumentException
    {
        if (array.length != length)
        {
            throw new IllegalArgumentException("Bad levels: " + array);
        }
    }

    private MSystem findSystem()
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
            LOG.info("Closing helper");
            helper.close(LOG);
        }
        return system;
    }

    private Resource[] findResources()
    {
        SessionHelper helper = InitServlet.createHelper();
        Resource[] resources = null;
        try
        {
            Session session = helper.beginTransaction();
            List results = session.find("from Resource");
            resources = (Resource[])
                results.toArray(new Resource[results.size()]);
            helper.commit();
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
        return resources;
    }

    private MClass[] findClasses(String resourceName)
    {
        SessionHelper helper = InitServlet.createHelper();
        MClass[] classes = null;
        try
        {
            Session session = helper.beginTransaction();
            List results = session.find(" from MClass as clazz " +
                                        "where clazz.level=?", resourceName,
                                        Hibernate.STRING);
            classes = (MClass[]) results.toArray(new MClass[results.size()]);
            helper.commit();
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
        return classes;
    }

    private Table[] findTables(String level)
    {
        SessionHelper helper = InitServlet.createHelper();
        Table[] tables = null;
        try
        {
            Session session = helper.beginTransaction();
            List results = session.find(" from Table as table " +
                                        "where class.level=?", level,
                                        Hibernate.STRING);
            tables = (Table[]) results.toArray(new Table[results.size()]);
            helper.commit();
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
        return tables;
    }

    private static final Logger LOG =
        Logger.getLogger(GetMetadataServlet.class);
    public static final String COMPACT_FORMAT = "COMPACT";
}
