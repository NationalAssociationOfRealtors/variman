package org.realtors.rets.server.config;

import java.util.List;

public class SecurityConstraints
{
    public void setAllConstraints(List constraints)
    {
        mGroupRules = constraints;
    }

    public List getAllConstraints()
    {
        return mGroupRules;
    }

    public GroupRules getRulesAt(int index)
    {
        return (GroupRules) mGroupRules.get(index);
    }

    private List mGroupRules;
}
