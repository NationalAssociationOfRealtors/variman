/*
 */
package org.realtors.rets.server.dmql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.realtors.rets.common.metadata.types.MTable;

public class SimpleDmqlMetadata implements DmqlParserMetadata
{
    public SimpleDmqlMetadata()
    {
        mFieldsToColumns = new HashMap();
        mColumnsToFields = new HashMap();
        mLookupShortValues = new HashMap();
        mLookupLongValues = new HashMap();
        mFieldTypes = new HashMap();
        mTables = new HashMap();
    }

    public void addLookup(String name, String[] values)
    {
        addLookup(name, DmqlFieldType.LOOKUP, values, values, values);
    }

    public void addLookup(String name, String[] values, String[] shortValues)
    {
        addLookup(name, DmqlFieldType.LOOKUP, values, shortValues,
                  shortValues);
    }
    
    public void addLookup(String name, String[] values, String[] shortValues,
                         String[] longValues)
    {
        addLookup(name, DmqlFieldType.LOOKUP, values, shortValues, longValues);
    }

    public void addLookupMulti(String name, String[] values)
    {
        addLookup(name, DmqlFieldType.LOOKUP_MULTI, values, values,
                  values);
    }

    public void addLookupMulti(String name, String[] values,
                               String[] shortValues)
    {
        addLookup(name, DmqlFieldType.LOOKUP_MULTI, values, shortValues,
                  shortValues);
    }

    private void addLookup(String name, DmqlFieldType fieldType,
                           String[] values, String[] shortValues,
                           String[] longValues)
    {
        Map shortValueMap = new HashMap();
        Map longValueMap = new HashMap();
        for (int i = 0; i < values.length; i++)
        {
            shortValueMap.put(values[i], shortValues[i]);
            longValueMap.put(values[i], longValues[i]);
        }
        mLookupShortValues.put(name, shortValueMap);
        mLookupLongValues.put(name, longValueMap);
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

    public void addTable(String fieldName, MTable table)
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

    public MTable getTable(String fieldName)
    {
        return mTables.get(fieldName);
    }

    public String getLookupLongValue(String lookupName, String value)
    {
        Map values = (Map) mLookupLongValues.get(lookupName);
        return (String) values.get(value);
    }

    public String getLookupShortValue(String lookupName, String value)
    {
        Map values = (Map) mLookupShortValues.get(lookupName);
        return (String) values.get(value);
    }

    private static final String PREFIX = "r_";
    private Map mLookupShortValues;
    private Map mLookupLongValues;
    private Map mFieldsToColumns;
    private Map mColumnsToFields;
    private Map<String, MTable> mTables;
    private Map mFieldTypes;
}
