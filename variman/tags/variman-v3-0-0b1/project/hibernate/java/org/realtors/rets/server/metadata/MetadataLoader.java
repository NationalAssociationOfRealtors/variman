/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004,2007 The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.realtors.rets.common.metadata.JDomCompactBuilder;
import org.realtors.rets.common.metadata.Metadata;
import org.realtors.rets.common.metadata.MetadataException;
import org.realtors.rets.common.metadata.types.MEditMask;
import org.realtors.rets.common.metadata.types.MForeignKey;
import org.realtors.rets.common.metadata.types.MLookup;
import org.realtors.rets.common.metadata.types.MLookupType;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MSearchHelp;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.common.metadata.types.MUpdate;
import org.realtors.rets.common.metadata.types.MUpdateHelp;
import org.realtors.rets.common.metadata.types.MUpdateType;
import org.realtors.rets.common.metadata.types.MValidationExpression;
import org.realtors.rets.common.metadata.types.MValidationExternal;
import org.realtors.rets.common.metadata.types.MValidationExternalType;
import org.realtors.rets.common.metadata.types.MValidationLookup;
import org.realtors.rets.common.metadata.types.MValidationLookupType;
import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.JdomUtils;
import org.realtors.rets.server.RetsServerException;
import org.xml.sax.InputSource;


/**
 * Helper method to load Metadata from a file.
 *
 * @author kgarner
 */
public class MetadataLoader
{
    /**
     * Creates a new <code>MetadataImporter</code> instance.
     *
     */
    public MetadataLoader()
    {
        mResources = new ListOrderedMap();
        mForeignKeys = new ListOrderedMap();
        mClasses = new ListOrderedMap();
        mEditMasks = new ListOrderedMap();
        mTables = new ListOrderedMap();
        mLookups = new ListOrderedMap();
        mSearchHelps = new ListOrderedMap();
        mValidationExternals = new ListOrderedMap();
        mValidationExpressions = new ListOrderedMap();
        mValidationLookups = new ListOrderedMap();
        mUpdates = new ListOrderedMap();
        mUpdateHelps = new ListOrderedMap();
        mTableStandardNames = new ListOrderedMap();
        mNextId = 1;
    }

    protected void save(Identifiable object)
    {
        Long id = new Long(mNextId);
        mNextId++;
        object.setId(id);
        return;
    }


    /**
     * Reads a metadata defintion in XML from the InputStream and returns
     * the head MSystem object.
     *
     * @param in an InputStream with the XML to be parsed.
     * @return an MSystem object.
     */
    public MSystem parseMetadata(InputStream in)
        throws RetsServerException
    {
        try
        {
            JDomCompactBuilder builder = new JDomCompactBuilder();
            mMetadata = builder.build(new InputSource(in));
            return parseMetadata();
        }
        catch (MetadataException e)
        {
            throw new RetsServerException(e);
        }
    }

    public MSystem parseMetadata(Document document)
        throws RetsServerException
    {
        try
        {
            JDomCompactBuilder builder = new JDomCompactBuilder();
            mMetadata = builder.build(document);
            return parseMetadata();
        }
        catch (MetadataException e)
        {
            throw new RetsServerException(e);
        }
    }

    public MSystem parseMetadataDirectory(String metadataDir)
        throws RetsServerException
    {
        try
        {
            List files = IOUtils.listFilesRecursive(
                new File(metadataDir), new MetadataFileFilter());
            List documents = parseAllFiles(files);
            Document merged =
                JdomUtils.mergeDocuments(documents, new Element("RETS"));
            return parseMetadata(merged);
        }
        catch (IOException e)
        {
            throw new RetsServerException(e);
        }
        catch (JDOMException e)
        {
            throw new RetsServerException(e);
        }
    }

    /**
     * Parses all files, returning a list of JDOM Document objects.
     *
     * @param files list of File objects
     * @return a list of Document objects
     * @throws org.jdom.JDOMException if a JDOM error occurs
     * @throws java.io.IOException if an I/O error occurs
     */
    private static List /* Document */ parseAllFiles(List /* File */ files)
        throws JDOMException, IOException
    {
        List documents = new ArrayList();
        SAXBuilder builder = new SAXBuilder();
        for (int i = 0; i < files.size(); i++)
        {
            File file = (File) files.get(i);
            documents.add(builder.build(file));
        }
        return documents;
    }

    /**
     * Filters out directories and files that are not metadata, in particular
     * files used by the 1.0 version of the RETS server. Metadata files must
     * have a ".xml" extension. Certain directories, like Notices, Roles, and
     * Template do not contain any metadata, so they are skipped completely.
     */
    private class MetadataFileFilter implements FileFilter
    {
        public boolean accept(File file)
        {
            if (file.isDirectory())
            {
                return false;
            }

            // These directories do not contain metadata
            String parent = file.getParent();
            if (StringUtils.contains(parent, "Notices") ||
                StringUtils.contains(parent, "Roles") ||
                StringUtils.contains(parent, "Template") ||
                StringUtils.contains(parent, ".svn"))
            {
                return false;
            }

            if (file.getName().endsWith(".xml"))
            {
                return true;
            }
            // Everything else is not considered metadata
            return false;
        }
    }

    private MSystem parseMetadata()
    {
        mSystem = doSystem();
        doResource(mSystem);
        doClasses();
        doObjects();
        doSearchHelp();
        doEditMask();
        doLookup();
        doLookupTypes();
        doUpdateHelp();
        doValidationExternal();
        doUpdate();
        doTable();
        doValidationLookup();
        doValidationLookupType();
        doValidationExternalType();
        doValidationExpression();
        doUpdateType();
        doForeignKey(mSystem);

        return mSystem;
    }

    protected TableStandardName lookupTableStandardName(String standardName)
    {
        return (TableStandardName) mTableStandardNames.get(standardName);
    }

    protected void doClasses()
    {
        Iterator i = mResources.values().iterator();
        while (i.hasNext())
        {
            Resource resource = (Resource) i.next();
            Set hClasses = new ListOrderedSet();
            org.realtors.rets.common.metadata.types.MClass[] classes =
                    mMetadata.getResource(resource.getResourceID())
                    .getMClasses();
            for (int j = 0; j < classes.length; j++)
            {
                org.realtors.rets.common.metadata.types.MClass in = classes[j];
                MClass hClass = new MClass(in);

                hClass.setResource(resource);
                hClass.setClassName(in.getClassName());
                hClass.setStandardName(ClassStandardNameEnum.fromString(
                                           in.getStandardName()));
                hClass.setVisibleName(in.getVisibleName());
                hClass.setDescription(in.getDescription());

                String dbName = (String) in.getAttribute("X-DBName");
                LOG.debug("class: " + hClass.getClassName() + ", dbname: " +
                         dbName);
                if (dbName != null)
                {
                    hClass.setDbTable(dbName);
                }
                else
                {
                    StringBuffer tmp = new StringBuffer("rets_");
                    tmp.append(resource.getResourceID()).append("_");
                    tmp.append(hClass.getClassName());
                    hClass.setDbTable(tmp.toString().toLowerCase());
                }
                LOG.debug("real dbname: " + hClass.getDbTable());

                hClass.updateLevel();

                save(hClass);
                hClasses.add(hClass);
                mClasses.put(hClass.getPath(), hClass);
            }

            resource.setClasses(hClasses);
            save(resource);
        }
    }

    protected void doEditMask()
    {
        Iterator i = mResources.values().iterator();
        while (i.hasNext())
        {
            Resource resource = (Resource) i.next();
            Set hEditMasks = new ListOrderedSet();
            MEditMask[] editMasks = mMetadata.getResource(
                    resource.getResourceID()).getMEditMasks();
            for (int j = 0; j < editMasks.length; j++)
            {
                MEditMask in = editMasks[j];
                EditMask hEditMask = new EditMask();

                hEditMask.setResource(resource);
                hEditMask.setEditMaskID(in.getEditMaskID());
                hEditMask.setValue(in.getValue());
                // 1.7.2
                hEditMask.setMetadataEntryID(in.getMetadataEntryID());

                hEditMask.updateLevel();

                save(hEditMask);
                hEditMasks.add(hEditMask);
                mEditMasks.put(hEditMask.getPath(), hEditMask);
            }
            resource.setEditMasks(hEditMasks);
            save(resource);
        }
    }

    protected void doForeignKey(MSystem hSystem)
    {
        MForeignKey[] foreignKeys = mMetadata.getSystem().getMForeignKeys();

        Set hForeignKeys = new ListOrderedSet();
        for (int i = 0; i < foreignKeys.length; i++)
        {
            MForeignKey in = foreignKeys[i];
            ForeignKey hFk = new ForeignKey();

            hFk.setSystem(hSystem);

            hFk.setForeignKeyID(in.getForeignKeyID());
            hFk.setConditionalParentField(in.getConditionalParentField());
            hFk.setConditionalParentValue(in.getConditionalParentValue());
            
            String path[] = new String[3];
            path[0] = in.getParentResourceID();
            path[1] = in.getParentClassID();
            path[2] = in.getParentSystemName();
            String tablePath = StringUtils.join(path, ":");
            Table table = (Table) mTables.get(tablePath);
            hFk.setParentTable(table);

            path[0] = in.getChildResourceID();
            path[1] = in.getChildClassID();
            path[2] = in.getChildSystemName();
            tablePath = StringUtils.join(path, ":");
            table = (Table) mTables.get(tablePath);
            hFk.setChildTable(table);
            if (table == null)
            {
                LOG.error("table is null for path: " + tablePath);
            }

            save(hFk);
            hForeignKeys.add(hFk);
        }
        hSystem.setForeignKeys(hForeignKeys);
        save(hSystem);
    }

    protected void doLookup()
    {
        Iterator i = mResources.values().iterator();
        while (i.hasNext())
        {
            Resource resource = (Resource) i.next();
            Set hLookups = new ListOrderedSet();
            MLookup[] lookups =
                mMetadata.getResource(resource.getResourceID()).getMLookups();
            for (int j = 0; j < lookups.length; j++)
            {
                MLookup in = lookups[j];
                Lookup hLookup = new Lookup();

                hLookup.setResource(resource);

                hLookup.setLookupName(in.getLookupName());

                hLookup.setVisibleName(in.getVisibleName());
                // 1.7.2
                hLookup.setMetadataEntryID(in.getMetadataEntryID());
                
                hLookup.updateLevel();
                save(hLookup);
                // doLookupTypes(hLookup, rSession, hSession);
                hLookups.add(hLookup);
                mLookups.put(hLookup.getPath(), hLookup);

            }
            resource.setLookups(hLookups);
            save(resource);
        }
    }

    /**
     * Meant to be called only from do Lookup.
     *
     */
    protected void doLookupTypes()
    {
        Iterator i = mLookups.values().iterator();
        while (i.hasNext())
        {
            Lookup lookup = (Lookup) i.next();

            MLookupType[] lookupTypes =
                    mMetadata.getLookup(lookup.getResource().getResourceID(),
                                        lookup.getLookupName())
                    .getMLookupTypes();
            Set hLookupTypes = new ListOrderedSet();
            for (int j = 0; j < lookupTypes.length; j++)
            {
                MLookupType in = lookupTypes[j];
                LookupType hLookupType = new LookupType();

                hLookupType.setLookup(lookup);

                hLookupType.setLongValue(in.getLongValue());
                hLookupType.setShortValue(in.getShortValue());
                hLookupType.setValue(in.getValue());
                // 1.7.2
                hLookupType.setMetadataEntryID(in.getMetadataEntryID());

                hLookupType.updateLevel();

                save(hLookupType);
                hLookupTypes.add(hLookupType);
            }
            lookup.setLookupTypes(hLookupTypes);
            save(lookup);
        }
    }

    protected void doObjects()
    {
        Iterator i = mResources.values().iterator();
        while (i.hasNext())
        {
            Resource resource = (Resource) i.next();
            Set hObjects = new ListOrderedSet();
            org.realtors.rets.common.metadata.types.MObject[] objects =
                    mMetadata.getResource(resource.getResourceID())
                    .getMObjects();
            for (int j = 0; j < objects.length; j++)
            {
                org.realtors.rets.common.metadata.types.MObject in = objects[j];
                MObject hObject = new MObject();

                hObject.setResource(resource);

                hObject.setObjectType(ObjectTypeEnum.fromString(
                        in.getObjectType()));
                hObject.setMimeType(in.getMIMEType());
                hObject.setVisibleName(in.getVisibleName());
                hObject.setDescription(
                        StringUtils.substring(
                                in.getDescription(), 0, 64));
                // 1.7.2
                hObject.setMetadataEntryID(in.getMetadataEntryID());
                hObject.setObjectTimeStamp(in.getObjectTimeStamp());
                hObject.setObjectCount(in.getObjectCount());
                
                // Should we have an updateLevel?
                save(hObject);
                hObjects.add(hObject);
            }

            resource.setObjects(hObjects);
            save(resource);
        }
    }

    protected void doResource(MSystem hSystem)
    {

        MResource[] resources = mMetadata.getSystem().getMResources();
        Set hResources = new ListOrderedSet();
        for (int i = 0; i < resources.length; i++)
        {
            MResource in = resources[i];
            Resource hResource = new Resource();

            hResource.setSystem(hSystem);
            String resourceID = in.getResourceID();
            hResource.setResourceID(resourceID);
            hResource.setStandardName(ResourceStandardNameEnum.fromString(
                                          in.getStandardName()));
            hResource.setVisibleName(in.getVisibleName());
            hResource.setDescription(in.getDescription());
            hResource.setKeyField(in.getKeyField());

            hResource.updateLevel();

            save(hResource);
            hResources.add(hResource);
            mResources.put(hResource.getPath(), hResource);
        }

        hSystem.setResources(hResources);
        save(hSystem);
    }

    protected void doSearchHelp()
    {
        Iterator i = mResources.values().iterator();
        while (i.hasNext())
        {
            Resource resource = (Resource) i.next();
            Set hSearchHelps = new ListOrderedSet();
            MSearchHelp[] searchHelps = mMetadata
                    .getResource(resource.getResourceID())
                    .getMSearchHelps();
            for (int j = 0; j < searchHelps.length; j++)
            {
                MSearchHelp in = searchHelps[j];
                SearchHelp hSearchHelp = new SearchHelp();

                hSearchHelp.setResource(resource);
                hSearchHelp.setSearchHelpID(in.getSearchHelpID());
                hSearchHelp.setValue(in.getValue());
                // 1.7.2
                hSearchHelp.setMetadataEntryID(in.getMetadataEntryID());
                
                hSearchHelp.updateLevel();

                save(hSearchHelp);
                hSearchHelps.add(hSearchHelp);
                mSearchHelps.put(hSearchHelp.getPath(), hSearchHelp);
            }
            resource.setSearchHelps(hSearchHelps);
            save(resource);
        }
    }

    protected MSystem doSystem()
    {
        org.realtors.rets.common.metadata.types.MSystem system =
            mMetadata.getSystem();
        MSystem hSystem = new MSystem();
        hSystem.setVersion(system.getVersion());
        hSystem.setDate(system.getDate());
        hSystem.setSystemID(system.getSystemID());
        hSystem.setDescription(system.getSystemDescription());
        hSystem.setComments(system.getComment());
        hSystem.setTimeZoneOffset(system.getTimeZoneOffset());
        save(hSystem);
        return hSystem;
    }

    protected void doTable()
    {
        Iterator i = mClasses.values().iterator();
        while (i.hasNext())
        {
            MClass hClass = (MClass) i.next();
            String resourceName = hClass.getResource().getResourceID();
            String className = hClass.getClassName();
            LOG.debug("Getting tables for resource: " + resourceName +
                      ", class: " + className);
            Set hTables = new ListOrderedSet();
            MTable[] tables = mMetadata.getMClass(
                    resourceName, className).getMTables();
            for (int j = 0; j < tables.length; j++)
            {
                MTable in = tables[j];
                LOG.debug("Table system name: " + in.getSystemName() +
                          ", standard name: " + in.getStandardName());
                Table hTable = new Table();

                hTable.setMClass(hClass);

                hTable.setSystemName(in.getSystemName());

                String standardName = in.getStandardName();
                TableStandardName tsn =
                        lookupTableStandardName(standardName);
                if (tsn == null)
                {
                    if (standardName != null && !standardName.equals(""))
                    {
                        tsn = new TableStandardName(standardName);
                        mTableStandardNames.put(standardName, tsn);
                        save(tsn);
                    }
                }
                hTable.setStandardName(tsn);

                hTable.setLongName(in.getLongName());
                hTable.setDbName(in.getDBName());
                hTable.setShortName(in.getShortName());
                hTable.setMaximumLength(in.getMaximumLength());
                hTable.setDataType(DataTypeEnum.fromString(in.getDataType()));
                hTable.setPrecision(in.getPrecision());
                hTable.setSearchable(in.getSearchable());
                String interpretation = in.getInterpretation();
                if (interpretation != null)
                {
                    hTable.setInterpretation(
                        InterpretationEnum.fromString(interpretation));
                }
                String alignment = in.getAlignment();
                if (alignment != null)
                {
                    hTable.setAlignment(
                            AlignmentEnum.fromString(alignment));
                }
                hTable.setUseSeparator(in.getUseSeparator());
                String editMasksJoined = in.getEditMaskID();
                String resourcePath =  hClass.getResource().getPath();
                String path = null;
                Set hEditMasks = new ListOrderedSet();
                if (editMasksJoined != null)
                {
                    String editMasks[] =
                            StringUtils.split(editMasksJoined, ",");

                    for (int c = 0; c < editMasks.length; c++)
                    {
                        path = resourcePath + ":" +
                                StringUtils.trimToEmpty(editMasks[c]);
                        EditMask em = (EditMask) mEditMasks.get(path);
                        if (em != null)
                        {
                            hEditMasks.add(em);
                        }
                        else
                        {
                            LOG.error("Edit mask null for path: " + path);
                        }
                    }
                }
                hTable.setEditMasks(hEditMasks);

                String lookupName = in.getLookupName();
                path = resourcePath + ":" + lookupName;
                Lookup lookup = (Lookup) mLookups.get(path);
                hTable.setLookup(lookup);

                hTable.setMaxSelect(in.getMaxSelect());
                String units = in.getUnits();
                if (units != null)
                {
                    hTable.setUnits(UnitEnum.fromString(units));
                }
                hTable.setIndex(in.getIndex());
                hTable.setMinimum(in.getMinimum());
                hTable.setMaximum(in.getMaximum());
                hTable.setDefault(in.getDefault());
                hTable.setRequired(in.getRequired());

                String searchHelpID = in.getSearchHelpID();
                path = resourcePath + ":" + searchHelpID;
                SearchHelp searchHelp =
                        (SearchHelp) mSearchHelps.get(path);
                hTable.setSearchHelp(searchHelp);
                // String = md.getAttribute("unique");
                hTable.setUnique(in.getUnique());
                
                // 1.7.2
                hTable.setMetadataEntryID(in.getMetadataEntryID());
                hTable.setModTimeStamp(in.getModTimeStamp());
                hTable.setForeignKeyName(in.getForeignKeyName());
                hTable.setForeignField(in.getForeignField());
                hTable.setKeyQuery(in.getKeyQuery());
                hTable.setKeySelect(in.getKeySelect());
                hTable.setInKeyIndex(in.getInKeyIndex());
                
                hTable.updateLevel();

                save(hTable);
                hTables.add(hTable);
                mTables.put(hTable.getPath(), hTable);
            }
            hClass.setTables(hTables);
            save(hClass);
        }
    }

    protected void doUpdate()
    {
        Iterator i = mClasses.values().iterator();
        while (i.hasNext())
        {
            MClass clazz = (MClass) i.next();
            Set hUpdates = new ListOrderedSet();
            MUpdate[] updates = mMetadata.getMClass(
                    clazz.getResource().getResourceID(),
                    clazz.getClassName()).getMUpdates();
            for (int j = 0; j < updates.length; j++)
            {
                MUpdate in = updates[j];
                Update hUpdate = new Update();

                hUpdate.setMClass(clazz);

                hUpdate.setUpdateName(in.getUpdateName());
                hUpdate.setDescription(in.getDescription());
                hUpdate.setKeyField(in.getKeyField());

                hUpdate.setMetadataEntryID(in.getMetadataEntryID());
                hUpdate.setUpdateTypeVersion(in.getUpdateTypeVersion());
                hUpdate.setUpdateTypeDate(in.getUpdateTypeDate());

                hUpdate.updateLevel();

                save(hUpdate);
                hUpdates.add(hUpdate);
                mUpdates.put(hUpdate.getPath(), hUpdate);
            }
            clazz.setUpdates(hUpdates);
            save(clazz);
        }
    }

    protected void doUpdateHelp()
    {
        Iterator i = mResources.values().iterator();
        while (i.hasNext())
        {
            Resource resource = (Resource) i.next();
            Set hUpdateHelps = new ListOrderedSet();
            MUpdateHelp[] updateHelps = mMetadata.getResource(
                    resource.getResourceID()).getMUpdateHelps();

            for (int j = 0; j < updateHelps.length; j++)
            {
                MUpdateHelp in = updateHelps[j];
                UpdateHelp hUpdateHelp = new UpdateHelp();

                hUpdateHelp.setResource(resource);
                hUpdateHelp.setUpdateHelpID(in.getUpdateHelpID());
                hUpdateHelp.setValue(in.getValue());
                // 1.7.2
                hUpdateHelp.setMetadataEntryID(in.getMetadataEntryID());

                hUpdateHelp.updateLevel();

                save(hUpdateHelp);
                hUpdateHelps.add(hUpdateHelp);
                mUpdateHelps.put(hUpdateHelp.getPath(), hUpdateHelp);
            }

            resource.setUpdateHelps(hUpdateHelps);
            save(resource);
        }
    }

    public void doUpdateType()
    {
        Iterator i = mUpdates.values().iterator();
        while (i.hasNext())
        {
            Update update = (Update) i.next();
            Set hUpdateTypes = new ListOrderedSet();
            MUpdateType[] updateTypes = mMetadata.getUpdate(
                    update.getMClass().getResource().getResourceID(),
                    update.getMClass().getClassName(),
                    update.getUpdateName()).getMUpdateTypes();
            for (int j = 0; j < updateTypes.length; j++)
            {
                MUpdateType in = updateTypes[j];
                UpdateType updateType = new UpdateType();

                updateType.setUpdate(update);
                String level = update.getLevel();
                String systemName = in.getSystemName();
                String tablePath = level + ":" + systemName;
                Table table = (Table) mTables.get(tablePath);
                updateType.setTable(table);
                // Hack to get around metadata bug
                if (table == null)
                {
                    LOG.error("null table for path: " + tablePath);
                    throw new RuntimeException("bail:  null path");
                }

                updateType.setSequence(in.getSequence());

                String joinedAttributes = in.getAttributes();
                String attributes[] =
                        StringUtils.split(joinedAttributes, ",");
                Set attributeSet = new ListOrderedSet();
                for (int c = 0; c < attributes.length; c++)
                {
                    attributeSet.add(UpdateTypeAttributeEnum.fromInt(
                            Integer.parseInt(attributes[c])));
                }
                updateType.setAttributes(attributeSet);

                updateType.setDefault(in.getDefault());

                String valExp[] = StringUtils.split(
                        in.getValidationExpressionID(), ",");
                Set valExpSet = new ListOrderedSet();
                String resourcePath =
                        update.getMClass().getResource().getPath();
                if (valExp != null)
                {
                    for (int c = 0; c < valExp.length; c++)
                    {
                        String vePath = resourcePath + ":" + valExp[c];
                        ValidationExpression ve = (ValidationExpression)
                                mValidationExpressions.get(vePath);
                        valExpSet.add(ve);
                    }
                }
                updateType.setValidationExpressions(valExpSet);

                String updateHelpPath = resourcePath + ":" +
                        in.getUpdateHelpID();
                updateType.setUpdateHelp(
                        (UpdateHelp) mUpdateHelps.get(updateHelpPath));

                String vlPath = resourcePath + ":" +
                        in.getValidationLookupName();
                updateType.setValidationLookup(
                        (ValidationLookup) mValidationLookups.get(vlPath));

                String vePath = resourcePath + ":" +
                        in.getValidationExternalName();
                updateType.setValidationExternal(
                        (ValidationExternal) mValidationExternals.get(vePath));
                
                // 1.7.2
                updateType.setMetadataEntryID(in.getMetadataEntryID());
                updateType.setMaxUpdate(in.getMaxUpdate());
                
                save(updateType);
                hUpdateTypes.add(updateType);
            }
            update.setUpdateTypes(hUpdateTypes);
            save(update);
        }
    }

    protected void doValidationExpression()
    {
        Iterator i = mResources.values().iterator();
        while (i.hasNext())
        {
            Resource resource = (Resource) i.next();
            Set hValidationExpressions = new ListOrderedSet();
            MValidationExpression[] validationExpressions =
                    mMetadata.getResource(resource.getResourceID())
                    .getMValidationExpressions();
            for (int j = 0; j < validationExpressions.length; j++)
            {
                MValidationExpression in = validationExpressions[j];
                ValidationExpression ve = new ValidationExpression();

                ve.setResource(resource);
                ve.setValidationExpressionID(
                        in.getValidationExpressionID());
                ve.setValidationExpressionType(
                        ValidationExpressionTypeEnum.fromString(
                                in.getValidationExpressionType()));
                ve.setValue(in.getValue());
                // 1.7.2
                ve.setMetadataEntryID(in.getMetadataEntryID());

                ve.updateLevel();
                save(ve);
                hValidationExpressions.add(ve);
                mValidationExpressions.put(ve.getPath(), ve);
            }
            resource.setValidationExpressions(hValidationExpressions);
            save(resource);
        }
    }

    protected void doValidationExternal()
    {
        Iterator i = mResources.values().iterator();
        while (i.hasNext())
        {
            Resource resource = (Resource) i.next();
            Set hValidationExternals = new ListOrderedSet();
            MValidationExternal[] validationExternals =
                    mMetadata.getResource(resource.getResourceID())
                    .getMValidationExternal();
            for (int j = 0; j < validationExternals.length; j++)
            {
                MValidationExternal in = validationExternals[j];
                ValidationExternal hValidationExternal =
                        new ValidationExternal();

                hValidationExternal.setResource(resource);

                hValidationExternal.setValidationExternalName(
                        in.getValidationExternalName());
                // 1.7.2
                hValidationExternal.setMetadataEntryID(
                		in.getMetadataEntryID());

                // get the search class
                String path = in.getSearchResource() + ":" +
                        in.getSearchClass();
                MClass clazz = (MClass) mClasses.get(path);
                hValidationExternal.setSearchClass(clazz);

                hValidationExternal.updateLevel();

                save(hValidationExternal);
                hValidationExternals.add(hValidationExternal);
                mValidationExternals.put(hValidationExternal.getPath(),
                                         hValidationExternal);
            }
            resource.setValidationExternals(hValidationExternals);
            save(resource);
        }
    }

    /**
     * Intended to be called by doValidationExternal.
     *
     */
    protected void doValidationExternalType()
    {
        Iterator i = mValidationExternals.values().iterator();
        while (i.hasNext())
        {
            ValidationExternal ve = (ValidationExternal) i.next();
            Set hValdationExternalTypes = new ListOrderedSet();
            MValidationExternalType[] validationExternalTypes =
                    mMetadata.getValidationExternal(
                            ve.getResource().getResourceID(),
                            ve.getValidationExternalName())
                    .getMValidationExternalTypes();

            for (int j = 0; j < validationExternalTypes.length; j++)
            {
                MValidationExternalType in = validationExternalTypes[j];
                ValidationExternalType vet = new ValidationExternalType();

                vet.setValidationExternal(ve);

                String joinedSearchField = in.getSearchField();
                String searchField[] =
                        StringUtils.split(joinedSearchField, ",");
                Set searchFieldSet = new ListOrderedSet();
                for (int c = 0; c < searchField.length; c++)
                {
                    searchFieldSet.add(
                            StringUtils.trimToEmpty(searchField[c]));
                }
                vet.setSearchField(searchFieldSet);

                String joinedDisplayField =
                        in.getDisplayField();
                String displayFields[] =
                        StringUtils.split(joinedDisplayField, ",");
                Set displayFieldSet = new ListOrderedSet();
                for (int c = 0; c < displayFields.length; c++)
                {
                    displayFieldSet.add(
                            StringUtils.trimToEmpty(displayFields[c]));
                }
                vet.setDisplayField(displayFieldSet);

                String joinedResultField =
                        in.getResultFields();
                String resultFields[] =
                        StringUtils.split(joinedResultField, ",");
                Map resultFieldMap = new ListOrderedMap();
                for (int c = 0; c < resultFields.length; c++)
                {
                    String split[] =
                            StringUtils.split(resultFields[c], "=", 2);
                    resultFieldMap.put(StringUtils.trimToEmpty(split[0]),
                                       StringUtils.trimToEmpty(split[1]));
                }
                // 1.7.2
                vet.setMetadataEntryID(in.getMetadataEntryID());
                
                vet.setResultFields(resultFieldMap);

                vet.updateLevel();
                save(vet);
            }
            ve.setValidationExternalTypes(hValdationExternalTypes);
            save(ve);
        }
    }

    /**
     * Requires that Table to be done.
     *
     */
    protected void doValidationLookup()
    {
        Iterator i = mResources.values().iterator();
        while (i.hasNext())
        {
            Resource resource = (Resource) i.next();
            Set hValidationLookups = new ListOrderedSet();
            MValidationLookup[] validationLookups =
                mMetadata.getResource(resource.getResourceID())
                    .getMValidationLookups();
            for (int j = 0; j < validationLookups.length; j++)
            {
                MValidationLookup in = validationLookups[j];
                ValidationLookup hvl = new ValidationLookup();

                hvl.setResource(resource);

                hvl.setValidationLookupName(in.getValidationLookupName());

                hvl.setParent1Field(in.getParent1Field());
                hvl.setParent2Field(in.getParent2Field());
                // 1.7.2
                hvl.setMetadataEntryID(in.getMetadataEntryID());

                hvl.updateLevel();

                save(hvl);
                hValidationLookups.add(hvl);
                mValidationLookups.put(hvl.getPath(), hvl);
            }
            resource.setValidationLookups(hValidationLookups);
            save(resource);
        }
    }

    protected void doValidationLookupType()
    {
        Iterator i = mValidationLookups.values().iterator();
        while (i.hasNext())
        {
            ValidationLookup vl = (ValidationLookup) i.next();
            Set hValdationLookupTypes = new ListOrderedSet();
            MValidationLookupType[] validationLookupTypes =
                    mMetadata.getValidationLookup(
                            vl.getResource().getResourceID(),
                            vl.getValidationLookupName())
                    .getMValidationLookupTypes();

            for (int j = 0; j < validationLookupTypes.length; j++)
            {
                MValidationLookupType in = validationLookupTypes[j];
                ValidationLookupType vlt = new ValidationLookupType();

                vlt.setValidationLookup(vl);
                vlt.setValidText(in.getValidText());
                vlt.setParent1Value(in.getParent1Value());
                vlt.setParent2Value(in.getParent2Value());
                // 1.7.2
                vlt.setMetadataEntryID(in.getMetadataEntryID());

                vlt.updateLevel();

                save(vlt);
                hValdationLookupTypes.add(vlt);
            }
            vl.setValidationLookupTypes(hValdationLookupTypes);
            save(vl);
        }
    }

    private MSystem mSystem;
    public static final String CVSID =
        "$Id: MetadataLoader.java,v 1.10 2004/12/15 20:57:18 dribin Exp $";

    private static final Logger LOG = Logger.getLogger(MetadataLoader.class);
    protected Map mClasses;
    protected Map mEditMasks;
    protected Map mForeignKeys;
    protected Map mLookups;
    protected Map mResources;
    protected Map mSearchHelps;
    protected Map mTables;
    protected Map mUpdateHelps;
    protected Map mUpdates;
    protected Map mValidationExpressions;
    protected Map mValidationExternals;
    protected Map mValidationLookups;
    protected Map mTableStandardNames;
    protected Metadata mMetadata;
    protected long mNextId;
}
