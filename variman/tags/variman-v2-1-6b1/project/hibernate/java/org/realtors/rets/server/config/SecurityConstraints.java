package org.realtors.rets.server.config;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

    private List mGroupRules;
    private Map mGroupRulesByName;
}
