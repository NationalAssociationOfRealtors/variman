/*
 */
package org.realtors.rets.server.dmql;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.realtors.rets.server.metadata.Table;

public class SimpleDmqlMetadata implements DmqlParserMetadata
{
    public SimpleDmqlMetadata()
    {
        mFieldsToColumns = new HashMap();
        mColumnsToFields = new HashMap();
        mLookups = new HashMap();
        mStrings = new HashSet();
        mTables = new HashMap();
    }

    public void addLookup(String name, String[] values)
    {
        Set valueSet = new HashSet();
        for (int i = 0; i < values.length; i++)
        {
            valueSet.add(values[i]);
        }
        mFieldsToColumns.put(name, PREFIX + name);
        mColumnsToFields.put(PREFIX + name, name);
        mLookups.put(name, valueSet);
    }

    public void addString(String fieldName)
    {
        addString(fieldName, PREFIX + fieldName);
    }

    public void addString(String fieldName, String columnName)
    {
        mFieldsToColumns.put(fieldName, columnName);
        mColumnsToFields.put(columnName, fieldName);
        mStrings.add(fieldName);
    }

    public void addTable(String fieldName, Table table)
    {
        mTables.put(fieldName, table);
    }

    public boolean isValidFieldName(String fieldName)
    {
        return mFieldsToColumns.containsKey(fieldName);
    }

    public boolean isValidStringName(String fieldName)
    {
        return mStrings.contains(fieldName);
    }

    public boolean isValidLookupName(String lookupName)
    {
        return mLookups.containsKey(lookupName);
    }

    public boolean isValidLookupValue(String lookupName, String lookupValue)
    {
        Set values = (Set) mLookups.get(lookupName);
        return values.contains(lookupValue);
    }

    public String fieldToColumn(String fieldName)
    {
        return (String) mFieldsToColumns.get(fieldName);
    }

    public String columnToField(String columnName)
    {
        return (String) mColumnsToFields.get(columnName);
    }

    public String getLookupDbValue(String lookupName, String lookupValue)
    {
        return isValidLookupValue(lookupName, lookupValue) ?
            lookupValue : null;
    }

    public Collection getAllColumns()
    {
        return mFieldsToColumns.keySet();
    }

    public Table getTable(String fieldName)
    {
        return (Table) mTables.get(fieldName);
    }

    private static final String PREFIX = "r_";
    private Map mLookups;
    private Set mStrings;
    private Map mFieldsToColumns;
    private Map mColumnsToFields;
    private Map mTables;
}
