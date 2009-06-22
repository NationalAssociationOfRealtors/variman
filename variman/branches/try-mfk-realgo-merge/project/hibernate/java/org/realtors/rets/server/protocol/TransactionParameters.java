/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.protocol;

import java.util.Map;

public abstract class TransactionParameters
{
    /**
     * Extracts the value from the parameter map with the specified name. If the
     * parameter map contains multiple values for the same name, then only the
     * first is returned. If a value is not found, <code>null</code> is
     * returned.
     * 
     * @param parameterMap the parameter map to search. Must not be
     *        <code>null</code>.
     * @param name the name of the parameter to search for. Must not be
     *        <code>null</code>.
     * @return the value associated with the specified parameter name if found,
     *         otherwise <code>false</code>.
     */
    protected String getParameter(Map/*String,String[]*/ parameterMap, String name)
    {
        String[] values = (String[])parameterMap.get(name);
        if (values != null && values.length > 0)
        {
            return values[0];
        }
        else
        {
            return null;
        }
    }
}
