package org.realtors.rets.server.importer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.cfg.Configuration;

import org.realtors.rets.client.Metadata;
import org.realtors.rets.client.MetadataTable;
import org.realtors.rets.client.RetsSession;

import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.ObjectTypeEnum;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.SearchHelp;
import org.realtors.rets.server.metadata.EditMask;
import org.realtors.rets.server.metadata.ResourceStandardNameEnum;
import org.realtors.rets.server.metadata.ClassStandardNameEnum;

public class MetadataImporter
{
    /**
     * Creates a new <code>MetadataImporter</code> instance.
     *
     */
    public MetadataImporter()
        throws Exception
    {
        initHibernate();
        mResources = new HashMap();
        mClasses = new HashMap();
        mDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
    }

    private void initHibernate()
        throws MappingException, HibernateException
    {
        Configuration cfg = new Configuration();
        cfg.addJar("retsdb2-hbm-xml.jar");
        mSessions = cfg.buildSessionFactory();
    }

    public void doIt()
        throws Exception
    {
        RetsSession session =
            new RetsSession("http://demo.crt.realtors.org:6103/login");
        session.login("Joe", "Schmoe");
        parseMetadata(session);
        session.logout();
    }

    private void parseMetadata(RetsSession rSession)
        throws Exception
    {
        Session hSession = null;
        Transaction tx = null;

        try
        {
            hSession = mSessions.openSession();
            tx = hSession.beginTransaction();

            MSystem hSystem = system(rSession, hSession);
            doResource(rSession, hSession, hSystem);
            doClasses(rSession, hSession);
            doObjects(rSession, hSession);

            tx.commit();
            hSession.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
            try
            {
                tx.rollback();
                hSession.close();
            }
            catch (Exception e2)
            {
                System.out.println(e2);
            }
        }
    }

    private MSystem system(RetsSession rSession, Session hSession)
        throws HibernateException
    {
        MetadataTable tSystem =
            rSession.getMetadataTable(MetadataTable.SYSTEM);
        MSystem hSystem = new MSystem();

        hSession.save(hSystem);
        return hSystem;
    }

    private void doResource(RetsSession rSession, Session hSession,
                            MSystem hSystem)
        throws HibernateException,ParseException
    {
        MetadataTable tResource =
            rSession.getMetadataTable(MetadataTable.RESOURCE);

        Set hResources = new HashSet();
        List resources = tResource.getDataRows("");
        Iterator i = resources.iterator();
        while (i.hasNext())
        {
            Metadata md = (Metadata) i.next();
            Resource hResource = new Resource();

            hResource.setSystemid(hSystem);
            String resourceID = md.getAttribute("ResourceID");
            hResource.setResourceID(resourceID);
            hResource.setStandardName(ResourceStandardNameEnum.fromString(
                                          md.getAttribute("StandardName")));
            hResource.setVisibleName(md.getAttribute("VisibleName"));
            hResource.setDescription(md.getAttribute("Description"));
            hResource.setKeyField(md.getAttribute("KeyField"));
            hResource.setClassVersion(md.getAttribute("ClassVersion"));

            hResource.setClassDate(
                mDateFormat.parse(md.getAttribute("ClassDate")));

            hResource.setObjectVersion(md.getAttribute("ObjectVersion"));
            hResource.setObjectDate(
                mDateFormat.parse(md.getAttribute("ObjectDate")));

            hResource.setSearchHelpVersion(
                md.getAttribute("SearchHelpVersion"));
            hResource.setSearchHelpDate(
                mDateFormat.parse(md.getAttribute("SearchHelpDate")));

            hResource.setEditMaskVersion(md.getAttribute("EditMaskVersion"));
            hResource.setEditMaskDate(
                mDateFormat.parse(md.getAttribute("EditMaskDate")));

            hResource.setLookupVersion(md.getAttribute("LookupVersion"));
            hResource.setLookupDate(
                mDateFormat.parse(md.getAttribute("LookupDate")));

            hResource.setUpdateHelpVersion(
                md.getAttribute("UpdateHelpVersion"));
            hResource.setUpdateHelpDate(
                mDateFormat.parse(md.getAttribute("UpdateHelpDate")));

            hResource.setValidationExpressionVersion(
                md.getAttribute("ValidationExpressionVersion"));
            hResource.setValidationExpressionDate(
                mDateFormat.parse(
                    md.getAttribute("ValidationExpressionDate")));

            hResource.setValidationLookupVersion(
                md.getAttribute("ValidationLookupVersion"));
            hResource.setValidationLookupDate(
                mDateFormat.parse(md.getAttribute("ValidationLookupDate")));

            hResource.setValidationExternalVersion(
                md.getAttribute("ValidationExternalVersion"));
            hResource.setValidationExternalDate(
                mDateFormat.parse(md.getAttribute("ValidationExternalDate")));

            hSession.save(hResource);
            hResources.add(hResource);
            mResources.put(resourceID, hResource);
        }

        hSystem.setResources(hResources);
        hSession.saveOrUpdate(hSystem);
    }

    private void doClasses(RetsSession rSession, Session hSession)
        throws HibernateException,ParseException
    {
        MetadataTable tClass =
            rSession.getMetadataTable(MetadataTable.CLASS);

        Iterator i = mResources.values().iterator();
        while (i.hasNext())
        {
            Resource resource = (Resource) i.next();
            Set hClasses = new HashSet();
            List classes = tClass.getDataRows(resource.getResourceID());
            Iterator j = classes.iterator();
            while (j.hasNext())
            {
                Metadata md = (Metadata) j.next();
                MClass hClass = new MClass();

                hClass.setResourceid(resource);
                String className = md.getAttribute("ClassName");
                hClass.setClassName(className);
                hClass.setStandardName(ClassStandardNameEnum.fromString(
                                           md.getAttribute("StandardName")));
                hClass.setVisibleName(md.getAttribute("VisibleName"));
                hClass.setDescription(md.getAttribute("Description"));
                hClass.setTableVersion(md.getAttribute("TableVersion"));
                hClass.setTableDate(
                    mDateFormat.parse(md.getAttribute("TableDate")));
                hClass.setUpdateVersion(md.getAttribute("UpdateVersion"));
                hClass.setUpdateDate(
                    mDateFormat.parse(md.getAttribute("UpdateDate")));

                hSession.save(hClass);
                hClasses.add(hClass);
                mClasses.put(className, hClass);
            }

            resource.setClasses(hClasses);
            hSession.saveOrUpdate(resource);
        }
    }

    private void doObjects(RetsSession rSession, Session hSession)
        throws HibernateException,ParseException
    {
        MetadataTable tObject =
            rSession.getMetadataTable(MetadataTable.OBJECT);

        Iterator i = mResources.values().iterator();
        while (i.hasNext())
        {
            Resource resource = (Resource) i.next();
            Set hObjects = new HashSet();
            List objects = tObject.getDataRows(resource.getResourceID());
            if (objects != null)
            {
                Iterator j = objects.iterator();
                while (j.hasNext())
                {
                    Metadata md = (Metadata) j.next();
                    MObject hObject = new MObject();

                    hObject.setResourceid(resource);

                    hObject.setObjectType(ObjectTypeEnum.fromString(
                                              md.getAttribute("ObjectType")));
                    hObject.setMimeType(md.getAttribute("MimeType"));
                    hObject.setVisibleName(md.getAttribute("VisibleName"));
                    hObject.setDescription(
                        StringUtils.substring(
                            md.getAttribute("Description"),0,63));

                    hSession.save(hObject);
                    hObjects.add(hObject);
                }
            }

            resource.setObjects(hObjects);
            hSession.saveOrUpdate(resource);
        }
    }

    private void doSearchHelp(RetsSession rSession, Session hSession)
        throws HibernateException
    {
        MetadataTable tSearchHelp =
            rSession.getMetadataTable(MetadataTable.SEARCH_HELP);

        Iterator i = mResources.values().iterator();
        while (i.hasNext())
        {
            Resource resource = (Resource) i.next();
            Set hSearchHelps = new HashSet();
            List searchHelps =
                tSearchHelp.getDataRows(resource.getResourceID());
            if (searchHelps != null)
            {
                Iterator j = searchHelps.iterator();
                while (j.hasNext())
                {
                    Metadata md = (Metadata) j.next();
                    SearchHelp hSearchHelp = new SearchHelp();

                    hSearchHelp.setResourceid(resource);
                    hSearchHelp.setSearchHelpID(
                        md.getAttribute("SearchHelpID"));
                    hSearchHelp.setValue(md.getAttribute("Value"));

                    hSession.save(hSearchHelp);
                    hSearchHelps.add(hSearchHelp);
                }
            }

            resource.setSearchHelps(hSearchHelps);
            hSession.saveOrUpdate(resource);
        }
    }

    private void doEditMask(RetsSession rSession, Session hSession)
        throws HibernateException
    {
        MetadataTable tEditMask =
            rSession.getMetadataTable(MetadataTable.EDITMASK);

        Iterator i = mResources.values().iterator();
        while (i.hasNext())
        {
            Resource resource = (Resource) i.next();
            Set hEditMasks = new HashSet();
            List editMasks = tEditMask.getDataRows(resource.getResourceID());
            if (editMasks != null)
            {
                Iterator j = editMasks.iterator();
                while (j.hasNext())
                {
                    Metadata md = (Metadata) j.next();
                    EditMask hEditMask = new EditMask();

                    
                }
            }
        }
    }

    public static final void main(String args[])
        throws Exception
    {
        MetadataImporter mi = new MetadataImporter();
        mi.doIt();
    }

    private SessionFactory mSessions;
    private Map mResources;
    private Map mClasses;
    private DateFormat mDateFormat;

    private static final String CVSID =
        "$Id: MetadataImporter.java,v 1.10 2003/06/30 19:11:35 kgarner Exp $";
}
