/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringStyle;

public class Util
{
    public static final ToStringStyle SHORT_STYLE;

    static
    {
        StandardToStringStyle style = new StandardToStringStyle();
        style.setContentStart(" [");
        style.setFieldSeparator(", ");
        style.setUseShortClassName(true);
        style.setUseIdentityHashCode(false);
        SHORT_STYLE = style;
    }

    public static String toString(boolean bool)
    {
        if (bool)
        {
            return "true";
        }
        else
        {
            return "false";
        }
    }
}
