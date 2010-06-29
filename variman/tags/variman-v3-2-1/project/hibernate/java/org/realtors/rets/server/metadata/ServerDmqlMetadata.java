/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004,2007 The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import org.apache.log4j.Logger;

import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MLookup;
import org.realtors.rets.common.metadata.types.MLookupType;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.server.dmql.DmqlFieldType;
import org.realtors.rets.server.dmql.DmqlParserMetadata;

public class ServerDmqlMetadata implements DmqlParserMetadata
{
    private ServerDmqlMetadata()
    {
        mBooleans = new LinkedHashSet<String>();
        mFields = new LinkedHashMap<String, MTable>();
        mFieldTypes = new LinkedHashMap<String, DmqlFieldType>();
        mFieldToColumn = new LinkedHashMap<String, String>();
        mColumnToField = new LinkedHashMap<String, String>();
        mLookupsDbMap = new LinkedHashMap<String, Map<String, String>>();
        mLookupTypesMap = new LinkedHashMap<String, Map<String, MLookupType>>();
        mColumns = new ArrayList<String>();
    }

    public ServerDmqlMetadata(MClass clazz, boolean standardNames)
    {
        this();
        init(clazz.getChildren(MetadataType.TABLE), standardNames);
    }

    public ServerDmqlMetadata(Collection<? extends MetaObject> tables, boolean standardNames)
    {
        this();
        init(tables, standardNames);
    }

    private void init(Collection<? extends MetaObject> tables, boolean standardNames)
    {
        for (Iterator i = tables.iterator(); i.hasNext();)
        {
            MTable table = (MTable) i.next();
            String dbName = table.getDBName();

            String fieldName = getTableName(table,  standardNames);
            if (fieldName == null)
            {
                // Skip tables that have no field name.  Technically, this
                // should only happen for tables that have no standard name.
                continue;
            }

            if (dbName == null)
            {
                LOG.warn("Column name for " + fieldName + " is null.");
                continue;
            }

            mFields.put(fieldName, table);
            mColumns.add(dbName);

            mFieldToColumn.put(fieldName, dbName);
            mColumnToField.put(dbName, fieldName);

            if (isLookup(table))
            {
                addLookups(fieldName, standardNames, table.getMLookup());
                mFieldTypes.put(fieldName, DmqlFieldType.LOOKUP);
            }
            else if (isLookupMulti(table))
            {
                addLookups(fieldName, standardNames, table.getMLookup());
                mFieldTypes.put(fieldName, DmqlFieldType.LOOKUP_MULTI);
            }
            else if (isBoolean(table))
            {
                mBooleans.add(fieldName);
                mFieldTypes.put(fieldName, DmqlFieldType.BOOLEAN);
            }
            else if (isNumeric(table))
            {
                mFieldTypes.put(fieldName, DmqlFieldType.NUMERIC);
            }
            else if (isTemporal(table))
            {
                mFieldTypes.put(fieldName, DmqlFieldType.TEMPORAL);
            }
            else
            {
                mFieldTypes.put(fieldName, DmqlFieldType.CHARACTER);
            }
        }

        Collections.sort(mColumns);
    }

    private boolean isLookup(MTable table)
    {
        InterpretationEnum interpretation = MetadataUtils.getInterpretationEnum(table);
        return ((interpretation != null) &&
                (interpretation.equals(InterpretationEnum.LOOKUP)));
    }

    private boolean isLookupMulti(MTable table)
    {
        InterpretationEnum interpretation = MetadataUtils.getInterpretationEnum(table);
        if ((interpretation != null) &&
                (interpretation.equals(InterpretationEnum.LOOKUPMULTI)))
        {
            if (table.getMLookup() == null)
            {
                LOG.error("Lookup is null for table: " + table.getPath());
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean isBoolean(MTable table)
    {
        DataTypeEnum type = MetadataUtils.getDataTypeEnum(table);
        if (type.equals(DataTypeEnum.BOOLEAN))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    private boolean isNumeric(MTable table)
    {
        DataTypeEnum type = MetadataUtils.getDataTypeEnum(table);
        if (type.equals(DataTypeEnum.TINY) ||
            type.equals(DataTypeEnum.SMALL) ||
            type.equals(DataTypeEnum.INT) ||
            type.equals(DataTypeEnum.LONG) ||
            type.equals(DataTypeEnum.DECIMAL))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isTemporal(MTable table)
    {
        DataTypeEnum type = MetadataUtils.getDataTypeEnum(table);
        if (type.equals(DataTypeEnum.DATE) ||
            type.equals(DataTypeEnum.DATETIME) ||
            type.equals(DataTypeEnum.TIME))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private String getTableName(MTable table, boolean standardNames)
    {
        String tableName;
        if (standardNames)
        {
            tableName = table.getStandardName();
        }
        else
        {
            tableName = table.getSystemName();
        }
        return tableName;
    }

    private void addLookups(String fieldName, boolean standardNames,
                            MLookup lookup)
    {
        if (lookup == null)
        {
            LOG.warn("Ignoring null lookup for field: " + fieldName);
            return;
        }

        boolean generateDbValues;
        Map<String, String> dbValues;
        if (fieldName.equals("ListingStatus") && standardNames)
        {
            dbValues = LISTING_STATUS_VALUES;
            generateDbValues = false;
        }
        else
        {
            dbValues = new LinkedHashMap<String, String>();
            generateDbValues = true;
        }

        Map<String, MLookupType> lookupTypesMap = new LinkedHashMap<String, MLookupType>();
        MLookupType[] lookupTypes = lookup.getMLookupTypes();
        Long saveId = Long.valueOf(-1);
        String saveLevel = null;
        for (MLookupType lookupType : lookupTypes)
        {
            if (lookupType.getUniqueId().longValue() > saveId.longValue()) {
                saveId = lookupType.getUniqueId();
            }
            if (saveLevel == null) {
                saveLevel = lookupType.getLevel();
            }
            String lookupValue = lookupType.getValue();
            lookupTypesMap.put(lookupValue, lookupType);
            if (generateDbValues)
            {
                // Assume lookups are stored in the database using the
                // lookup value.
                dbValues.put(lookupValue, lookupValue);
            }
        }
        /*
         * Dummy up an entry to handle .ANY.
         */
        MLookupType lookupType = new MLookupType();
        lookupType.setUniqueId(Long.valueOf(saveId.longValue() + 1));
        lookupType.setParent(lookup);
        lookupType.setLongValue(".ANY.");
        lookupType.setMetadataEntryID("ANY");
        lookupType.setShortValue(".ANY.");
        lookupType.setValue(".ANY.");
        dbValues.put(".ANY.", ".ANY.");
        lookupTypesMap.put(".ANY.", lookupType);
        
        /*
         * Dummy up an entry to handle .EMPTY.
         */
        lookupType = new MLookupType();
        lookupType.setUniqueId(Long.valueOf(saveId.longValue() + 2));
        lookupType.setParent(lookup);
        lookupType.setLongValue(".EMPTY.");
        lookupType.setMetadataEntryID("EMPTY");
        lookupType.setShortValue(".EMPTY.");
        lookupType.setValue(".EMPTY.");
        dbValues.put(".EMPTY.", ".EMPTY.");
        lookupTypesMap.put(".EMPTY.", lookupType);

        mLookupsDbMap.put(fieldName, dbValues);
        mLookupTypesMap.put(fieldName, lookupTypesMap);
    }

    public boolean isValidFieldName(String fieldName)
    {
        return mFields.containsKey(fieldName);
    }

    public DmqlFieldType getFieldType(String fieldName)
    {
        return mFieldTypes.get(fieldName);
    }

    public boolean isValidLookupValue(String lookupName, String lookupValue)
    {
        Map<String, String> values = mLookupsDbMap.get(lookupName);
        return values.containsKey(lookupValue);
    }

    public String fieldToColumn(String fieldName)
    {
        return mFieldToColumn.get(fieldName);
    }

    public String columnToField(String columnName)
    {
        return mColumnToField.get(columnName);
    }

    public List<String> getAllColumns()
    {
        return mColumns;
    }

    public String getLookupDbValue(String lookupName, String lookupValue)
    {
        Map<String, String> values = mLookupsDbMap.get(lookupName);
        if (values == null)
        {
            return null;
        }
        else
        {
            return values.get(lookupValue);
        }
    }

    public MTable getTable(String fieldName)
    {
        return mFields.get(fieldName);
    }

    public String getLookupShortValue(String lookupName, String value)
    {
        MLookupType lookupType = findLookupType(lookupName, value);
        if (lookupType == null)
        {
            return null;
        }
        return lookupType.getShortValue();
    }

    public String getLookupLongValue(String lookupName, String value)
    {
        MLookupType lookupType = findLookupType(lookupName, value);
        if (lookupType == null)
        {
            return null;
        }
        return lookupType.getLongValue();
    }

    private MLookupType findLookupType(String lookupName, String value)
    {
        Map<String, MLookupType> lookupTypes = mLookupTypesMap.get(lookupName);
        if (lookupTypes == null)
        {
            return null;
        }
        return lookupTypes.get(value);
    }

    private static final Logger LOG =
        Logger.getLogger(ServerDmqlMetadata.class);

    public static final boolean STANDARD = true;
    public static final boolean SYSTEM = false;
    private Set<String> mBooleans;
    private Map<String, MTable> mFields;
    private Map<String, DmqlFieldType> mFieldTypes;
    private Map<String, String> mFieldToColumn;
    private Map<String, String> mColumnToField;
    private Map<String, Map<String, String>> mLookupsDbMap;
    private Map<String, Map<String, MLookupType>> mLookupTypesMap;
    private static final Map<String, String> LISTING_STATUS_VALUES;
    private List<String> mColumns;

    static
    {
        LISTING_STATUS_VALUES = new HashMap<String, String>();
        LISTING_STATUS_VALUES.put("Active", "A");
        LISTING_STATUS_VALUES.put("Closed", "C");
        LISTING_STATUS_VALUES.put("Expired", "X");
        LISTING_STATUS_VALUES.put("OffMarket", "O");
        LISTING_STATUS_VALUES.put("Pending", "P");
    }
}
