/*
 */
package org.realtors.rets.server.webapp.cct;

import org.apache.commons.lang.enum.Enum;

public class CapabilityUrlLevel extends Enum
{
    public static final CapabilityUrlLevel NORMAL =
        new CapabilityUrlLevel("normal");

    public static final CapabilityUrlLevel MINIMAL =
        new CapabilityUrlLevel("minimal");

    public static final CapabilityUrlLevel MAXIMMAL =
        new CapabilityUrlLevel("maximal");

    public CapabilityUrlLevel(String level)
    {
        super(level);
    }
}
