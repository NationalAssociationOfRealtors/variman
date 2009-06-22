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

import java.util.List;

import org.realtors.rets.server.Group;
import org.realtors.rets.server.QueryLimit;

public interface GroupRules
{
    public Group getGroup();
    
    public void setGroup(Group group);

    public List/*FilterRule*/ getFilterRules();
    
    public void setFilterRules(List/*FilterRule*/ filterRules);

    public boolean addFilterRule(FilterRule filterRule);

    public boolean removeFilterRule(FilterRule filterRule);
    
    public List/*ConditionRule*/ getConditionRules();
    
    public void setConditionRules(List/*ConditionRule*/ conditionRules);

    public boolean addConditionRule(ConditionRule conditionRule);

    public boolean removeConditionRule(ConditionRule conditionRule);
    
    public int getRecordLimit();

    public void setRecordLimit(int recordLimit);

    public TimeRestriction getTimeRestriction();

    public void setTimeRestriction(TimeRestriction timeRestriction);

    public QueryLimit getQueryLimit();
    
    public void setQueryLimit(QueryLimit queryLimit);
    
}
