package org.realtors.rets.server.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupRules
{
    public GroupRules(String groupName)
    {
        mGroupName = groupName;
        mFilterRules = new ArrayList();
        mReadOnlyFilterRules = Collections.unmodifiableList(mFilterRules);
        mConditionRules = new ArrayList();
        mReadOnlyConditionRules = Collections.unmodifiableList(mConditionRules);
    }

    public String getGroupName()
    {
        return mGroupName;
    }

    public List /* FilterRule */ getFilterRules()
    {
        return mReadOnlyFilterRules;
    }

    public void addFilterRule(FilterRule filterRule)
    {
        mFilterRules.add(filterRule);
    }

    public void removeFilterRule(FilterRule filterRule)
    {
        mFilterRules.remove(filterRule);
    }

    public List /* ConditionRule */ getConditionRules()
    {
        return mReadOnlyConditionRules;
    }

    public void addConditionRule(ConditionRule conditionRule)
    {
        mConditionRules.add(conditionRule);
    }

    public void removeConditionRule(ConditionRule conditionRule)
    {
        mConditionRules.remove(conditionRule);
    }

    private String mGroupName;
    private List mFilterRules;
    private List mReadOnlyFilterRules;
    private List mConditionRules;
    private List mReadOnlyConditionRules;
}
