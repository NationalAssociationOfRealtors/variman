/*
 * Rex RETS Server
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
    public GetObjectPatternContext(String resource, String type,
                                   String key, int objectId)
    {
        mKey = key;
        mObjectId = objectId;
    }

    public String getKey()
    {
        return mKey;
    }

    public int getObjectId()
    {
        return mObjectId;
    }

    private String mKey;
    private int mObjectId;
}
