/*
 */
package org.realtors.rets.server;

import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.builder.StandardToStringStyle;

public class Util
{
    public static final ToStringStyle SHORT_STYLE;
    static
    {
        StandardToStringStyle style = new StandardToStringStyle();
        style.setShortClassName(true);
        style.setUseIdentityHashCode(false);
        SHORT_STYLE = style;
    }
}
