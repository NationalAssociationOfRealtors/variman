/*
 * Variman RETS Server
 *
 * Author: ?, Danny Hurlburt
 * Copyright (c) 200?, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
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
        mSqlConstraints = new HashMap/*String,String*/();
        mDmqlConstraints = new HashMap/*String,String*/(); // TODO: Change this to hold /*String,DmqlQuery*/
    }

    public void addRules(final GroupRules groupRules)
    {
        final String groupName = groupRules.getGroup().getName();
        final List/*ConditionRule*/ conditionRules = groupRules.getConditionRules();
        for (int i = 0; i < conditionRules.size(); i++) {
            final ConditionRule rule = (ConditionRule)conditionRules.get(i);
            final String resourceID = rule.getResourceID();
            final String className = rule.getRetsClassName();
            final String constraintsKey = getConstraintsKey(groupName, resourceID, className);
            final String sqlConstraint = rule.getSqlConstraint();
            if (sqlConstraint != null) {
                mSqlConstraints.put(constraintsKey, sqlConstraint);
            }
            final String dmqlConstraint = rule.getDmqlConstraint();
            if (dmqlConstraint != null) {
                mDmqlConstraints.put(constraintsKey, dmqlConstraint);
            }
        }
    }

    public String findSqlConstraint(final Set/*Group*/ groups, final String resourceID, final String className)
    {
        final StringBuffer allSqlConstaints = new StringBuffer();
        int count = 0;
        String prefix = "(";
        for (Iterator/*Group*/ iterator = groups.iterator(); iterator.hasNext(); ) {
            final Group group = (Group)iterator.next();
            final String sqlConstraint = findSqlConstraint(group, resourceID, className);
            if (StringUtils.isNotBlank(sqlConstraint)) {
                allSqlConstaints.append(prefix).append(sqlConstraint).append(")");
                prefix = " AND (";
                count++;
            }
        }
        if (count > 1) {
            // Surround with parentheses
            allSqlConstaints.insert(0, "(").append(")");
        }
        return allSqlConstaints.toString();
    }

    private String findSqlConstraint(final Group group, final String resourceID, final String className)
    {
        final String groupName = group.getName();
        final String constraintsKey = getConstraintsKey(groupName, resourceID, className);
        final String sqlConstraint = (String)mSqlConstraints.get(constraintsKey);
        if (sqlConstraint == null) {
            return "";
        }
        return sqlConstraint;
    }

    public String findDmqlConstraint(final Set/*Group*/ groups, final String resourceID, final String className)
    {
        final StringBuffer allDmqlConstaints = new StringBuffer();
        int count = 0;
        String prefix = "(";
        for (Iterator/*Group*/ iterator = groups.iterator(); iterator.hasNext(); ) {
            final Group group = (Group)iterator.next();
            final String dmqlConstraint = findDmqlConstraint(group, resourceID, className);
            if (StringUtils.isNotBlank(dmqlConstraint)) {
                allDmqlConstaints.append(prefix).append(dmqlConstraint).append(")");
                prefix = ",(";
                count++;
            }
        }
        if (count > 1) {
            // Surround with parentheses
            allDmqlConstaints.insert(0, "(").append(")");
        }
        return allDmqlConstaints.toString();
    }

    private String findDmqlConstraint(final Group group, final String resourceID, final String className) // TODO: Change this to return DmqlQuery.
    {
        final String groupName = group.getName();
        final String constraintsKey = getConstraintsKey(groupName, resourceID, className);
        final String dmqlConstraint = (String)mDmqlConstraints.get(constraintsKey); // TODO: Change this to return DmqlQuery.
        return dmqlConstraint;
    }
    
    protected static String getConstraintsKey(final String groupName, final String resourceID, final String className)
    {
        final String constraintKey = groupName + ":" + resourceID + ":" + className;
        return constraintKey;
    }

    private Map/*String,String*/ mSqlConstraints;
    private Map/*String,String*/ mDmqlConstraints; // TODO: Change this to hold /*String,DmqlQuery*/
    
}
