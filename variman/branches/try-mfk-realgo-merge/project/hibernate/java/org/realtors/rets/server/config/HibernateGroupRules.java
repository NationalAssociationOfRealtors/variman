/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2007, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.realtors.rets.server.Group;
import org.realtors.rets.server.QueryLimit;

public class HibernateGroupRules implements GroupRules
{
    public HibernateGroupRules()
    {
        super();
    }

    public Integer getId() {
        return m_id;
    }

    public void setId(Integer id) {
        m_id = id;
    }

    public Group getGroup() {
        return m_group;
    }

    public void setGroup(Group group) {
        m_group = group;
    }

    public int getRecordLimit() {
        return m_recordLimit;
    }

    public void setRecordLimit(int recordLimit) {
        m_recordLimit = recordLimit;
    }

    public Set/*ConditionRule*/ getConditionRulesSet() {
        return m_conditionRulesSet;
    }

    public void setConditionRulesSet(Set/*ConditionRule*/ conditionRules) {
        if (conditionRules == null) {
            throw new NullPointerException("conditionRules is null.");
        }
        m_conditionRulesSet = conditionRules;
        m_conditionRulesSetChanged = true;
    }

    public List/*ConditionRule*/ getConditionRules() {
        if (m_conditionRulesSetChanged) {
            updateConditionRulesList();
        }
        return Collections.unmodifiableList(m_conditionRulesList);
    }
    
    private void updateConditionRulesList()
    {
        Set/*ConditionRule*/ conditionRulesSet = getConditionRulesSet();
        m_conditionRulesList = new ArrayList/*ConditionRule*/(conditionRulesSet);
        m_conditionRulesSetChanged = false;
    }

    public void setConditionRules(List/*ConditionRule*/ conditionRules) {
        if (conditionRules == null) {
            throw new NullPointerException("conditionRules is null.");
        }
        m_conditionRulesList = conditionRules;
        updateConditionRulesSet();
    }

    private void updateConditionRulesSet()
    {
        List/*ConditionRule*/ conditionRulesList = getConditionRules();
        m_conditionRulesSet = new LinkedHashSet/*ConditionRule*/(conditionRulesList);
    }

    public boolean addConditionRule(ConditionRule conditionRule)
    {
        if (conditionRule == null) {
            throw new NullPointerException("conditionRule is null.");
        }
        boolean wasAdded = getConditionRulesSet().add(conditionRule);
        if (wasAdded) {
            m_conditionRulesSetChanged = true;
        }
        return wasAdded;
    }

    public boolean removeConditionRule(ConditionRule conditionRule)
    {
        boolean wasRemoved = getConditionRulesSet().remove(conditionRule);
        if (wasRemoved) {
            m_conditionRulesSetChanged = true;
        }
        return wasRemoved;
    }
    
    public Set/*FilterRule*/ getFilterRulesSet() {
        return m_filterRulesSet;
    }

    public void setFilterRulesSet(Set/*FilterRule*/ filterRules) {
        if (filterRules == null) {
            throw new NullPointerException("filterRules is null.");
        }
        m_filterRulesSet = filterRules;
        m_filterRulesSetChanged = true;
    }

    public List/*FilterRule*/ getFilterRules() {
        if (m_filterRulesSetChanged) {
            updateFilterRulesList();
        }
        return Collections.unmodifiableList(m_filterRulesList);
    }
    
    private void updateFilterRulesList()
    {
        Set/*FilterRule*/ filterRulesSet = getFilterRulesSet();
        m_filterRulesList = new ArrayList/*FilterRule*/(filterRulesSet);
        m_filterRulesSetChanged = false;
    }

    public void setFilterRules(List/*FilterRule*/ filterRules) {
        if (filterRules == null) {
            throw new NullPointerException("filterRules is null.");
        }
        m_filterRulesList = filterRules;
        updateFilterRulesSet();
    }

    private void updateFilterRulesSet()
    {
        List/*FilterRule*/ filterRulesList = getFilterRules();
        m_filterRulesSet = new LinkedHashSet/*FilterRule*/(filterRulesList);
    }

    public boolean addFilterRule(FilterRule filterRule)
    {
        if (filterRule == null) {
            throw new NullPointerException("filterRule is null.");
        }
        boolean wasAdded = getFilterRulesSet().add(filterRule);
        if (wasAdded) {
            m_filterRulesSetChanged = true;
        }
        return wasAdded;
    }

    public boolean removeFilterRule(FilterRule filterRule)
    {
        boolean wasRemoved = getFilterRulesSet().remove(filterRule);
        if (wasRemoved) {
            m_filterRulesSetChanged = true;
        }
        return wasRemoved;
    }
    
    public QueryLimit getQueryLimit() {
        if (m_queryLimitChanged) {
            updateQueryLimit();
        }
        return m_queryLimit;
    }

    public void setQueryLimit(QueryLimit queryLimit) {
        if (queryLimit == null) {
            throw new NullPointerException("queryLimit is null.");
        }
        m_queryLimit = queryLimit;
        updateQueryLimitParts();
    }
    
    protected void updateQueryLimitParts()
    {
        long limit = m_queryLimit.getLimit();
        QueryLimit.Period period = m_queryLimit.getPeriod();
        m_limit = limit;
        m_periodStr = period.getName();
    }

    public TimeRestriction getTimeRestriction() {
        if (m_timeRestrictionChanged) {
            updateTimeRestriction();
        }
        return m_timeRestriction;
    }
    
    public void setTimeRestriction(TimeRestriction timeRestriction) {
        m_timeRestriction = timeRestriction;
        updateTimeRestrictionParts();
    }
    
    protected void updateTimeRestrictionParts()
    {
        if (m_timeRestriction != null) {
            String policyName = m_timeRestriction.getPolicy().getName();
            int startHour = m_timeRestriction.getStartHour();
            int startMinute = m_timeRestriction.getStartMinute();
            int endHour = m_timeRestriction.getEndHour();
            int endMinute = m_timeRestriction.getEndMinute();
            m_policyName = policyName; // Calling setter causes infinite loop!!
            m_startHour = new Integer(startHour); // Calling setter causes infinite loop!! Not using Integer.valueOf(int) to be Java1.4 backwards-compatible.
            m_startMinute = new Integer(startMinute); // Calling setter causes infinite loop!! Not using Integer.valueOf(int) to be Java1.4 backwards-compatible.
            m_endHour = new Integer(endHour); // Calling setter causes infinite loop!! Not using Integer.valueOf(int) to be Java1.4 backwards-compatible.
            m_endMinute = new Integer(endMinute); // Calling setter causes infinite loop!! Not using Integer.valueOf(int) to be Java1.4 backwards-compatible.
        } else {
            m_policyName = null;
            m_startHour = null;
            m_startMinute = null;
            m_endHour = null;
            m_endMinute = null;
        }
    }

    public String getPolicyName() {
        return m_policyName;
    }

    public void setPolicyName(String policyName) {
        m_policyName = policyName;
        m_timeRestrictionChanged = true;
    }

    public Integer getStartHour() {
        return m_startHour;
    }

    public void setStartHour(Integer startHour) {
        m_startHour = startHour;
        m_timeRestrictionChanged = true;
    }

    public Integer getStartMinute() {
        return m_startMinute;
    }

    public void setStartMinute(Integer startMinute) {
        m_startMinute = startMinute;
        m_timeRestrictionChanged = true;
    }
    
    public Integer getEndHour() {
        return m_endHour;
    }

    public void setEndHour(Integer endHour) {
        m_endHour = endHour;
        m_timeRestrictionChanged = true;
    }

    public Integer getEndMinute() {
        return m_endMinute;
    }

    public void setEndMinute(Integer endMinute) {
        m_endMinute = endMinute;
        m_timeRestrictionChanged = true;
    }

    protected void updateTimeRestriction()
    {
        String policyName = getPolicyName();
        TimeRestriction.Policy trPolicy = findTRPolicy(policyName);
        boolean policyFound = (trPolicy != null);
        Integer startHour = getStartHour();
        Integer startMinute = getStartMinute();
        Integer endHour = getEndHour();
        Integer endMinute = getEndMinute();
        if (policyFound && startHour != null && startMinute != null && endHour != null && endMinute != null) {
            m_timeRestriction = new TimeRestriction(trPolicy, startHour.intValue(), startMinute.intValue(), endHour.intValue(), endMinute.intValue()); // Calling setter causes infinite loop!!
        } else {
            m_timeRestriction = null; // Calling setter causes infinite loop!!
        }
        m_timeRestrictionChanged = false;
    }
    
    public long getLimit() {
        return m_limit;
    }

    public void setLimit(long limit) {
        m_limit = limit;
        m_queryLimitChanged = true;
    }

    public String getPeriod() {
        return m_periodStr;
    }

    public void setPeriod(String periodStr) {
        m_periodStr = periodStr;
        m_queryLimitChanged = true;
    }
    
    protected void updateQueryLimit()
    {
        long limit = getLimit();
        String periodStr = getPeriod();
        QueryLimit.Period period = findPeriod(periodStr);
        if (period == null && limit != QueryLimit.UNLIMITED) {
            throw new IllegalStateException("period '" + periodStr + "' is not a valid period.");
        }
        m_queryLimit = QueryLimit.valueOf(limit, period);
        m_queryLimitChanged = false;
    }

    protected QueryLimit.Period findPeriod(String periodStr)
    {
        List/*QueryLimit.Period*/ periods = QueryLimit.Period.getEnumList();
        
        for (Iterator iter = periods.iterator(); iter.hasNext(); ) {
            QueryLimit.Period period = (QueryLimit.Period)iter.next();
            if (period.getName().equals(periodStr)) {
                return period;
            }
        }
        return null; // not found
    }

    protected TimeRestriction.Policy findTRPolicy(String policyName)
    {
        List/*TimeRestriction.Policy*/ policies = TimeRestriction.Policy.getEnumList();
        
        for (Iterator iter = policies.iterator(); iter.hasNext(); ) {
            TimeRestriction.Policy policy = (TimeRestriction.Policy)iter.next();
            if (policy.getName().equals(policyName)) {
                return policy;
            }
        }
        return null; // not found
    }

    private Integer m_id;
    private Group m_group;
    private List/*FilterRule*/ m_filterRulesList = Collections.EMPTY_LIST;
    private Set/*FilterRule*/ m_filterRulesSet = Collections.EMPTY_SET;
    private boolean m_filterRulesSetChanged = true;
    private List/*ConditionRule*/ m_conditionRulesList = Collections.EMPTY_LIST;
    private Set/*ConditionRule*/ m_conditionRulesSet = Collections.EMPTY_SET;
    private boolean m_conditionRulesSetChanged = true;
    private int m_recordLimit;
    private long m_limit;
    private String m_periodStr;
    private String m_policyName;
    private Integer m_startHour;
    private Integer m_startMinute;
    private Integer m_endHour;
    private Integer m_endMinute;
    
    private TimeRestriction m_timeRestriction;
    private boolean m_timeRestrictionChanged;
    private QueryLimit m_queryLimit = QueryLimit.NO_QUERY_LIMIT;
    private boolean m_queryLimitChanged;
    
}
