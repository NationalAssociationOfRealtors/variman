/*
 */
package org.realtors.rets.server.protocol;

import java.util.Map;

public class TransactionParameters
{
    protected String getParameter(Map parameterMap, String name)
    {
        String[] values = (String[]) parameterMap.get(name);
        if (values != null)
        {
            return values[0];
        }
        else
        {
            return null;
        }
    }
}
