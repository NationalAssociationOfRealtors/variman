/*
 */
package org.realtors.rets.server.metadata;

import java.util.Date;

import org.realtors.rets.server.metadata.ServerMetadata;

public class MetadataSegment
{
    public MetadataSegment(ServerMetadata[] data, String[] levels,
                           String version, Date date)
    {
        mData = data;
        mLevels = levels;
        mVersion = version;
        mDate = date;
    }

    public ServerMetadata[] getData()
    {
        return mData;
    }

    public String[] getLevels()
    {
        return mLevels;
    }

    public String getVersion()
    {
        return mVersion;
    }

    public Date getDate()
    {
        return mDate;
    }

    private ServerMetadata[] mData;
    private String[] mLevels;
    private String mVersion;
    private Date mDate;
}
