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

    GetObjectPatternFormatter mNext;
}
