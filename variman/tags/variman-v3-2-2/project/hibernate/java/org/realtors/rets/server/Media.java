/*
 * Variman RETS Server
 *
 * Author: Mark Klein
 * Copyright (c) 2010, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import org.realtors.rets.common.util.RetsDateTime;

/**
 * Class that instantiates a media object from the database.
 * @author mklein
 * 
 *  @hibernate.mapping
 *      package="org.realtors.rets.server"
 *  @hibernate.class
 *      table="rets_media"
 */
public class Media implements Serializable, Comparable
{
    /**
     *  Represents the unique key for this piece of media.
     */
    private Long mId;
    /**
     *  Represents the primary key for this piece of media. The primary
     *  key is a composite key of media type, the resource key (typically
     *  the listing ID), and the object ID.
     */
    private MediaKey mMediaKey;
    /**
     *  Represents the path to the media. This can be a full URL, or it 
     *  can be a path to the file on the local filesystem. It may be appended
     *  to the path established in the configuration file as the base path
     *  to media. This maps to the database column "resource_file_path".
     */
    private String mResourceFilePath;
    /**
     * Represents the description of the media. This maps to the database column 
     * "description".
     */
    private String mDescription;
    /**
     * Represents the timestamp for the date and time this media was last changed.
     * This maps to the database column "timestamp".
     */
    private Date mTimestamp;

    public Media(String resourceKey, int objectID)
    {
        super();
        mMediaKey = new MediaKey(resourceKey, objectID);
    }
    
    public Media(int id, String resourceKey, String resourceFilePath, String description, int objectID, String mediaType, Date timestamp)
    {
        super();
        mMediaKey = new MediaKey(resourceKey, objectID, mediaType);
        mId = Long.valueOf(id);
        mResourceFilePath = resourceFilePath;
        mDescription = description;
        mTimestamp = timestamp;
    }

    protected Media()
    {
        // Empty
    }

    /**
     * Returns the unique media ID.
     *
     * @return the media ID.
     * 
     * @hibernate.property 
     *      column="id"
     *      not-null="true"
     *      unique="true"
     */
    protected Long getId()
    {
        return mId;
    }

    protected void setId(Long id)
    {
        mId = id;
    }
    
    /**
     * Returns the description.
     *
     * @return the description.
     * 
     * @hibernate.property 
     *      column="description"
     *      type="string"
     */
    public String getDescription()
    {
        return mDescription;
    }

    public void setDescription(String description)
    {
        mDescription = description;
    }
    
    /**
     * Returns the media key.
     * 
     * @return the media key.
     * 
     * @hibernate.composite-id
     *      class="org.realtors.rets.server.MediaKey"
     */
    public MediaKey getMediaKey()
    {
        return mMediaKey;
    }
    
    public void setMediaKey(MediaKey mediaKey)
    {
        mMediaKey = mediaKey;
    }
        
    /**
     * Returns the resource File Path.
     * @return A String containing the resource file path.
     * 
     *  @hibernate.property 
     *      column="resource_file_path"
     *      not-null="true"
     */
    public String getResourceFilePath()
    {
        return mResourceFilePath;
    }
    
    protected void setResourceFilePath(String resourceFilePath)
    {
        mResourceFilePath = resourceFilePath;
    }
    
    /**
     * Returns the photo resource ID.
     *
     * @return the photo resource ID.
     */
    public String getResourceKey()
    {
        return mMediaKey.getResourceKey();
    }

    protected void setResourceKey(String resourceKey)
    {
        mMediaKey.setResourceKey(resourceKey);
    }
    
    /**
     * Returns the photo object id.
     *
     * @return the photo object id.
     */
    public int getObjectID()
    {
        return mMediaKey.getObjectID();
    }

    protected void setObjectID(int objectID)
    {
        mMediaKey.setObjectID(objectID);
    }

    /**
     * Returns the modification timestamp
     * @return a timestamp containing the last modification date
     * 
     * @hibernate.property
     *      column="modification_timestamp"
     *      type="timestamp"
     */
    public Date getTimestamp()
    {
        return mTimestamp;
    }
    
    public void setTimestamp(Date timestamp)
    {
        mTimestamp = timestamp;
    }
    
    /**
     * Returns the Media Type
     * @return a string containing the Media Type
     */
    public String getMediaType()
    {
        return mMediaKey.getMediaType();
    }
    
    public void setMediaType(String mediaType)
    {
        mMediaKey.setMediaType(mediaType);
    }
    
    public String toString()
    {
        return mMediaKey.getResourceKey() + ":" + mMediaKey.getObjectID();
    }

    public String dump()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("id", mId)
            .append("ResourceKey", mMediaKey.getResourceKey())
            .append("ObjectID", mMediaKey.getObjectID())
            .append("Description", mDescription)
            .append("FilePath", mResourceFilePath)
            .append("MediaType", mMediaKey.getMediaType())
            .append("Timestamp", mTimestamp)
            .toString();
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof Media))
        {
            return false;
        }
        Media rhs = (Media) obj;
        return new EqualsBuilder()
            .append(mMediaKey.getResourceKey(), rhs.mMediaKey.getResourceKey())
            .append(mMediaKey.getObjectID(), rhs.mMediaKey.getObjectID())
            .append(mTimestamp, rhs.mTimestamp)
            .isEquals();
    }

    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(mMediaKey.getResourceKey())
            .append(mMediaKey.getObjectID())
            .append(mTimestamp)
            .toHashCode();
    }

    public int compareTo(Object obj)
    {
        Media rhs = (Media) obj;
        return new CompareToBuilder()
            .append(mMediaKey.getResourceKey(), rhs.mMediaKey.getResourceKey())
            .append(mMediaKey.getObjectID(), rhs.mMediaKey.getObjectID())
            .append(mTimestamp, rhs.mTimestamp)
            .toComparison();
    }
}
