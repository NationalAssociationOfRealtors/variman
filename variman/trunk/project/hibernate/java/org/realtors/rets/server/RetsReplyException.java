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

/**
 * A class representing an error case that can be represented by a RETS reply
 * code.
 */
public class RetsReplyException extends RetsServerException
{
    public RetsReplyException(int replyCode, String meaning)
    {
        super(meaning);
        mReplyCode = replyCode;
        mMeaning = meaning;
    }

    public RetsReplyException(ReplyCode replyCode)
    {
        this(replyCode.getValue(), replyCode.getName());
    }

    public RetsReplyException(ReplyCode replyCode, String meaning)
    {
        this(replyCode.getValue(), replyCode.getName() + ": " + meaning);
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
