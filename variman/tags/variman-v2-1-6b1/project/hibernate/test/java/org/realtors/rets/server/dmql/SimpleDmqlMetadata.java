/*
 */
package org.realtors.rets.server.dmql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.realtors.rets.server.metadata.Table;

public class SimpleDmqlMetadata implements DmqlParserMetadata
{
    public SimpleDmqlMetadata()
    {
        mFieldsToColumns = new HashMap();
        mColumnsToFields = new HashMap();
        mLookupShortValues = new HashMap();
        mFieldTypes = new HashMap();
        mTables = new HashMap();
    }

    public void addLookup(String name, String[] values)
    {
        addLookup(name, DmqlFieldType.LOOKUP, values, values);
    }

    public void addLookup(String name, String[] values, String[] shortValues)
    {
        addLookup(name, DmqlFieldType.LOOKUP, values, shortValues);
    }

    public void addLookupMulti(String name, String[] values)
    {
        addLookup(name, DmqlFieldType.LOOKUP_MULTI, values, values);
    }

    public void addLookupMulti(String name, String[] values,
                               String[] shortValues)
    {
        addLookup(name, DmqlFieldType.LOOKUP_MULTI, values, shortValues);
    }

    private void addLookup(String name, DmqlFieldType fieldType,
                           String[] values, String[] shortValues)
    {
        Map valueMap = new HashMap();
        for (int i = 0; i < values.length; i++)
        {
            valueMap.put(values[i], shortValues[i]);
        }
        mLookupShortValues.put(name, valueMap);
        addType(name, PREFIX + name, fieldType);
    }

    public void addString(String fieldName)
    {
        addString(fieldName, PREFIX + fieldName);
    }

    public void addString(String fieldName, String columnName)
    {
        addType(fieldName, columnName, DmqlFieldType.CHARACTER);
    }

    public void addNumeric(String fieldName)
    {
        addNumeric(fieldName, PREFIX + fieldName);
    }

    public void addNumeric(String fieldName, String columnName)
    {
        addType(fieldName, columnName, DmqlFieldType.NUMERIC);
    }

    public void addTemporal(String fieldName)
    {
        addTemporal(fieldName, PREFIX + fieldName);
    }

    public void addTemporal(String fieldName, String columnName)
    {
        addType(fieldName, columnName, DmqlFieldType.TEMPORAL);
    }

    private void addType(String fieldName, String columnName,
                         DmqlFieldType fieldType)
    {
        addColumnMapping(fieldName, columnName);
        mFieldTypes.put(fieldName, fieldType);
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

    public DmqlFieldType getFieldType(String fieldName)
    {
        return (DmqlFieldType) mFieldTypes.get(fieldName);
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
    private Map mFieldsToColumns;
    private Map mColumnsToFields;
    private Map mTables;
    private Map mFieldTypes;
}
