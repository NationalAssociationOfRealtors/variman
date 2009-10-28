package org.realtors.rets.server.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.realtors.rets.server.QueryCount;

public class GroupRules
{
    public GroupRules() {
        this(null);
    }

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

    public void setGroupName(String groupName)
    {
        mGroupName = groupName;
    }

    public List /* FilterRule */ getFilterRules()
    {
        return mReadOnlyFilterRules;
    }

    public void setFilterRules(List filterRules)
    {
        Iterator itr = filterRules.iterator();
        while( itr.hasNext() )
        {
            FilterRule filterRule = (FilterRule)itr.next();
            addFilterRule(filterRule);
        }
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

    public void setConditionRules(List conditionRules)
    {
        Iterator itr = conditionRules.iterator();
        while( itr.hasNext() )
        {
            ConditionRule conditionRule = (ConditionRule)itr.next();
            addConditionRule(conditionRule);
        }
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

    public void setQueryCountLimit(long limit)
    {
        if (limit <= 0)
        {
            throw new IllegalArgumentException("limit must be greater than 0: "
                                               + limit);
        }

        mQueryCountLimit = limit;
    }

    public QueryCount.LimitPeriod getQueryCountLimitPeriod()
    {
        return mQueryCountLimitPeriod;
    }

    public void setQueryCountLimitPeriod(QueryCount.LimitPeriod limitPeriod)
    {
        mQueryCountLimitPeriod = limitPeriod;
    }

    public Integer getId() {
        return this.mId;
    }

    public void setId(Integer id) {
        this.mId = id;
    }

    public Map getExtendableProperties()
    {
        if (mExtendableProperties == null) {
            mExtendableProperties = new LinkedHashMap();
        }
       return mExtendableProperties;
    }

    public void setExtendableProperties(Map extendableProperties) {
           this.mExtendableProperties = extendableProperties;
    }

    public Object getExtendableProperty(String name) {
        return getExtendableProperties().get(name);
    }

    public void setExtendableProperty(String name, Object value) {
        getExtendableProperties().put(name, value);
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
    private Integer mId;
    private Map mExtendableProperties;
}
