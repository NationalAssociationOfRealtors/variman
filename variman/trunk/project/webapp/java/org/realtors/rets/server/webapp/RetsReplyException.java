/*
 */
package org.realtors.rets.server.webapp;

import org.apache.commons.lang.exception.NestableException;

/**
 * A class representing an error case that can be represented by a RETS reply
 * code.
 */
public class RetsReplyException extends NestableException
{
    public RetsReplyException(int replyCode, String meaning)
    {
        super(meaning);
        mReplyCode = replyCode;
        mMeaning = meaning;
    }

    public int getReplyCode()
    {
        return mReplyCode;
    }

    public String getMeaning()
    {
        return mMeaning;
    }

    private int mReplyCode;
    private String mMeaning;
}
