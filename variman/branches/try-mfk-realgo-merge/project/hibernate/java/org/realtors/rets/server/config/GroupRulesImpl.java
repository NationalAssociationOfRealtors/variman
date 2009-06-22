/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.realtors.rets.server.Group;
import org.realtors.rets.server.QueryLimit;

public class GroupRulesImpl implements GroupRules
{
    public GroupRulesImpl()
    {
        this(null);
    }
    
    public GroupRulesImpl(Group group)
    {
        mGroup = group;
        setFilterRules(new ArrayList/*FilterRules*/());
        setConditionRules(new ArrayList/*ConditionRules*/());
        setNoQueryLimit();
    }

    public Group getGroup()
    {
        return mGroup;
    }

    public void setGroup(Group group)
    {
        mGroup = group;
    }

    public List/*FilterRule*/ getFilterRules()
    {
        return mReadOnlyFilterRules;
    }

    public void setFilterRules(List/*FilterRule*/ filterRules)
    {
        if (filterRules == null) {
            mFilterRules = Collections.EMPTY_LIST;
        } else {
            mFilterRules = filterRules;
        }
        mReadOnlyFilterRules = Collections.unmodifiableList(mFilterRules);
    }

    public boolean addFilterRule(FilterRule filterRule)
    {
        return mFilterRules.add(filterRule);
    }

    public boolean removeFilterRule(FilterRule filterRule)
    {
        return mFilterRules.remove(filterRule);
    }

    public List/*ConditionRule*/ getConditionRules()
    {
        return mReadOnlyConditionRules;
    }

    public void setConditionRules(List/*ConditionRule*/ conditionRules)
    {
        if (conditionRules == null) {
            mConditionRules = Collections.EMPTY_LIST;
        } else {
            mConditionRules = conditionRules;
        }
        mReadOnlyConditionRules = Collections.unmodifiableList(mConditionRules);
    }

    public boolean addConditionRule(ConditionRule conditionRule)
    {
        return mConditionRules.add(conditionRule);
    }

    public boolean removeConditionRule(ConditionRule conditionRule)
    {
        return mConditionRules.remove(conditionRule);
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

    public QueryLimit getQueryLimit()
    {
        return mQueryLimit;
    }

    public void setQueryLimit(QueryLimit queryLimit)
    {
        if (queryLimit == null)
        {
            throw new IllegalArgumentException("queryLimit must not be null");
        }
        mQueryLimit = queryLimit;
    }
    
    protected void setNoQueryLimit()
    {
        mQueryLimit = QueryLimit.NO_QUERY_LIMIT;
    }

    private Group mGroup;
    private List mFilterRules;
    private List mReadOnlyFilterRules;
    private List mConditionRules;
    private List mReadOnlyConditionRules;
    private int mRecordLimit;
    private TimeRestriction mTimeRestriction;
    private QueryLimit mQueryLimit;
    
}
