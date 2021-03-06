package org.realtors.rets.server.protocol;

import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.HashSet;

import org.apache.log4j.Logger;

import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.config.FilterRule;
import org.realtors.rets.server.Group;
import org.realtors.rets.server.metadata.Table;

public class TableGroupFilter
{
    public TableGroupFilter()
    {
        mAllTables = new HashMap();
        mRules = new HashMap();
    }

    public Set /* Table */ findTables(Group group, String resource,
                                      String retsClass)
    {
        String tableKey = resource + ":" + retsClass;
        String ruleKey = group.getName() + ":" + tableKey;
        Set filteredTables = (Set) mRules.get(ruleKey);
        if (filteredTables != null)
        {
            return filteredTables;
        }
        return (Set) mAllTables.get(tableKey);
    }

    public Set /* Table */ findTables(Set /* Group */ groups, String resource,
                                      String retsClass)
    {
        if (groups.isEmpty())
        {
            return (Set) mAllTables.get(resource + ":" + retsClass);
        }

        Set unionOfTables = new HashSet();
        for (Iterator iterator = groups.iterator(); iterator.hasNext();)
        {
            Group group =  (Group) iterator.next();
            unionOfTables.addAll(findTables(group, resource, retsClass));
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
            Set tables = (Set) mAllTables.get(tableKey);
            if (tables == null)
            {
                LOG.warn("No tables found for " + tableKey +
                         ".  Skipping rule " + filterRule);
                continue;
            }
            Set filteredTables = new HashSet();
            for (Iterator iterator = tables.iterator(); iterator.hasNext();)
            {
                Table table = (Table) iterator.next();
                if (filterRule.includeSystemName(table.getSystemName()))
                {
                    filteredTables.add(table);
                }
            }
            mRules.put(rulesKey, filteredTables);
        }
    }

    public void setTables(String resource, String retsClass,
                          Set /* Table */ tables)
    {
        String key = resource + ":" + retsClass;
        mAllTables.put(key, tables);
    }

    private static final Logger LOG =
        Logger.getLogger(TableGroupFilter.class);

    /** Contains all tables for a given resource and class. */
    private Map /* String, Set <Table> */ mAllTables;

    /**
     * Contains a filterd list of tables for a given resource, class, and
     * group.
     */
    private Map /* String, Set <Table> */ mRules;
}
