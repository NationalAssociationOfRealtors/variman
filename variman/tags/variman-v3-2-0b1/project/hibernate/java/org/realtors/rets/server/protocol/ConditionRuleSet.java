/*
 * Variman RETS Server
 *
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.protocol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import org.realtors.rets.server.Group;
import org.realtors.rets.server.config.ConditionRule;
import org.realtors.rets.server.config.GroupRules;

public class ConditionRuleSet
{
    public ConditionRuleSet()
    {
        mSqlConstraints = new HashMap();
    }

    public void addRules(GroupRules rules)
    {
        List conditionRules = rules.getConditionRules();
        for (int i = 0; i < conditionRules.size(); i++)
        {
            ConditionRule rule = (ConditionRule) conditionRules.get(i);
            String constraintsKey = getConstraintsKey(rules.getGroupName(),
                                                      rule.getResource(),
                                                      rule.getRetsClass());
            mSqlConstraints.put(constraintsKey, rule.getSqlConstraint());
        }
    }

    public String findSqlConstraint(Set groups, String resource,
                                    String retsClass)
    {
        StringBuffer allConstraints = new StringBuffer();
        int count = 0;
        String prefix = "(";
        for (Iterator iterator = groups.iterator(); iterator.hasNext();)
        {
            Group group = (Group) iterator.next();
            String constraint = findSqlConstraint(group, resource, retsClass);
            if (StringUtils.isNotBlank(constraint))
            {
                allConstraints.append(prefix).append(constraint).append(")");
                prefix = " AND (";
                count++;
            }
        }
        if (count > 1)
        {
            allConstraints.insert(0, "(").append(")");
        }
        return allConstraints.toString();
    }

    private String findSqlConstraint(Group group, String resource,
                                  String retsClass)
    {
        String groupName = group.getName();
        String constraintsKey = getConstraintsKey(groupName, resource, retsClass);
        String constraint = (String) mSqlConstraints.get(constraintsKey);
        if (constraint == null)
        {
            return "";
        }
        return constraint;
    }

    protected static String getConstraintsKey(final String groupName, final String resourceID, final String className)
    {
        final String constraintKey = groupName + ":" + resourceID + ":" + className;
        return constraintKey;
    }

    private Map mSqlConstraints;
}
