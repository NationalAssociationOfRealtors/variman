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
}
