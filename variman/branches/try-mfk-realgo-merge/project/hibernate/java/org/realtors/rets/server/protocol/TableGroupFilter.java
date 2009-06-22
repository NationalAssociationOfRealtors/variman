package org.realtors.rets.server.protocol;

import java.util.Collections;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.HashSet;

import net.sf.hibernate.HibernateException;

import org.apache.log4j.Logger;

import org.realtors.rets.server.config.FilterRuleUtils;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.config.FilterRule;
import org.realtors.rets.server.Group;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.User;
import org.realtors.rets.server.UserAgent;
import org.realtors.rets.server.UserAgentUtils;
import org.realtors.rets.server.UserUtils;
import org.realtors.rets.server.metadata.Table;

public class TableGroupFilter
{
    public TableGroupFilter()
    {
        mAllTables = new HashMap/*String, Set<Table>*/();
        mRules = new HashMap/*String, Set<Table>*/();
    }
    
    public Set/*Table*/ findTables(User user, UserAgent userAgent, String resourceId, String className) throws RetsServerException
    {
        Set/*Table*/ userFilteredTables = new HashSet/*Table*/();
        Set/*Table*/ userAgentFilteredTables = new HashSet/*Table*/();
        try {
            Set/*Group*/ userGroups = UserUtils.getGroups(user);
            Set/*Group*/ userAgentGroups = Collections.EMPTY_SET; // TODO: Use the following instead: UserAgentUtils.getGroups(userAgent);
            userFilteredTables = findTables(userGroups, resourceId, className);
            userAgentFilteredTables = findTables(userAgentGroups, resourceId, className);
            userFilteredTables.retainAll(userAgentFilteredTables); // Takes the intersection of the two sets. userFilteredTables gets modified in place.
        } catch (HibernateException e) {
            throw new RetsServerException(e);
        }
        return userFilteredTables;
    }
    
    public Set/*Table*/ findTables(User user, String resourceId, String className) throws RetsServerException
    {
        Set/*Table*/ filteredTables = new HashSet/*Table*/();
        try {
            Set/*Group*/ groups = UserUtils.getGroups(user);
            filteredTables = findTables(groups, resourceId, className);
        } catch (HibernateException e) {
            throw new RetsServerException(e);
        }
        return filteredTables;
    }

    public Set/*Table*/ findTables(Group group, String resourceId, String className)
    {
        String tableKey = resourceId + ":" + className;
        String ruleKey = group.getName() + ":" + tableKey;
        Set/*Table*/ filteredTables = (Set/*Table*/) mRules.get(ruleKey);
        if (filteredTables == null) {
            filteredTables = Collections.EMPTY_SET;
        }
        return filteredTables;
    }

    public Set/*Table*/ findTables(Set/*Group*/ groups, String resourceId, String className)
    {
        Set/*Table*/ unionOfTables = new HashSet/*Table*/();
        for (Iterator/*Group*/ iterator = groups.iterator(); iterator.hasNext();)
        {
            Group group = (Group) iterator.next();
            unionOfTables.addAll(findTables(group, resourceId, className));
        }
        return unionOfTables;
    }

    public void addRules(GroupRules groupRules)
    {
        List/*FilterRule*/ filterRules = groupRules.getFilterRules();
        String groupName = groupRules.getGroup().getName();
        for (int i = 0; i < filterRules.size(); i++)
        {
            FilterRule filterRule = (FilterRule) filterRules.get(i);
            String tableKey = filterRule.getResourceID() + ":" + filterRule.getRetsClassName();
            String groupRulesKey = groupName + ":" + tableKey;
            Set/*Table*/ tables = (Set/*Table*/) mAllTables.get(tableKey);
            if (tables == null)
            {
                LOG.warn("No tables found for " + tableKey +
                         ".  Skipping rule " + filterRule);
                continue;
            }
            Set/*Table*/ filteredTables = new HashSet/*Table*/();
            for (Iterator/*Table*/ iterator = tables.iterator(); iterator.hasNext();)
            {
                Table table = (Table) iterator.next();
                if (FilterRuleUtils.includeSystemName(filterRule, table.getSystemName()))
                {
                    filteredTables.add(table);
                }
            }
            mRules.put(groupRulesKey, filteredTables);
        }
    }

    public void setTables(String resourceId, String className, Set/*Table*/ tables)
    {
        String key = resourceId + ":" + className;
        mAllTables.put(key, tables);
    }

    private static final Logger LOG = Logger.getLogger(TableGroupFilter.class);

    /* Contains all tables for a given resource ID and class. */
    private Map/*String, Set<Table>*/ mAllTables;

    /*
     * Contains a filterd list of tables for a given resource ID, class, and
     * group.
     */
    private Map/*String, Set<Table>*/ mRules;
}
