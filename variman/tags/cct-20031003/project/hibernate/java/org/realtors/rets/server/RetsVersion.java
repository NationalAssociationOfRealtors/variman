/*
 */
package org.realtors.rets.server;

import org.apache.commons.lang.enum.Enum;

public class RetsVersion extends Enum
{
    public static final RetsVersion RETS_1_0 = new RetsVersion("RETS/1.0");
    public static final RetsVersion RETS_1_5 = new RetsVersion("RETS/1.5");

    private RetsVersion(String version)
    {
        super(version);
    }
}
