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

public class GetObjectPatternContext
{
    public GetObjectPatternContext()
    {
    }

    public GetObjectPatternContext(String key, int objectId)
    {
        mKey = key;
        mObjectId = objectId;
    }

    public String getKey()
    {
        return mKey;
    }

    public void setKey(String key)
    {
        mKey = key;
    }

    public int getObjectId()
    {
        return mObjectId;
    }

    public void setObjectId(int objectId)
    {
        mObjectId = objectId;
    }

    private String mKey;
    private int mObjectId;
}
