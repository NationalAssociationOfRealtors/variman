/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.metadata;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.realtors.rets.server.dmql.DmqlFieldType;
import org.realtors.rets.server.dmql.DmqlParserMetadata;

public class ServerDmqlMetadata implements DmqlParserMetadata
{
    private ServerDmqlMetadata()
    {
        mFields = new HashMap();
        mFieldTypes = new HashMap();
        mFieldToColumn = new HashMap();
        mColumnToField = new HashMap();
        mLookupsDbMap = new HashMap();
        mLookupTypesMap = new HashMap();
        mStrings = new HashSet();
        mNumerics = new HashSet();
    }

    public ServerDmqlMetadata(MClass clazz, boolean standardNames)
    {
        this();
        init(clazz.getTables(), standardNames);
    }

    public ServerDmqlMetadata(Collection tables, boolean standardNames)
    {
        this();
        init(tables, standardNames);
    }

    private void init(Collection tables, boolean standardNames)
    {
        DefaultColumnList defaultColumnList = new DefaultColumnList();
        for (Iterator i = tables.iterator(); i.hasNext();)
        {
            Table table = (Table) i.next();
            String dbName = table.getDbName();
            int defaultOrder = table.getDefault();
            // Skip all that have default set to -1
            if (defaultOrder == -1)
            {
                continue;
            }

            String fieldName = getTableName(table,  standardNames);
            if (fieldName == null)
            {
                // Skip tables that have no field name.  Technically, this
                // should only happen for tables that have no standard name.
                continue;
            }

            mFields.put(fieldName, table);
            if (defaultOrder > 0)
            {
                defaultColumnList.add(defaultOrder, dbName);
            }

            if (table.getInterpretation() != InterpretationEnum.LOOKUPMULTI)
            {
                mFieldToColumn.put(fieldName, dbName);
                mColumnToField.put(dbName, fieldName);
            }

            Lookup lookup = table.getLookup();
            if (lookup != null)
            {
                addLookups(fieldName, standardNames, lookup);
                mFieldTypes.put(fieldName, DmqlFieldType.LOOKUP);
            }
            else if (isNumeric(table))
            {
                mNumerics.add(fieldName);
                mFieldTypes.put(fieldName, DmqlFieldType.NUMERIC);
            }
            else if (isTemporal(table))
            {
                mFieldTypes.put(fieldName, DmqlFieldType.TEMPORAL);
            }
            else
            {
                mStrings.add(fieldName);
                mFieldTypes.put(fieldName, DmqlFieldType.CHARACTER);
            }
        }
        mDefaultColumnNames = defaultColumnList.getColumnNames();
    }

    private boolean isNumeric(Table table)
    {
        DataTypeEnum type = table.getDataType();
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

    private boolean isTemporal(Table table)
    {
        DataTypeEnum type = table.getDataType();
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

    private String getTableName(Table table, boolean standardNames)
    {
        if (standardNames)
        {
            TableStandardName standardName = table.getStandardName();
            if (standardName != null)
            {
                return standardName.getName();
            }
            else
            {
                return null;
            }
        }
        else
        {
            return table.getSystemName();
        }
    }

    private void addLookups(String fieldName, boolean standardNames,
                            Lookup lookup)
    {
        boolean generateDbValues;
        Map dbValues;
        if (fieldName.equals("ListingStatus") && standardNames)
        {
            dbValues = LISTING_STATUS_VALUES;
            generateDbValues = false;
        }
        else
        {
            dbValues = new HashMap();
            generateDbValues = true;
        }

        HashMap lookupTypesMap = new HashMap();
        Set lookupTypes = lookup.getLookupTypes();
        for (Iterator iterator = lookupTypes.iterator(); iterator.hasNext();)
        {
            LookupType lookupType = (LookupType) iterator.next();
            String lookupValue = lookupType.getValue();
            lookupTypesMap.put(lookupValue, lookupType);
            if (generateDbValues)
            {
                // Assume lookups are stored in the database using the
                // lookup value.
                dbValues.put(lookupValue, lookupValue);
            }
        }

        mLookupsDbMap.put(fieldName, dbValues);
        mLookupTypesMap.put(fieldName, lookupTypesMap);
    }

    public boolean isValidFieldName(String fieldName)
    {
        return mFields.containsKey(fieldName);
    }

    public DmqlFieldType getFieldType(String fieldName)
    {
        return (DmqlFieldType) mFieldTypes.get(fieldName);
    }

    public boolean isValidLookupValue(String lookupName, String lookupValue)
    {
        Map values = (Map) mLookupsDbMap.get(lookupName);
        return values.containsKey(lookupValue);
    }

    public String fieldToColumn(String fieldName)
    {
        return (String) mFieldToColumn.get(fieldName);
    }

    public String columnToField(String columnName)
    {
        return (String) mColumnToField.get(columnName);
    }

    public List getAllColumns()
    {
        return mDefaultColumnNames;
    }

    public String getLookupDbValue(String lookupName, String lookupValue)
    {
        Map values = (Map) mLookupsDbMap.get(lookupName);
        if (values == null)
        {
            return null;
        }
        else
        {
            return (String) values.get(lookupValue);
        }
    }

    public Table getTable(String fieldName)
    {
        return (Table) mFields.get(fieldName);
    }

    public String getLookupShortValue(String lookupName, String value)
    {
        LookupType lookupType = findLookupType(lookupName, value);
        if (lookupType == null)
        {
            return null;
        }
        return lookupType.getShortValue();
    }

    public String getLookupLongValue(String lookupName, String value)
    {
        LookupType lookupType = findLookupType(lookupName, value);
        if (lookupType == null)
        {
            return null;
        }
        return lookupType.getLongValue();
    }

    private LookupType findLookupType(String lookupName, String value)
    {
        Map lookupTypes = (Map) mLookupTypesMap.get(lookupName);
        if (lookupTypes == null)
        {
            return null;
        }
        return (LookupType) lookupTypes.get(value);
    }

    public static final boolean STANDARD = true;
    public static final boolean SYSTEM = false;
    private Map mFields;
    private Map mFieldTypes;
    private Map mFieldToColumn;
    private Map mColumnToField;
    private Map mLookupsDbMap;
    private Map mLookupTypesMap;
    private Set mStrings;
    private static final Map LISTING_STATUS_VALUES;
    private Set mNumerics;
    private List mDefaultColumnNames;

    static
    {
        LISTING_STATUS_VALUES = new HashMap();
        LISTING_STATUS_VALUES.put("Active", "A");
        LISTING_STATUS_VALUES.put("Closed", "C");
        LISTING_STATUS_VALUES.put("Expired", "X");
        LISTING_STATUS_VALUES.put("OffMarket", "O");
        LISTING_STATUS_VALUES.put("Pending", "P");
    }
}
