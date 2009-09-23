package org.realtors.rets.server.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.realtors.rets.server.QueryCount;

public class GroupRules
{
    public GroupRules(String groupName)
    {
        mGroupName = groupName;
        mFilterRules = new ArrayList();
        mReadOnlyFilterRules = Collections.unmodifiableList(mFilterRules);
        mConditionRules = new ArrayList();
        mReadOnlyConditionRules = Collections.unmodifiableList(mConditionRules);
        setNoQueryCountLimit();
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

    public int getRecordLimit()
    {
        return mRecordLimit;
    }

    public void setRecordLimit(int recordLimit)
    {
        mRecordLimit = recordLimit;
    }

    public TimeRestriction getTimeRestriction()
    {
        return mTimeRestriction;
    }

    public void setTimeRestriction(TimeRestriction timeRestriction)
    {
        mTimeRestriction = timeRestriction;
    }

    public void setQueryCountLimit(long limit, QueryCount.LimitPeriod limitPeriod)
    {
        if (limit <= 0)
        {
            throw new IllegalArgumentException("limit must be greater than 0: "
                                               + limit);
        }
        if (limitPeriod == null)
        {
            throw new IllegalArgumentException("limitPeriod must not be null");
        }

        mQueryCountLimit = limit;
        mQueryCountLimitPeriod = limitPeriod;
    }

    public void setNoQueryCountLimit()
    {
        mQueryCountLimit = 0;
        mQueryCountLimitPeriod = null;
    }

    public boolean hasNoQueryLimit()
    {
        return (mQueryCountLimitPeriod == null);
    }

    public long getQueryCountLimit()
    {
        return mQueryCountLimit;
    }

    public QueryCount.LimitPeriod getQueryCountLimitPeriod()
    {
        return mQueryCountLimitPeriod;
    }

    private String mGroupName;
    private List mFilterRules;
    private List mReadOnlyFilterRules;
    private List mConditionRules;
    private List mReadOnlyConditionRules;
    private int mRecordLimit;
    private TimeRestriction mTimeRestriction;
    private long mQueryCountLimit;
    private QueryCount.LimitPeriod mQueryCountLimitPeriod;
}
