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
