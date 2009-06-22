/*
 * Variman RETS Server
 *
 * Author: Dave Dribin, Danny Hurlburt
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.config;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.realtors.rets.server.Group;

public class SecurityConstraints
{
    public SecurityConstraints()
    {
        mGroupRules = Collections.EMPTY_LIST;
        mGroupRulesByName = new HashMap();
    }
    
    /**
     * @deprecated Use setAllGroupRules instead
     */
    public void setAllConstraints(List/*GroupRules*/ allGroupRules)
    {
        setAllGroupRules(allGroupRules);
    }
    
    protected void resetGroupRulesByName(List/*GroupRules*/ allGroupRules)
    {
        mGroupRulesByName.clear();
        for (Iterator iter = allGroupRules.iterator(); iter.hasNext(); )
        {
            GroupRules groupRules = (GroupRules)iter.next();
            mGroupRulesByName.put(groupRules.getGroup().getName(), groupRules);
        }
    }

    public List/*GroupRules*/ getAllGroupRules()
    {
        return mGroupRules;
    }
    
    public void setAllGroupRules(List/*GroupRules*/ allGroupRules)
    {
        mGroupRules = allGroupRules;
        resetGroupRulesByName(mGroupRules);
    }

    public GroupRules getRulesForGroup(String groupName)
    {
        GroupRules groupRules = (GroupRules)mGroupRulesByName.get(groupName);
        return groupRules;
    }

    public void removeRulesForGroup(String groupName)
    {
        GroupRules groupRules = (GroupRules) mGroupRulesByName.get(groupName);
        if (groupRules != null)
        {
            mGroupRules.remove(groupRules);
            mGroupRulesByName.remove(groupName);
        }
    }

    public List/*GroupRules*/ getAllRulesForGroups(Collection/*Group*/ groups)
    {
        List/*GroupRules*/ allRules = new ArrayList/*GroupRules*/();
        for (Iterator iter = groups.iterator(); iter.hasNext();)
        {
            Group group = (Group)iter.next();
            GroupRules groupRules = getRulesForGroup(group.getName());
            if (groupRules != null) {
                allRules.add(groupRules);
            }
        }
        return allRules;
    }

    private List/*GroupRules*/ mGroupRules;
    private Map/*String,GroupRules*/ mGroupRulesByName;
}
