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
        mConditionRules = new ArrayList();
        mReadOnlyConditionRules = Collections.unmodifiableList(mConditionRules);
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

    public List /* ConditionRule */ getConditionRules()
    {
        return mReadOnlyConditionRules;
    }

    public void addConditionRule(ConditionRule conditionRule)
    {
        mConditionRules.add(conditionRule);
    }

    private String mGroupName;
    private List mRules;
    private List mReadOnlyRules;
    private List mConditionRules;
    private List mReadOnlyConditionRules;
}
