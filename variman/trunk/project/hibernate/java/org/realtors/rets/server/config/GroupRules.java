package org.realtors.rets.server.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupRules
{
    public GroupRules(String groupName)
    {
        mGroupName = groupName;
        mRules = new ArrayList();
        mReadOnlyRules = Collections.unmodifiableList(mRules);
    }

    public String getGroupName()
    {
        return mGroupName;
    }

    public List /* RuleDescription */ getRules()
    {
        return mReadOnlyRules;
    }

    public void addRule(RuleDescription ruleDescription)
    {
        mRules.add(ruleDescription);
    }

    public void removeRule(RuleDescription ruleDescription)
    {
        mRules.remove(ruleDescription);
    }

    private String mGroupName;
    private List mRules;
    private List mReadOnlyRules;
}
