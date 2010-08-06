/*
 * Variman RETS Server
 *
 * Author: Mark Klein
 * Copyright (c) 2010, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Class that instantiates a media object from the database.
 * @author mklein
 */

public class MediaKey implements Serializable, Comparable
{
    /**
     * mResourceKey maps to the database column resource_key
     */
    private String mResourceKey;
    /**
     * mObjectID maps to the database column object_id
     */
    private int mObjectID;
    /**
     * mMediaType maps to the database column media_type
     */   
    private String mMediaType;

    public MediaKey(String resourceKey, int objectID)
    {
        mResourceKey = resourceKey;
        mObjectID = objectID;
    }
    
    public MediaKey(String resourceKey, int objectID, String mediaType)
    {
        mResourceKey = resourceKey;
        mObjectID = objectID;
        mMediaType = mediaType;
    }

    protected MediaKey()
    {
        // Empty
    }

    /**
     * Returns the photo resource ID.
     *
     * @return the photo resource ID.
     * 
     * @hibernate.key-property
     *      column="resource_key"
     *      position="1"
     */
    public String getResourceKey()
    {
        return mResourceKey;
    }

    protected void setResourceKey(String resourceKey)
    {
        mResourceKey = resourceKey;
    }
    
    /**
     * Returns the photo object id.
     *
     * @return the photo object id.
     * 
     * @hibernate.key-property
     *      column="object_id"
     *      position="2"
     */
    public int getObjectID()
    {
        return mObjectID;
    }

    protected void setObjectID(int objectID)
    {
        mObjectID = objectID;
    }

    /**
     * Returns the Media Type
     * @return a string containing the Media Type
     * 
     * @hibernate.key-property
     *      column="media_type"
     *      position="3"
     */
    public String getMediaType()
    {
        return mMediaType;
    }
    
    public void setMediaType(String mediaType)
    {
        mMediaType = mediaType;
    }
    
    public String toString()
    {
        return mResourceKey + ":" + mObjectID;
    }

    public String dump()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("ResourceKey", mResourceKey)
            .append("ObjectID", mObjectID)
            .append("MediaType", mMediaType)
            .toString();
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof MediaKey))
        {
            return false;
        }
        MediaKey rhs = (MediaKey) obj;
        return new EqualsBuilder()
            .append(mResourceKey, rhs.mResourceKey)
            .append(mObjectID, rhs.mObjectID)
            .append(mMediaType, rhs.mMediaType)
            .isEquals();
    }

    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(mResourceKey)
            .append(mObjectID)
            .append(mMediaType)
            .toHashCode();
    }

    public int compareTo(Object obj)
    {
        MediaKey rhs = (MediaKey) obj;
        return new CompareToBuilder()
            .append(mResourceKey, rhs.mResourceKey)
            .append(mObjectID, rhs.mObjectID)
            .append(mMediaType, rhs.mMediaType)
            .toComparison();
    }
}
