package org.realtors.rets.server.config;

import java.util.List;

public class SecurityConstraints
{
    public void setAllConstraints(List constraints)
    {
        mGroupRules = constraints;
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
        for (int i = 0; i < mGroupRules.size(); i++)
        {
            GroupRules groupRules = (GroupRules) mGroupRules.get(i);
            if (groupRules.getGroupName().equals(groupName))
            {
                return groupRules;
            }
        }

        GroupRules groupRules = new GroupRules(groupName);
        mGroupRules.add(groupRules);
        return groupRules;
    }

    private List mGroupRules;
}
