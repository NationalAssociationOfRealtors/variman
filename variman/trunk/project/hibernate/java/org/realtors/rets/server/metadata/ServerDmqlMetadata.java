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
        mLookups = new HashMap();
        mStrings = new HashSet();
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
            mFields.put(fieldName, table.getDbName());

            Lookup lookup = table.getLookup();
            if (lookup != null)
            {
                addLookups(fieldName, standardNames, lookup);
            }
            else
            {
                mStrings.add(fieldName);
            }
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
            values = sListingStatusValues;
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
        Map values = (Map) mLookups.get(lookupName);
        return values.containsKey(lookupValue);
    }

    public String getFieldDbColumn(String fieldName)
    {
        return (String) mFields.get(fieldName);
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

    public static final boolean STANDARD = true;
    public static final boolean SYSTEM = false;
    private Map mFields;
    private Map mLookups;
    private Set mStrings;
    private static final Map sListingStatusValues;

    static
    {
        sListingStatusValues = new HashMap();
        sListingStatusValues.put("Active", "A");
        sListingStatusValues.put("Closed", "C");
        sListingStatusValues.put("Expired", "X");
        sListingStatusValues.put("OffMarket", "O");
        sListingStatusValues.put("Pending", "P");
    }
}
