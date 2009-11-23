/*
 * Variman RETS Server
 *
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server;

import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.protocol.ConditionRuleSet;

/**
 * Allows varying which concrete ConditionRuleSet implementation class
 * gets created.
 */
public interface ConditionRuleSetFactory {
    public ConditionRuleSet getConditionRuleSet(RetsConfig config);
}
