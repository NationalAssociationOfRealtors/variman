/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

public class StringJoiner
{
    public StringJoiner(String separator)
    {
        mSeparator = separator;
        mCurrentSeparator = "";
        mBuffer = new StringBuffer();
    }

    public void append(Object object)
    {
        mBuffer.append(mCurrentSeparator);
        mBuffer.append(object.toString());
        mCurrentSeparator = mSeparator;
    }

    public String toString()
    {
        return mBuffer.toString();
    }

    private String mSeparator;
    private String mCurrentSeparator;
    private StringBuffer mBuffer;
}
