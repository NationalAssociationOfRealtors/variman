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
import java.util.Map;
import java.util.Set;

import org.realtors.rets.server.dmql.DmqlParserMetadata;

public class ServerDmqlMetadata implements DmqlParserMetadata
{
    private ServerDmqlMetadata()
    {
        mFields = new HashMap();
        mFieldToColumn = new HashMap();
        mColumnToField = new HashMap();
        mLookups = new HashMap();
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
        for (Iterator i = tables.iterator(); i.hasNext();)
        {
            Table table = (Table) i.next();
            String fieldName = getTableName(table,  standardNames);
            mFields.put(fieldName, table);

            if (table.getInterpretation() != InterpretationEnum.LOOKUPMULTI)
            {
                mFieldToColumn.put(fieldName, table.getDbName());
                mColumnToField.put(table.getDbName(), fieldName);
            }

            Lookup lookup = table.getLookup();
            if (lookup != null)
            {
                addLookups(fieldName, standardNames, lookup);
            }
            else if (isNumeric(table))
            {
                mNumerics.add(fieldName);
            }
            else
            {
                mStrings.add(fieldName);
            }
        }
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

    private String getTableName(Table table, boolean standardNames)
    {
        if (standardNames)
        {
            return table.getStandardName().getName();
        }
        else
        {
            return table.getSystemName();
        }
    }

    private void addLookups(String fieldName, boolean standardNames,
                            Lookup lookup)
    {
        Map values;
        if (fieldName.equals("ListingStatus") && standardNames)
        {
            values = LISTING_STATUS_VALUES;
        }
        else
        {
            values = getLookupValues(lookup);
        }
        mLookups.put(fieldName, values);
    }

    private Map getLookupValues(Lookup lookup)
    {
        Map values = new HashMap();
        Set lookupTypes = lookup.getLookupTypes();
        for (Iterator j = lookupTypes.iterator(); j.hasNext();)
        {
            LookupType lookupType = (LookupType) j.next();
            values.put(lookupType.getValue(), lookupType.getValue());
        }
        return values;
    }

    public boolean isValidFieldName(String fieldName)
    {
        return mFields.containsKey(fieldName);
    }

    public boolean isCharacterField(String fieldName)
    {
        return mStrings.contains(fieldName);
    }

    public boolean isNumericField(String fieldName)
    {
        return mNumerics.contains(fieldName);
    }

    public boolean isLookupField(String lookupName)
    {
        return mLookups.containsKey(lookupName);
    }

    public boolean isValidLookupValue(String lookupName, String lookupValue)
    {
        Map values = (Map) mLookups.get(lookupName);
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

    public Collection getAllColumns()
    {
        return mFieldToColumn.values();
    }

    public String getLookupDbValue(String lookupName, String lookupValue)
    {
        Map values = (Map) mLookups.get(lookupName);
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

    public static final boolean STANDARD = true;
    public static final boolean SYSTEM = false;
    private Map mFields;
    private Map mFieldToColumn;
    private Map mColumnToField;
    private Map mLookups;
    private Set mStrings;
    private static final Map LISTING_STATUS_VALUES;
    private Set mNumerics;

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
