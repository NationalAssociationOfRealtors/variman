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

public abstract class GetObjectPatternFormatter
{
    public void format(StringBuffer buffer, GetObjectPatternContext context)
    {
        doFormat(buffer, context);
        if (mNext != null)
        {
            mNext.format(buffer, context);
        }
    }

    protected abstract void doFormat(StringBuffer buffer,
                                     GetObjectPatternContext context);

    public void setNext(GetObjectPatternFormatter next)
    {
        mNext = next;
    }

    private GetObjectPatternFormatter mNext;
}
