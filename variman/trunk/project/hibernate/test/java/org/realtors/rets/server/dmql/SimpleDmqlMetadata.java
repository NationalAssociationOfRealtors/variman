/*
 */
package org.realtors.rets.server.dmql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.realtors.rets.server.metadata.Table;

public class SimpleDmqlMetadata implements DmqlParserMetadata
{
    public SimpleDmqlMetadata()
    {
        mFieldsToColumns = new HashMap();
        mColumnsToFields = new HashMap();
        mLookupShortValues = new HashMap();
        mStrings = new HashSet();
        mNumerics = new HashSet();
        mTables = new HashMap();
    }

    public void addLookup(String name, String[] values)
    {
        addLookup(name, values, values);
    }

    public void addLookup(String name, String[] values, String[] shortValues)
    {
        Map valueMap = new HashMap();
        for (int i = 0; i < values.length; i++)
        {
            valueMap.put(values[i], shortValues[i]);
        }
        addColumnMapping(name, PREFIX + name);
        mLookupShortValues.put(name, valueMap);
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
        return mLookupShortValues.containsKey(lookupName);
    }

    public boolean isValidLookupValue(String lookupName, String lookupValue)
    {
        Map values = (Map) mLookupShortValues.get(lookupName);
        return values.containsKey(lookupValue);
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

    public List getAllColumns()
    {
        return new ArrayList(mFieldsToColumns.keySet());
    }

    public Table getTable(String fieldName)
    {
        return (Table) mTables.get(fieldName);
    }

    public String getLookupLongValue(String lookupName, String value)
    {
        return "Long " + getLookupShortValue(lookupName, value);
    }

    public String getLookupShortValue(String lookupName, String value)
    {
        Map values = (Map) mLookupShortValues.get(lookupName);
        return (String) values.get(value);
    }

    private static final String PREFIX = "r_";
    private Map mLookupShortValues;
    private Set mStrings;
    private Map mFieldsToColumns;
    private Map mColumnsToFields;
    private Map mTables;
    private Set mNumerics;
}
