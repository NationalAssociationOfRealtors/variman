package org.realtors.rets.server.protocol;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import org.apache.log4j.Logger;

import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.config.FilterRule;
import org.realtors.rets.server.Group;

public class TableGroupFilter
{
    public TableGroupFilter()
    {
        mAllTables = new LinkedHashMap<String, Set<MTable>>();
        mRules = new LinkedHashMap<String, Set<MTable>>();
    }

    public Set<MTable> findTables(Group group, String resource,
                                      String retsClass)
    {
        String tableKey = resource + ":" + retsClass;
        String ruleKey = group.getName() + ":" + tableKey;
        Set<MTable> filteredTables = mRules.get(ruleKey);
        if (filteredTables != null)
        {
            return filteredTables;
        }
        return mAllTables.get(tableKey);
    }

    public Set<MTable> findTables(Set<Group> groups, String resource,
                                      String retsClass)
    {
        if (groups.isEmpty())
        {
            return mAllTables.get(resource + ":" + retsClass);
        }

        Set<MTable> unionOfTables = new LinkedHashSet<MTable>();
        for (Iterator<Group> iterator = groups.iterator(); iterator.hasNext();)
        {
            Group group =  iterator.next();
            Set<MTable> tables = findTables(group, resource, retsClass);
            if (tables != null) {
                unionOfTables.addAll(tables);
            }
        }
        return unionOfTables;
    }

    public void addRules(GroupRules rules)
    {
        List filterRules = rules.getFilterRules();
        String groupName = rules.getGroupName();
        for (int i = 0; i < filterRules.size(); i++)
        {
            FilterRule filterRule = (FilterRule) filterRules.get(i);
            String tableKey = filterRule.getResource() + ":" +
                              filterRule.getRetsClass();
            String rulesKey = groupName + ":" + tableKey;
            Set<MTable> tables = mAllTables.get(tableKey);
            if (tables == null)
            {
                LOG.warn("No tables found for " + tableKey +
                         ".  Skipping rule " + filterRule);
                continue;
            }
            Set<MTable> filteredTables = new LinkedHashSet<MTable>();
            for (Iterator<MTable> iterator = tables.iterator(); iterator.hasNext();)
            {
                MTable table = iterator.next();
                if (filterRule.includeSystemName(table.getSystemName()))
                {
                    filteredTables.add(table);
                }
            }
            mRules.put(rulesKey, filteredTables);
        }
    }

    public void setTables(String resource, String retsClass,
                          Set<MTable> tables)
    {
        String key = resource + ":" + retsClass;
        mAllTables.put(key, tables);
    }

    private static final Logger LOG =
        Logger.getLogger(TableGroupFilter.class);

    /** Contains all tables for a given resource and class. */
    private Map<String, Set<MTable>> mAllTables;

    /**
     * Contains a filterd list of tables for a given resource, class, and
     * group.
     */
    private Map<String, Set<MTable>> mRules;
}
