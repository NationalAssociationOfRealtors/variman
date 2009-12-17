/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.metadata;

import java.util.Date;
import java.util.List;

import org.realtors.rets.common.metadata.MetaObject;

public class MetadataSegment
{
    public MetadataSegment(List<MetaObject> data, String[] levels, String version,
                           Date date)
    {
        mData = data.toArray(new MetaObject[data.size()]);
        mDataList = data;
        mLevels = levels;
        mVersion = version;
        mDate = date;
    }

    public MetaObject[] getData()
    {
        return mData;
    }

    public List getDataList()
    {
        return mDataList;
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

    private MetaObject[] mData;
    private List<MetaObject> mDataList;
    private String[] mLevels;
    private String mVersion;
    private Date mDate;
}
