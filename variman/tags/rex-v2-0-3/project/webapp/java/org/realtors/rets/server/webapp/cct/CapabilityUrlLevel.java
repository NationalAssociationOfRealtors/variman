/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

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
