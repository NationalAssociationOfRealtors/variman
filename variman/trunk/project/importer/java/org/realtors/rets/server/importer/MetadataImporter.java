package org.realtors.rets.server.importer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import org.realtors.rets.server.metadata.AlignmentEnum;
import org.realtors.rets.server.metadata.UnitEnum;
import org.realtors.rets.server.metadata.InterpretationEnum;
import org.realtors.rets.server.metadata.DataTypeEnum;
import org.realtors.rets.server.metadata.TableStandardNameEnum;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.ObjectTypeEnum;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.SearchHelp;
import org.realtors.rets.server.metadata.EditMask;
import org.realtors.rets.server.metadata.Lookup;
import org.realtors.rets.server.metadata.LookupType;
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.Update;
import org.realtors.rets.server.metadata.UpdateHelp;
import org.realtors.rets.server.metadata.ValidationExternal;
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
        mEditMasks = new HashMap();
        mTables = new HashMap();
        mLookups = new HashMap();
        mSearchHelps = new HashMap();
        mValidationExternals = new HashMap();
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

            MSystem hSystem = doSystem(rSession, hSession);
            doResource(rSession, hSession, hSystem);
            doClasses(rSession, hSession);
            doObjects(rSession, hSession);
            doSearchHelp(rSession, hSession);
            doEditMask(rSession, hSession);
            doLookup(rSession, hSession);
            doUpdateHelp(rSession, hSession);
            doValidationExternal(rSession, hSession);
            doUpdate(rSession, hSession);
            doTable(rSession, hSession);

            tx.commit();
            hSession.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            try
            {
                tx.rollback();
                hSession.close();
            }
            catch (Exception e2)
            {
                e2.printStackTrace();
            }
        }
    }

    private MSystem doSystem(RetsSession rSession, Session hSession)
        throws HibernateException
    {
        MetadataTable tSystem =
            rSession.getMetadataTable(MetadataTable.SYSTEM);
        MSystem hSystem = new MSystem();
        hSystem.setVersion(10101);
        hSystem.setDate(Calendar.getInstance().getTime());

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

            hSession.save(hResource);
            hResources.add(hResource);
            mResources.put(hResource.getPath(), hResource);
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
            List classes = tClass.getDataRows(resource.getPath());
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

                hSession.save(hClass);
                hClasses.add(hClass);
                mClasses.put(hClass.getPath(), hClass);
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
            List objects = tObject.getDataRows(resource.getPath());
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
                tSearchHelp.getDataRows(resource.getPath());
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
                    mSearchHelps.put(hSearchHelp.getPath(), hSearchHelp);
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
            List editMasks = tEditMask.getDataRows(resource.getPath());
            if (editMasks != null)
            {
                Iterator j = editMasks.iterator();
                while (j.hasNext())
                {
                    Metadata md = (Metadata) j.next();
                    EditMask hEditMask = new EditMask();

                    hEditMask.setResourceid(resource);
                    hEditMask.setEditMaskID(md.getAttribute("EditMaskID"));
                    hEditMask.setValue(md.getAttribute("Value"));

                    hSession.save(hEditMask);
                    hEditMasks.add(hEditMask);
                    mEditMasks.put(hEditMask.getPath(), hEditMask);
                }
            }
            resource.setEditMasks(hEditMasks);
            hSession.saveOrUpdate(resource);
        }
    }

    private void doLookup(RetsSession rSession, Session hSession)
        throws HibernateException, ParseException
    {
        MetadataTable tLookup =
            rSession.getMetadataTable(MetadataTable.LOOKUP);

        Iterator i = mResources.values().iterator();
        while (i.hasNext())
        {
            Resource resource = (Resource) i.next();
            Set hLookups = new HashSet();
            List lookups = tLookup.getDataRows(resource.getPath());
            if (lookups != null)
            {
                Iterator j = lookups.iterator();
                while (j.hasNext())
                {
                    Metadata md = (Metadata) j.next();
                    Lookup hLookup = new Lookup();

                    hLookup.setResourceid(resource);

                    String lookupName = md.getAttribute("LookupName");
                    hLookup.setLookupName(lookupName);

                    hLookup.setVisibleName(md.getAttribute("VisibleName"));

                    doLookupTypes(hLookup, rSession, hSession);

                    hSession.save(hLookup);
                    hLookups.add(hLookup);
                    mLookups.put(hLookup.getPath(), hLookup);
                }
            }
            resource.setLookups(hLookups);
            hSession.saveOrUpdate(resource);
        }
    }

    /**
     * Meant to be called only from do Lookup.
     *
     * @param hLookup the Lookup object to get the types for
     * @param rSession the rets session
     * @param hSession the hibernate session
     * @exception HibernateException if an error occurs
     */
    private void doLookupTypes(Lookup hLookup, RetsSession rSession,
                               Session hSession)
        throws HibernateException
    {
        MetadataTable tLookupTypes =
            rSession.getMetadataTable(MetadataTable.LOOKUP_TYPE);

        List lookupTypes = tLookupTypes.getDataRows(hLookup.getPath());
        Set hLookupTypes = new HashSet();
        if (lookupTypes != null)
        {
            System.out.println("in lookup type area: " +
                               hLookup.getLookupName());
            Iterator i = lookupTypes.iterator();
            while (i.hasNext())
            {
                Metadata md = (Metadata) i.next();
                LookupType hLookupType = new LookupType();

                hLookupType.setLookupid(hLookup);

                hLookupType.setLongValue(md.getAttribute("LongValue"));
                hLookupType.setShortValue(md.getAttribute("ShortValue"));
                hLookupType.setValue(md.getAttribute("Value"));

                hSession.save(hLookupType);
                hLookupTypes.add(hLookupType);
            }
        }
        hLookup.setLookupTypes(hLookupTypes);
        hSession.saveOrUpdate(hLookup);
    }

    private void doUpdateHelp(RetsSession rSession, Session hSession)
        throws HibernateException
    {
        MetadataTable tUpdateHelps =
            rSession.getMetadataTable(MetadataTable.UPDATE_HELP);

        Iterator i = mResources.values().iterator();
        while (i.hasNext())
        {
            Resource resource = (Resource) i.next();
            Set hUpdateHelps = new HashSet();
            List updateHelps =
                tUpdateHelps.getDataRows(resource.getPath());
            if (updateHelps != null)
            {
                Iterator j = updateHelps.iterator();
                while (j.hasNext())
                {
                    Metadata md = (Metadata) j.next();
                    UpdateHelp hUpdateHelp = new UpdateHelp();

                    hUpdateHelp.setResourceid(resource);
                    hUpdateHelp.setUpdateHelpID(
                        md.getAttribute("UpdateHelpID"));
                    hUpdateHelp.setValue(md.getAttribute("Value"));

                    hSession.save(hUpdateHelp);
                    hUpdateHelps.add(hUpdateHelp);
                }
            }
            resource.setUpdateHelps(hUpdateHelps);
            hSession.saveOrUpdate(resource);
        }
    }

    private void doValidationExternal(RetsSession rSession, Session hSession)
        throws HibernateException,ParseException
    {
        MetadataTable tValidationExternals =
            rSession.getMetadataTable(MetadataTable.VALIDATION_EXTERNAL);

        Iterator i = mResources.values().iterator();
        while (i.hasNext())
        {
            Resource resource = (Resource) i.next();
            Set hValidationExternals = new HashSet();
            List validationExternals =
                tValidationExternals.getDataRows(resource.getPath());
            if (validationExternals != null)
            {
                Iterator j = validationExternals.iterator();
                while (j.hasNext())
                {
                    Metadata md = (Metadata) j.next();
                    ValidationExternal hValidationExternal =
                        new ValidationExternal();

                    hValidationExternal.setResourceid(resource);

                    hValidationExternal.setValidationExternalName(
                        md.getAttribute("ValidationExternalName"));

                    // get the search class
                    String path = md.getAttribute("SearchResource") + ":" +
                        md.getAttribute("SearchClass");
                    MClass clazz = (MClass) mClasses.get(path);
                    hValidationExternal.setSearchClass(clazz);

                    hSession.save(hValidationExternal);
                    hValidationExternals.add(hValidationExternal);
                    mValidationExternals.put(hValidationExternal.getPath(),
                                             hValidationExternal);
//                     doValidationExternalType(hValidationExternal, rSession,
//                                              hSession);
                }
            }
            resource.setValidationExternals(hValidationExternals);
            hSession.saveOrUpdate(resource);
        }
    }


    /**
     * Intended to be called by doValidationExternal.  Requires tables
     * to be done, don't install yet.
     *

     *
     * @param rSession the rets session
     * @param hSession the hibernate session
     * @exception HibernateException if an error occurs
     */
    private void doValidationExternalType(RetsSession rSession,
                                          Session hSession)
        throws HibernateException
    {
        MetadataTable tValidationExternalTypes =
            rSession.getMetadataTable(MetadataTable.VALIDATION_EXTERNAL_TYPE);

        Iterator i = mValidationExternals.values().iterator();
        while (i.hasNext())
        {
        }
        // todo: Fill in doValidationExternalType
    }

    /**
     * Requires that Table to be done.
     *
     * @param rSession the rets session
     * @param hSession the hibernate session
     */
    private void doValidationLookup(RetsSession rSession, Session hSession)
    {
        //todo: Fill in doValidationLookup
    }

    private void doUpdate(RetsSession rSession, Session hSession)
        throws HibernateException, ParseException
    {
        MetadataTable tUpdates =
            rSession.getMetadataTable(MetadataTable.UPDATE);

        Iterator i = mClasses.values().iterator();
        while (i.hasNext())
        {
            MClass clazz = (MClass) i.next();
            Set hUpdates = new HashSet();
            List updates = tUpdates.getDataRows(clazz.getPath());
            if (updates != null)
            {
                Iterator j = updates.iterator();
                while (j.hasNext())
                {
                    Metadata md = (Metadata) j.next();
                    Update hUpdate = new Update();

                    hUpdate.setClassid(clazz);

                    hUpdate.setUpdateName(md.getAttribute("UpdateName"));
                    hUpdate.setDescription(md.getAttribute("Description"));
                    hUpdate.setKeyField(md.getAttribute("KeyField"));

                    hSession.save(hUpdate);
                    hUpdates.add(hUpdate);
                }
            }
            clazz.setUpdates(hUpdates);
            hSession.saveOrUpdate(clazz);
        }
    }

    private void doTable(RetsSession rSession, Session hSession)
        throws HibernateException
    {
        MetadataTable tTables =
            rSession.getMetadataTable(MetadataTable.TABLE);

        Iterator i = mClasses.values().iterator();
        while (i.hasNext())
        {
            MClass hClass = (MClass) i.next();
            Set hTables = new HashSet();
            List tables = tTables.getDataRows(hClass.getPath());
            if (tables != null)
            {
                Iterator j = tables.iterator();
                while (j.hasNext())
                {
                    Metadata md = (Metadata) j.next();
                    Table hTable = new Table();

                    hTable.setClassid(hClass);

                    hTable.setSystemName(md.getAttribute("SystemName"));
                    hTable.setStandardName(
                        TableStandardNameEnum.fromString(
                            md.getAttribute("StandardName")));
                    hTable.setLongName(md.getAttribute("LongName"));

                    hTable.setDbName(
                        StringUtils.substring(md.getAttribute("DbName"),0,9));
                    hTable.setShortName(md.getAttribute("ShortName"));
                    hTable.setMaximumLength(
                        Integer.parseInt(md.getAttribute("MaximumLength")));
                    hTable.setDataType(
                        DataTypeEnum.fromString(md.getAttribute("DataType")));
                    hTable.setPrecision(
                        Integer.parseInt(md.getAttribute("Precision")));
                    hTable.setSearchable(
                        Boolean.valueOf(
                            md.getAttribute("Searchable")).booleanValue());
                    hTable.setInterpretation(
                        InterpretationEnum.fromString(
                            md.getAttribute("Interpretation")));
                    hTable.setAlignment(
                        AlignmentEnum.fromString(
                            md.getAttribute("Alignment")));
                    hTable.setUseSeparator(
                        Boolean.valueOf(
                            md.getAttribute("UseSeparator")).booleanValue());

                    String editMasksJoined = md.getAttribute("EditMaskID");
                    String resourcePath =  hClass.getResourceid().getPath();
                    String path = null;
                    if (editMasksJoined != null)
                    {
                        String editMasks[] =
                            StringUtils.split(editMasksJoined,",");
                        Set hEditMasks = new HashSet();
                        for (int c = 0; c < editMasks.length; c++)
                        {
                            Resource resource = hClass.getResourceid();
                            path = resourcePath + ":" + editMasks[c];
                            EditMask em = (EditMask) mEditMasks.get(path);
                            hEditMasks.add(em);
                        }
                        hTable.setEditMasks(hEditMasks);
                    }

                    String lookupName = md.getAttribute("LookupName");
                    path = resourcePath + ":" + lookupName;
                    Lookup lookup = (Lookup) mLookups.get(path);
                    hTable.setLookup(lookup);

                    hTable.setMaxSelect(
                        Integer.parseInt(md.getAttribute("MaxSelect")));

                    hTable.setUnits(
                        UnitEnum.fromString(md.getAttribute("Units")));

                    hTable.setIndex(
                        Integer.parseInt(md.getAttribute("Index")));

                    hTable.setMinimum(
                        Integer.parseInt(md.getAttribute("Minimum")));

                    hTable.setMaximum(
                        Integer.parseInt(md.getAttribute("Maximum")));

                    hTable.setDefault(
                        Integer.parseInt(md.getAttribute("Default")));

                    hTable.setRequired(
                        Integer.parseInt(md.getAttribute("Required")));

                    // todo: search help
                    String searchHelpID = md.getAttribute("SearchHelpID");
                    path = resourcePath + ":" + searchHelpID;
                    SearchHelp searchHelp =
                        (SearchHelp) mSearchHelps.get(path);
                    hTable.setSearchHelp(searchHelp);

                    hTable.setUnique(
                        Boolean.valueOf(
                            md.getAttribute("Unique")).booleanValue());

                    hSession.save(hTable);
                    hTables.add(hTable);
                    mTables.put(hTable.getPath(), hTable);
                }
                hClass.setTables(hTables);
                hSession.saveOrUpdate(hClass);
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
    private Map mEditMasks;
    private Map mTables;
    private Map mLookups;
    private Map mSearchHelps;
    private Map mValidationExternals;
    private DateFormat mDateFormat;

    private static final String CVSID =
        "$Id: MetadataImporter.java,v 1.17 2003/07/08 21:23:07 dribin Exp $";
}
