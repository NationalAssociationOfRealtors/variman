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

import java.util.List;

import org.realtors.rets.server.config.FilterRule.Type;

public class FilterRuleUtils
{
    private FilterRuleUtils()
    {
        // Prevents instantiation.
    }
    
    public static boolean includeSystemName(final FilterRule filterRule, final String systemName)
    {
        FilterRule.Type type = filterRule.getType();
        List/*String*/ systemNames = filterRule.getSystemNames();
        boolean includeSystemName = systemNames.contains(systemName);
        if (type == Type.EXCLUDE)
        {
            includeSystemName = !includeSystemName;
        }
        return includeSystemName;
    }

}
