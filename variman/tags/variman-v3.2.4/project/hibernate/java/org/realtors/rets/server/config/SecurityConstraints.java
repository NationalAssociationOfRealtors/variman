package org.realtors.rets.server.config;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.realtors.rets.server.Group;

/**
 * Represents the entire security constraint configuration.  
 * This includes the filter and condition rules for all known groups.
 */
public class SecurityConstraints
{
    public SecurityConstraints()
    {
        mGroupRulesByName = new HashMap();
    }

    public void setAllConstraints(List constraints)
    {
        mGroupRules = constraints;
        mGroupRulesByName.clear();
        for (int i = 0; i < mGroupRules.size(); i++)
        {
            GroupRules groupRules = (GroupRules) mGroupRules.get(i);
            mGroupRulesByName.put(groupRules.getGroupName(), groupRules);
        }
    }

    public List getAllGroupRules()
    {
        return mGroupRules;
    }

    public GroupRules getGroupRulesAt(int index)
    {
        return (GroupRules) mGroupRules.get(index);
    }

    public GroupRules getRulesForGroup(String groupName)
    {
        GroupRules groupRules = (GroupRules) mGroupRulesByName.get(groupName);
        if (groupRules == null)
        {
            groupRules = new GroupRules(groupName);
            mGroupRules.add(groupRules);
            mGroupRulesByName.put(groupName, groupRules);
        }
        return groupRules;
    }

    public void removeRulesForGroup(String groupName)
    {
        GroupRules groupRules = (GroupRules) mGroupRulesByName.get(groupName);
        if (groupRules != null)
        {
            mGroupRules.remove(groupRules);
            mGroupRulesByName.remove(groupName);
        }
    }

    public List getAllRulesForGroups(Collection groups)
    {
        List allRules = new ArrayList();
        for (Iterator iterator = groups.iterator(); iterator.hasNext();)
        {
            Group group = (Group) iterator.next();
            allRules.add(getRulesForGroup(group.getName()));
        }
        return allRules;
    }

    private List mGroupRules;
    private Map mGroupRulesByName;
}
