/*
 */
package org.realtors.rets.server.metadata;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Collection;

import org.realtors.rets.server.dmql.DmqlParserMetadata;

public class ServerDmqlMetadata implements DmqlParserMetadata
{
    private ServerDmqlMetadata()
    {
        mFields = new HashSet();
        mLookups = new HashMap();
        mStrings = new HashSet();
    }

    public ServerDmqlMetadata(MClass clazz, boolean standardNames)
    {
        this();
        init(clazz.getTables(), standardNames);
    }

    public ServerDmqlMetadata(Collection tables, boolean standaredNames)
    {
        this();
        init(tables, standaredNames);
    }

    private void init(Collection tables, boolean standardNames)
    {
        for (Iterator i = tables.iterator(); i.hasNext();)
        {
            Table table = (Table) i.next();
            String fieldName = getTableName(table,  standardNames);
            mFields.add(fieldName);

            Lookup lookup = table.getLookup();
            if (lookup != null)
            {
                Set values = new HashSet();
                Set lookupTypes = lookup.getLookupTypes();
                for (Iterator j = lookupTypes.iterator(); j.hasNext();)
                {
                    LookupType lookupType = (LookupType) j.next();
                    values.add(lookupType.getValue());
                }
                mLookups.put(fieldName, values);
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

    public boolean isValidFieldName(String fieldName)
    {
        return mFields.contains(fieldName);
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

    public static final boolean STANDARD = true;
    public static final boolean SYSTEM = false;
    private Set mFields;
    private Map mLookups;
    private Set mStrings;
}
