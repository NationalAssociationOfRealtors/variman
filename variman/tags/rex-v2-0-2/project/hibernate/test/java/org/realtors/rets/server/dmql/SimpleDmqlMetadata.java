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
        mNumerics = new HashSet();
        mTables = new HashMap();
    }

    public void addLookup(String name, String[] values)
    {
        Set valueSet = new HashSet();
        for (int i = 0; i < values.length; i++)
        {
            valueSet.add(values[i]);
        }
        addColumnMapping(name, PREFIX + name);
        mLookups.put(name, valueSet);
    }

    public void addString(String fieldName)
    {
        addString(fieldName, PREFIX + fieldName);
    }

    public void addString(String fieldName, String columnName)
    {
        addColumnMapping(fieldName,  columnName);
        mStrings.add(fieldName);
    }

    public void addNumeric(String fieldName)
    {
        addNumeric(fieldName, PREFIX + fieldName);
    }

    public void addNumeric(String fieldName, String columnName)
    {
        addColumnMapping(fieldName, columnName);
        mNumerics.add(fieldName);
    }

    private void addColumnMapping(String fieldName, String columnName)
    {
        mFieldsToColumns.put(fieldName, columnName);
        mColumnsToFields.put(columnName,  fieldName);
    }

    public void addTable(String fieldName, Table table)
    {
        mTables.put(fieldName, table);
    }

    public boolean isValidFieldName(String fieldName)
    {
        return mFieldsToColumns.containsKey(fieldName);
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
    private Set mNumerics;
}
