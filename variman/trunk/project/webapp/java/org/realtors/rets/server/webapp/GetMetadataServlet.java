/*
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.realtors.rets.server.SessionHelper;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.MetadataSegment;
import org.realtors.rets.server.metadata.Update;
import org.realtors.rets.server.metadata.UpdateType;
import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.SearchHelp;
import org.realtors.rets.server.metadata.EditMask;
import org.realtors.rets.server.metadata.Lookup;
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

        if (type.equals("SYSTEM"))
        {
            assertLength(levels, 0);
            metadata.add(new MetadataSegment(new MSystem[] {system}, levels,
                                             "", null));

            System.out.println(system.getDescription());
            if (recursive)
            {
                recurseSystem(system, metadata, levels, version, date);
            }
        }
        else if (type.equals("RESOURCE"))
        {
            assertLength(levels, 0);
            Resource[] resources = findResources();
            metadata.add(new MetadataSegment(resources, levels, version, date));
        }
        else if (type.equals("CLASS"))
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
        else if (type.equals("TABLE"))
        {
            assertLength(levels, 2);
            Table[] tables = findTables(level);
            metadata.add(tables);

        }
        return metadata;
    }

    private void recurseSystem(MSystem system, List metadata, String[] levels,
                               String version, Date date)
    {
        Set resourceSet = system.getResources();
        Resource[] resources = (Resource[])
            resourceSet.toArray(new Resource[resourceSet.size()]);
        metadata.add(new MetadataSegment(resources,  levels,
                                         version, date));
        recurseResources(resources, metadata, version, date);
    }

    private void recurseResources(Resource[] resources, List metadata,
                                  String version, Date date)
    {
        for (int i = 0; i < resources.length; i++)
        {
            Resource resource = resources[i];
            String[] thisLevel = new String[] {resource.getResourceID()};

            Set classesSet = resource.getClasses();
            MClass[] classes = (MClass[])
                classesSet.toArray(new MClass[classesSet.size()]);
            metadata.add(new MetadataSegment(classes, thisLevel, version,
                                             date));
            recurseClasses(classes, metadata, thisLevel, version, date);

            Set objectsSet = resource.getObjects();
            MObject[] objects = (MObject[])
                objectsSet.toArray(new MObject[objectsSet.size()]);
            metadata.add(new MetadataSegment(objects, thisLevel, version,
                                             date));

            Set searchHelpsSet = resource.getSearchHelps();
            SearchHelp[] serachHelps = (SearchHelp[])
                searchHelpsSet.toArray(new SearchHelp[searchHelpsSet.size()]);
            metadata.add(new MetadataSegment(serachHelps, thisLevel, version,
                                             date));

            Set editMasksSet = resource.getEditMasks();
            EditMask[] editMasks = (EditMask[])
                editMasksSet.toArray(new EditMask[editMasksSet.size()]);
            metadata.add(new MetadataSegment(editMasks, thisLevel, version,
                                             date));

            Set lookupsSet = resource.getLookups();
            Lookup[] lookups = (Lookup[])
                lookupsSet.toArray(new Lookup[lookupsSet.size()]);
            metadata.add(new MetadataSegment(lookups, thisLevel, version,
                                             date));
            recurseLookups(lookups, metadata, thisLevel, version, date);
        }
    }

    private void recurseLookups(Lookup[] lookups, List metadata,
                                String[] level, String version, Date date)
    {
    }

    private void recurseClasses(MClass[] classes, List metadata, String[] level,
                                String version, Date date)
    {
        for (int i = 0; i < classes.length; i++)
        {
            MClass clazz = classes[i];
            String[] thisLevel = new String[] {level[0], clazz.getClassName()};

            Set tablesSet = clazz.getTables();
            Table[] tables = (Table[])
                tablesSet.toArray(new Table[tablesSet.size()]);
            metadata.add(new MetadataSegment(tables, thisLevel, version, date));

            Set updatesSet = clazz.getUpdates();
            Update[] updates = (Update[])
                updatesSet.toArray(new Update[updatesSet.size()]);
            metadata.add(new MetadataSegment(updates, thisLevel, version,
                                             date));
            recurseUpdates(updates, metadata, thisLevel, version, date);
        }
    }

    private void recurseUpdates(Update[] updates, List metadata,
                                String[] levels, String version, Date date)
    {
        for (int i = 0; i < updates.length; i++)
        {
            Update update = updates[i];
            String[] thisLevel = new String[] {levels[0], levels[1],
                                               update.getUpdateName()};

            Set updateTypesSet = update.getUpdateTypes();
            UpdateType[] updateTypes = (UpdateType[])
                updateTypesSet.toArray(new UpdateType[updateTypesSet.size()]);
            metadata.add(new MetadataSegment(updateTypes,  thisLevel, version,
                                             date));
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
