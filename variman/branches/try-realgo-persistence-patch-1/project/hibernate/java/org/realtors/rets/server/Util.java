/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

import java.text.MessageFormat;

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
    
    public static String getVersionString(int version)
    {
	    Object[] dottedVersion = new Object[3];
	    dottedVersion[0] = new Integer(version / 10000000);
	    dottedVersion[1] = new Integer((version / 100000) % 100);
	    dottedVersion[2] = new Integer(version % 100000);
	    MessageFormat format =
	        new MessageFormat("{0,number,#}.{1,number,00}.{2,number,00000}");
	    return format.format(dottedVersion);
    }
}
