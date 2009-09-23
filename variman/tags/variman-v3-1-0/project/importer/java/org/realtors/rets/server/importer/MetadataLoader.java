/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.importer;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

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
import org.realtors.rets.server.metadata.AlignmentEnum;
import org.realtors.rets.server.metadata.ClassStandardNameEnum;
import org.realtors.rets.server.metadata.DataTypeEnum;
import org.realtors.rets.server.metadata.EditMask;
import org.realtors.rets.server.metadata.ForeignKey;
import org.realtors.rets.server.metadata.InterpretationEnum;
import org.realtors.rets.server.metadata.Lookup;
import org.realtors.rets.server.metadata.LookupType;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.ObjectTypeEnum;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.ResourceStandardNameEnum;
import org.realtors.rets.server.metadata.SearchHelp;
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.TableStandardName;
import org.realtors.rets.server.metadata.UnitEnum;
import org.realtors.rets.server.metadata.Update;
import org.realtors.rets.server.metadata.UpdateHelp;
import org.realtors.rets.server.metadata.UpdateType;
import org.realtors.rets.server.metadata.UpdateTypeAttributeEnum;
import org.realtors.rets.server.metadata.ValidationExpression;
import org.realtors.rets.server.metadata.ValidationExpressionTypeEnum;
import org.realtors.rets.server.metadata.ValidationExternal;
import org.realtors.rets.server.metadata.ValidationExternalType;
import org.realtors.rets.server.metadata.ValidationLookup;
import org.realtors.rets.server.metadata.ValidationLookupType;
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
        mResources = new HashMap();
        mClasses = new HashMap();
        mEditMasks = new HashMap();
        mTables = new HashMap();
        mLookups = new HashMap();
        mSearchHelps = new HashMap();
        mValidationExternals = new HashMap();
        mValidationExpressions = new HashMap();
        mValidationLookups = new HashMap();
        mUpdates = new HashMap();
        mUpdateHelps = new HashMap();
        mTableStandardNames = new HashMap();
    }

    protected void save(Object object)
    {
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
    {
        loadMetadataTables(in);
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

    private void loadMetadataTables(InputStream in)
    {
        JDomCompactBuilder builder = new JDomCompactBuilder();
        try
        {
            mMetadata = builder.build(new InputSource(in));
        }
        catch (MetadataException e)
        {
            throw new RuntimeException(e.getMessage());

        }
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
            Set hClasses = new HashSet();
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

                StringBuffer tmp = new StringBuffer("rets_");
                tmp.append(resource.getResourceID()).append("_");
                tmp.append(hClass.getClassName());
                hClass.setDbTable(tmp.toString().toLowerCase());

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
            Set hEditMasks = new HashSet();
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

        Set hForeignKeys = new HashSet();
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
            Set hLookups = new HashSet();
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
            Set hLookupTypes = new HashSet();
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
            Set hObjects = new HashSet();
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
        Set hResources = new HashSet();
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
            Set hSearchHelps = new HashSet();
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
        MSystem hSystem = new MSystem();
        hSystem.setVersion(10100000);
        hSystem.setDate(Calendar.getInstance().getTime());
        hSystem.setTimeZoneOffset("-06:00");
        save(hSystem);
        return hSystem;
    }

    protected void doTable()
    {
        Iterator i = mClasses.values().iterator();
        while (i.hasNext())
        {
            MClass hClass = (MClass) i.next();
            Set hTables = new HashSet();
            MTable[] tables = mMetadata.getMClass(
                    hClass.getResource().getResourceID(),
                    hClass.getClassName()).getMTables();
            for (int j = 0; j < tables.length; j++)
            {
                MTable in = tables[j];
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

                String tmp = in.getDBName().toLowerCase();
                if (tmp.startsWith("r_"))
                {
                    hTable.setDbName(StringUtils.substring(tmp, 0, 10));
                }
                else
                {
                    hTable.setDbName(
                            StringUtils.substring("r_" + tmp, 0, 10));
                }

                hTable.setShortName(in.getShortName());
                hTable.setMaximumLength(in.getMaximumLength());
                hTable.setDataType(DataTypeEnum.fromString(in.getDataType()));
                hTable.setPrecision(in.getPrecision());
                hTable.setSearchable(in.getSearchable());
                String interpretation = in.getInterpretation();
                if (interpretation != null)
                {
                    hTable.setInterpretation(
                        InterpretationEnum.fromString(
                            interpretation));
                }
                hTable.setAlignment(
                        AlignmentEnum.fromString(
                               in.getAlignment()));
                hTable.setUseSeparator(in.getUseSeparator());
                String editMasksJoined = in.getEditMaskID();
                String resourcePath =  hClass.getResource().getPath();
                String path = null;
                Set hEditMasks = new HashSet();
                if (editMasksJoined != null)
                {
                    String editMasks[] =
                            StringUtils.split(editMasksJoined, ",");

                    for (int c = 0; c < editMasks.length; c++)
                    {
                        path = resourcePath + ":" +
                                StringUtils.trimToEmpty(editMasks[c]);
                        EditMask em = (EditMask) mEditMasks.get(path);
                        hEditMasks.add(em);
                        if (em == null)
                        {
                            LOG.error("edit mask null for path: " + path);
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
            Set hUpdates = new HashSet();
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
            Set hUpdateHelps = new HashSet();
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
            Set hUpdateTypes = new HashSet();
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
                Set attributeSet = new HashSet();
                for (int c = 0; c < attributes.length; c++)
                {
                    attributeSet.add(UpdateTypeAttributeEnum.fromInt(
                            Integer.parseInt(attributes[c])));
                }
                updateType.setAttributes(attributeSet);

                updateType.setDefault(in.getDefault());

                String valExp[] = StringUtils.split(
                        in.getValidationExpressionID(), ",");
                Set valExpSet = new HashSet();
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
            Set hValidationExpressions = new HashSet();
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
            Set hValidationExternals = new HashSet();
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
            Set hValdationExternalTypes = new HashSet();
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
                Set searchFieldSet = new HashSet();
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
                Set displayFieldSet = new HashSet();
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
                Map resultFieldMap = new HashMap();
                for (int c = 0; c < resultFields.length; c++)
                {
                    String split[] =
                            StringUtils.split(resultFields[c], "=", 2);
                    resultFieldMap.put(StringUtils.trimToEmpty(split[0]),
                                       StringUtils.trimToEmpty(split[1]));
                }
                vet.setResultFields(resultFieldMap);
                // 1.7.2
                vet.setMetadataEntryID(in.getMetadataEntryID());

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
            Set hValidationLookups = new HashSet();
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
            Set hValdationLookupTypes = new HashSet();
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
        "$Id: MetadataLoader.java,v 1.15 2004/03/31 15:37:20 dribin Exp $";

    private static final Logger LOG = Logger.getLogger(MetadataLoader.class);
    protected Map mClasses;
    protected Map mEditMasks;
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
}
