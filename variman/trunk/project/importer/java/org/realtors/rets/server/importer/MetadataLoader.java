package org.realtors.rets.server.importer;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import org.apache.log4j.Logger;

import org.realtors.rets.client.Metadata;
import org.realtors.rets.client.MetadataTable;
import org.realtors.rets.client.RetsException;
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


/**
 * Helper method to load Metadata from a file.
 * 
 * @author kgarner
 */
public class MetadataLoader extends MetadataHelpers
{
    /**
     * Creates a new <code>MetadataImporter</code> instance.
     *
     */
    public MetadataLoader()
        throws RetsException
    {
        super();
    }

    private boolean boolValue(String bString)
    {
        return bString.equalsIgnoreCase("true") || 
               bString.equalsIgnoreCase("1");
    }

    private void doClasses()
    {
        MetadataTable tClass = mMetadataTables.getTable(MetadataTable.CLASS);

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

                hClass.setResource(resource);
                String className = md.getAttribute("ClassName");
                hClass.setClassName(className);
                hClass.setStandardName(ClassStandardNameEnum.fromString(
                                           md.getAttribute("StandardName")));
                hClass.setVisibleName(md.getAttribute("VisibleName"));
                hClass.setDescription(md.getAttribute("Description"));
                
                StringBuffer tmp = new StringBuffer("rets_");
                tmp.append(resource.getResourceID()).append("_");
                tmp.append(hClass.getClassName());
                hClass.setDbTable(tmp.toString());

                hClass.updateLevel();

                hClasses.add(hClass);
                mClasses.put(hClass.getPath(), hClass);
            }

            resource.setClasses(hClasses);
        }
    }

    private void doEditMask()
    {
        MetadataTable tEditMask =
            mMetadataTables.getTable(MetadataTable.EDITMASK);

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

                    hEditMask.setResource(resource);
                    hEditMask.setEditMaskID(md.getAttribute("EditMaskID"));
                    hEditMask.setValue(md.getAttribute("Value"));

                    hEditMask.updateLevel();

                    hEditMasks.add(hEditMask);
                    mEditMasks.put(hEditMask.getPath(), hEditMask);
                }
            }
            resource.setEditMasks(hEditMasks);
        }
    }

    private void doForeignKey(MSystem hSystem)
    {
        MetadataTable tForeignKeys =
            mMetadataTables.getTable(MetadataTable.FOREIGN_KEYS);

        Set hForeignKeys = new HashSet();
        List foreignKeys = tForeignKeys.getDataRows("");
        if (foreignKeys != null)
        {
            Iterator i = foreignKeys.iterator();
            while (i.hasNext())
            {
                Metadata md = (Metadata) i.next();
                ForeignKey hFk = new ForeignKey();

                hFk.setSystem(hSystem);

                hFk.setForeignKeyID(md.getAttribute("ForeignKeyID"));
                String path[] = new String[3];
                path[0] = md.getAttribute("ParentResourceID");
                path[1] = md.getAttribute("ParentClassID");
                path[2] = md.getAttribute("ParentSystemName");
                String tablePath = StringUtils.join(path, ":");
                Table table = (Table) mTables.get(tablePath);
                hFk.setParentTable(table);

                path[0] = md.getAttribute("ChildResourceID");
                path[1] = md.getAttribute("ChildClassID");
                path[2] = md.getAttribute("ChildSystemName");
                tablePath = StringUtils.join(path, ":");
                table = (Table) mTables.get(tablePath);
                hFk.setChildTable(table);
                if (table == null)
                {
                    LOG.error("table is null for path: " + tablePath);
                }

                hForeignKeys.add(hFk);
            }
        }
        hSystem.setForeignKeys(hForeignKeys);
    }

    private void doLookup()
    {
        MetadataTable tLookup =
            mMetadataTables.getTable(MetadataTable.LOOKUP);

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

                    hLookup.setResource(resource);

                    String lookupName = md.getAttribute("LookupName");
                    hLookup.setLookupName(lookupName);

                    hLookup.setVisibleName(md.getAttribute("VisibleName"));

                    hLookup.updateLevel();
                    hLookups.add(hLookup);
                    mLookups.put(hLookup.getPath(), hLookup);
                }
            }
            resource.setLookups(hLookups);
        }
    }

    /**
     * Meant to be called only from do Lookup.
     */
    private void doLookupTypes()
    {
        MetadataTable tLookupTypes =
            mMetadataTables.getTable(MetadataTable.LOOKUP_TYPE);

        Iterator i = mLookups.values().iterator();
        while (i.hasNext())
        {
            Lookup lookup = (Lookup) i.next();
            List lookupTypes = tLookupTypes.getDataRows(lookup.getPath());
            Set hLookupTypes = new HashSet();
            if (lookupTypes != null)
            {
                Iterator j = lookupTypes.iterator();
                while (j.hasNext())
                {
                    Metadata md = (Metadata) j.next();
                    LookupType hLookupType = new LookupType();

                    hLookupType.setLookup(lookup);

                    hLookupType.setLongValue(md.getAttribute("LongValue"));
                    hLookupType.setShortValue(md.getAttribute("ShortValue"));
                    hLookupType.setValue(md.getAttribute("Value"));

                    hLookupType.updateLevel();

                    hLookupTypes.add(hLookupType);
                }
            }
            lookup.setLookupTypes(hLookupTypes);
        }
    }

    private void doObjects()
    {
        MetadataTable tObject =
            mMetadataTables.getTable(MetadataTable.OBJECT);

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

                    hObject.setResource(resource);

                    hObject.setObjectType(ObjectTypeEnum.fromString(
                                              md.getAttribute("ObjectType")));
                    hObject.setMimeType(md.getAttribute("MimeType"));
                    hObject.setVisibleName(md.getAttribute("VisibleName"));
                    hObject.setDescription(
                        StringUtils.substring(
                            md.getAttribute("Description"), 0, 64));

                    hObjects.add(hObject);
                }
            }

            resource.setObjects(hObjects);
        }
    }

    private void doResource(MSystem hSystem)
    {
        MetadataTable tResource =
            mMetadataTables.getTable(MetadataTable.RESOURCE);

        Set hResources = new HashSet();
        List resources = tResource.getDataRows("");
        Iterator i = resources.iterator();
        while (i.hasNext())
        {
            Metadata md = (Metadata) i.next();
            Resource hResource = new Resource();

            hResource.setSystem(hSystem);
            String resourceID = md.getAttribute("ResourceID");
            hResource.setResourceID(resourceID);
            hResource.setStandardName(ResourceStandardNameEnum.fromString(
                                          md.getAttribute("StandardName")));
            hResource.setVisibleName(md.getAttribute("VisibleName"));
            hResource.setDescription(md.getAttribute("Description"));
            hResource.setKeyField(md.getAttribute("KeyField"));

            hResource.updateLevel();

            hResources.add(hResource);
            mResources.put(hResource.getPath(), hResource);
        }

        hSystem.setResources(hResources);
    }

    private void doSearchHelp()
    {
        MetadataTable tSearchHelp =
            mMetadataTables.getTable(MetadataTable.SEARCH_HELP);

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

                    hSearchHelp.setResource(resource);
                    hSearchHelp.setSearchHelpID(
                        md.getAttribute("SearchHelpID"));
                    hSearchHelp.setValue(md.getAttribute("Value"));

                    hSearchHelp.updateLevel();

                    hSearchHelps.add(hSearchHelp);
                    mSearchHelps.put(hSearchHelp.getPath(), hSearchHelp);
                }
            }

            resource.setSearchHelps(hSearchHelps);
        }
    }

    private MSystem doSystem()
    {
        MSystem hSystem = new MSystem();
        hSystem.setVersion(101001);
        hSystem.setDate(Calendar.getInstance().getTime());
        return hSystem;
    }

    private void doTable()
    {
        MetadataTable tTables =
            mMetadataTables.getTable(MetadataTable.TABLE);

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

                    hTable.setMClass(hClass);

                    hTable.setSystemName(md.getAttribute("SystemName"));

                    String standardName = md.getAttribute("StandardName");
                    TableStandardName tsn =
                        lookupTableStandardName(standardName);
                    if (tsn == null)
                    {
                        if (!standardName.equals(""))
                        {
                            tsn = new TableStandardName(standardName);
                            mTableStandardNames.put(standardName, tsn);
                        }
                    }
                    hTable.setStandardName(tsn);

                    hTable.setLongName(md.getAttribute("LongName"));

                    String tmp = md.getAttribute("DbName");
                    if (tmp.startsWith("r_"))
                    {
                        hTable.setDbName(StringUtils.substring(tmp, 0, 10));
                    }
                    else
                    {
                        hTable.setDbName(
                            StringUtils.substring("r_" + tmp, 0, 10));
                    }                        
                        
                    hTable.setShortName(md.getAttribute("ShortName"));
                    hTable.setMaximumLength(
                        Integer.parseInt(md.getAttribute("MaximumLength")));
                    hTable.setDataType(
                        DataTypeEnum.fromString(md.getAttribute("DataType")));
                    hTable.setPrecision(
                        Integer.parseInt(md.getAttribute("Precision")));
                    hTable.setSearchable(
                        boolValue(md.getAttribute("Searchable")));
                    hTable.setInterpretation(
                        InterpretationEnum.fromString(
                            md.getAttribute("Interpretation")));
                    hTable.setAlignment(
                        AlignmentEnum.fromString(
                            md.getAttribute("Alignment")));
                    hTable.setUseSeparator(
                        boolValue(md.getAttribute("UseSeparator")));

                    String editMasksJoined = md.getAttribute("EditMaskID");
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

                    String searchHelpID = md.getAttribute("SearchHelpID");
                    path = resourcePath + ":" + searchHelpID;
                    SearchHelp searchHelp =
                        (SearchHelp) mSearchHelps.get(path);
                    hTable.setSearchHelp(searchHelp);

                    // String = md.getAttribute("unique");
                    hTable.setUnique(boolValue(md.getAttribute("Unique")));

                    hTable.updateLevel();

                    hTables.add(hTable);
                    mTables.put(hTable.getPath(), hTable);
                }
                hClass.setTables(hTables);
            }
        }
    }

    private void doUpdate()
    {
        MetadataTable tUpdates =
            mMetadataTables.getTable(MetadataTable.UPDATE);

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

                    hUpdate.setMClass(clazz);

                    hUpdate.setUpdateName(md.getAttribute("UpdateName"));
                    hUpdate.setDescription(md.getAttribute("Description"));
                    hUpdate.setKeyField(md.getAttribute("KeyField"));

                    hUpdate.updateLevel();

                    hUpdates.add(hUpdate);
                    mUpdates.put(hUpdate.getPath(), hUpdate);
                }
            }
            clazz.setUpdates(hUpdates);
        }
    }

    private void doUpdateHelp()
    {
        MetadataTable tUpdateHelps =
            mMetadataTables.getTable(MetadataTable.UPDATE_HELP);

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

                    hUpdateHelp.setResource(resource);
                    hUpdateHelp.setUpdateHelpID(
                        md.getAttribute("UpdateHelpID"));
                    hUpdateHelp.setValue(md.getAttribute("Value"));

                    hUpdateHelp.updateLevel();

                    hUpdateHelps.add(hUpdateHelp);
                    mUpdateHelps.put(hUpdateHelp.getPath(), hUpdateHelp);
                }
            }
            resource.setUpdateHelps(hUpdateHelps);
        }
    }

    private void doUpdateType()
    {
        MetadataTable tUpdateTypes =
            mMetadataTables.getTable(MetadataTable.UPDATE_TYPE);

        Iterator i = mUpdates.values().iterator();
        while (i.hasNext())
        {
            Update update = (Update) i.next();
            Set hUpdateTypes = new HashSet();
            List updateTypes = tUpdateTypes.getDataRows(update.getPath());
            if (updateTypes != null)
            {
                Iterator j = updateTypes.iterator();
                while (j.hasNext())
                {
                    Metadata md = (Metadata) j.next();
                    UpdateType updateType = new UpdateType();

                    updateType.setUpdate(update);
                    String level = update.getLevel();
                    String systemName = md.getAttribute("SystemName");
                    String tablePath = level + ":" + systemName;
                    Table table = (Table) mTables.get(tablePath);
                    updateType.setTable(table);
                    // Hack to get around metadata bug
                    if (table == null)
                    {
                        LOG.error("null table for path: " + tablePath);
                        System.exit(1);
                    }

                    updateType.setSequence(
                        Integer.parseInt(md.getAttribute("Sequence")));

                    String joinedAttributes = md.getAttribute("Attributes");
                    String attributes[] =
                        StringUtils.split(joinedAttributes, ",");
                    Set attributeSet = new HashSet();
                    for (int c = 0; c < attributes.length; c++)
                    {
                        attributeSet.add(UpdateTypeAttributeEnum.fromInt(
                                             Integer.parseInt(attributes[c])));
                    }
                    updateType.setAttributes(attributeSet);

                    updateType.setDefault(md.getAttribute("Default"));

                    String valExp[] = StringUtils.split(
                        md.getAttribute("ValidationExpressionID"), ",");
                    Set valExpSet = new HashSet();
                    String resourcePath =
                        update.getMClass().getResource().getPath();
                    for (int c = 0; c < valExp.length; c++)
                    {
                        String vePath = resourcePath + ":" + valExp[c];
                        ValidationExpression ve = (ValidationExpression)
                            mValidationExpressions.get(vePath);
                        valExpSet.add(ve);
                    }
                    updateType.setValidationExpressions(valExpSet);

                    String updateHelpPath = resourcePath + ":" +
                        md.getAttribute("UpdateHelpID");
                    updateType.setUpdateHelp(
                        (UpdateHelp) mUpdateHelps.get(updateHelpPath));

                    String vlPath = resourcePath + ":" +
                        md.getAttribute("ValdiationLookupName");
                    updateType.setValidationLookup(
                        (ValidationLookup) mValidationLookups.get(vlPath));

                    String vePath = resourcePath + ":" +
                        md.getAttribute("ValdationExternalName");
                    updateType.setValidationExternal(
                        (ValidationExternal) mValidationExternals.get(vePath));

                    hUpdateTypes.add(updateType);
                }
            }
            update.setUpdateTypes(hUpdateTypes);
        }
    }

    private void doValidationExpression()
    {
        MetadataTable tValidationExpressions =
            mMetadataTables.getTable(MetadataTable.VALIDATION_EXPRESSION);

        Iterator i = mResources.values().iterator();
        while (i.hasNext())
        {
            Resource resource = (Resource) i.next();
            Set hValidationExpressions = new HashSet();
            List validationExpressions =
                tValidationExpressions.getDataRows(resource.getPath());
            if (validationExpressions != null)
            {
                Iterator j = validationExpressions.iterator();
                while (j.hasNext())
                {
                    Metadata md = (Metadata) j.next();
                    ValidationExpression ve = new ValidationExpression();

                    ve.setResource(resource);
                    ve.setValidationExpressionID(
                        md.getAttribute("ValidationExpressionID"));
                    ve.setValidationExpressionType(
                        ValidationExpressionTypeEnum.fromString(
                            md.getAttribute("ValidationExpressionType")));
                    ve.setValue(md.getAttribute("value"));

                    ve.updateLevel();

                    hValidationExpressions.add(ve);
                    mValidationExpressions.put(ve.getPath(), ve);
                }
            }
            resource.setValidationExpressions(hValidationExpressions);
        }
    }

    private void doValidationExternal()
    {
        MetadataTable tValidationExternals =
            mMetadataTables.getTable(MetadataTable.VALIDATION_EXTERNAL);

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

                    hValidationExternal.setResource(resource);

                    hValidationExternal.setValidationExternalName(
                        md.getAttribute("ValidationExternalName"));

                    // get the search class
                    String path = md.getAttribute("SearchResource") + ":" +
                        md.getAttribute("SearchClass");
                    MClass clazz = (MClass) mClasses.get(path);
                    hValidationExternal.setSearchClass(clazz);

                    hValidationExternal.updateLevel();

                    hValidationExternals.add(hValidationExternal);
                    mValidationExternals.put(hValidationExternal.getPath(),
                                             hValidationExternal);
                }
            }
            resource.setValidationExternals(hValidationExternals);
        }
    }


    /**
     * Intended to be called by doValidationExternal.
     */
    private void doValidationExternalType()
    {
        MetadataTable tValidationExternalTypes =
            mMetadataTables.getTable(MetadataTable.VALIDATION_EXTERNAL_TYPE);

        Iterator i = mValidationExternals.values().iterator();
        while (i.hasNext())
        {
            ValidationExternal ve = (ValidationExternal) i.next();
            Set hValdationExternalTypes = new HashSet();
            List validationExternalTypes =
                tValidationExternalTypes.getDataRows(ve.getPath());
            if (validationExternalTypes != null)
            {
                Iterator j = validationExternalTypes.iterator();
                while (j.hasNext())
                {
                    Metadata md = (Metadata) j.next();
                    ValidationExternalType vet = new ValidationExternalType();

                    vet.setValidationExternal(ve);

                    String joinedSearchField = md.getAttribute("SearchField");
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
                        md.getAttribute("DisplayField");
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
                        md.getAttribute("ResultFields");
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

                    vet.updateLevel();
                }
                ve.setValidationExternalTypes(hValdationExternalTypes);
            }
        }
    }

    /**
     * Requires that Table to be done.
     */
    private void doValidationLookup()
    {
        MetadataTable tValidationLookups =
            mMetadataTables.getTable(MetadataTable.VALIDATION_LOOKUP);

        Iterator i = mResources.values().iterator();
        while (i.hasNext())
        {
            Resource resource = (Resource) i.next();
            Set hValidationLookups = new HashSet();
            String resourcePath = resource.getPath();
            List validationLookups =
                tValidationLookups.getDataRows(resourcePath);
            if (validationLookups != null)
            {
                Iterator j = validationLookups.iterator();
                while (j.hasNext())
                {
                    Metadata md = (Metadata) j.next();
                    ValidationLookup hvl = new ValidationLookup();

                    hvl.setResource(resource);

                    hvl.setValidationLookupName(
                        md.getAttribute("ValidationLookupName"));

                    hvl.setParent1Field(md.getAttribute("Parent1Field"));
                    hvl.setParent2Field(md.getAttribute("Parent2Field"));

                    hvl.updateLevel();

                    hValidationLookups.add(hvl);
                    mValidationLookups.put(hvl.getPath(), hvl);
                }
                resource.setValidationLookups(hValidationLookups);
            }
        }
    }

    private void doValidationLookupType()
    {
        MetadataTable tValidationLookupTypes =
            mMetadataTables.getTable(MetadataTable.VALIDATION_LOOKUP_TYPE);

        Iterator i = mValidationLookups.values().iterator();
        while (i.hasNext())
        {
            ValidationLookup vl = (ValidationLookup) i.next();
            Set hValdationLookupTypes = new HashSet();
            List validationLookupTypes =
                tValidationLookupTypes.getDataRows(vl.getPath());
            if (validationLookupTypes != null)
            {
                Iterator j = validationLookupTypes.iterator();
                while (j.hasNext())
                {
                    Metadata md = (Metadata) j.next();
                    ValidationLookupType vlt = new ValidationLookupType();

                    vlt.setValidationLookup(vl);
                    vlt.setValidText(md.getAttribute("ValidText"));
                    vlt.setParent1Value(md.getAttribute("Parent1Value"));
                    vlt.setParent2Value(md.getAttribute("Parent2Value"));

                    vlt.updateLevel();

                    hValdationLookupTypes.add(vlt);
                }
            }
            vl.setValidationLookupTypes(hValdationLookupTypes);
        }
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

    private MSystem mSystem;
    public static final String CVSID =
        "$Id: MetadataLoader.java,v 1.9 2003/12/04 20:42:36 dribin Exp $";

    private static final Logger LOG = Logger.getLogger(MetadataLoader.class);
}
