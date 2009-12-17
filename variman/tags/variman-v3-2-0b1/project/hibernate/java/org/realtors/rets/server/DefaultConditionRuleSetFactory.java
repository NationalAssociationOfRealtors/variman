/*
 * Variman RETS Server
 *
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server;

import java.util.List;

import org.apache.log4j.Logger;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.protocol.ConditionRuleSet;

/**
 * Default implementation of the condition rule set factory.
 */
public class DefaultConditionRuleSetFactory implements ConditionRuleSetFactory
{
    /*- (non-Javadoc)
     * @see org.realtors.rets.server.ConditionRuleSetFactory#getConditionRuleSet(org.realtors.rets.server.config.RetsConfig)
     */
    public ConditionRuleSet getConditionRuleSet(RetsConfig config)
    {
        LOG.debug("Creating condition rule set");
        ConditionRuleSet ruleSet = new ConditionRuleSet();
        List<GroupRules> securityConstraints = config.getAllGroupRules();
        for (int i = 0; i < securityConstraints.size(); i++) {
            GroupRules rules = securityConstraints.get(i);
            LOG.debug("Adding condition rules for " + rules.getGroupName());
            ruleSet.addRules(rules);
        }
        return ruleSet;
    }

    private static final Logger LOG =
        Logger.getLogger(DefaultConditionRuleSetFactory.class);
}
