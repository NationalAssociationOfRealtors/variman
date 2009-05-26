/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 */
public class MObject extends ServerMetadata implements Serializable
{
    public MObject()
    {
    }

    public MObject(long id)
    {
        this();
        mId = new Long(id);
    }

    /**
     *
     * @return a Long object
     *
     */
    public Long getId()
    {
        return mId;
    }

    public void setId(Long id)
    {
        mId = id;
    }

    /**
     *
     * @return an ObjectTypeEnum object
     *
     */
    public ObjectTypeEnum getObjectType()
    {
        return mObjectType;
    }

    public void setObjectType(ObjectTypeEnum objectType)
    {
        mObjectType = objectType;
    }

    /**
     *
     * @return a String
     *
     */
    public String getMimeType()
    {
        return mMimeType;
    }

    public void setMimeType(String mimeType)
    {
        mMimeType = mimeType;
    }

    /**
     *
     * @return a String
     *
     */
    public String getVisibleName()
    {
        return mVisibleName;
    }

    public void setVisibleName(String visibleName)
    {
        mVisibleName = visibleName;
    }

    /**
     *
     * @return a String
     *
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
     *  @return a String containing the MetadataEntryID
     */
    public String getMetadataEntryID()
    {
    	return mMetadataEntryID;
    }
    
    public void setMetadataEntryID(String metadataEntryID)
    {
    	mMetadataEntryID = metadataEntryID;
    }
    /**
     *  @return a String containing the ObjectTimeStamp
     */
    public String getObjectTimeStamp()
    {
    	return mObjectTimeStamp;
    }
    
    public void setObjectTimeStamp(String objectTimeStamp)
    {
    	mObjectTimeStamp = objectTimeStamp;
    }
    
    /**
     *  @return a String containing the ObjectCount
     */
    public String getObjectCount()
    {
    	return mObjectCount;
    }
    
    public void setObjectCount(String objectCount)
    {
    	mObjectCount = objectCount;
    }
        
    /**
     *
     * @return a Resource object
     *
     */
    public Resource getResource()
    {
        return mResource;
    }

    public void setResource(Resource resource)
    {
        mResource = resource;
    }

    public String getLevel()
    {
        return mResource.getPath();
    }

    public String getTableName()
    {
        return TABLE;
    }

    public String getRetsId()
    {
        return mVisibleName;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof MObject))
        {
            return false;
        }
        MObject castOther = (MObject) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }

    public Object accept(MetadataVisitor visitor)
    {
        return visitor.visit(this);
    }

    /** identifier field */
    private Long mId;

    /** nullable persistent field */
    private ObjectTypeEnum mObjectType;

    /** nullable persistent field */
    private String mMimeType;

    /** nullable persistent field */
    private String mVisibleName;

    /** nullable persistent field */
    private String mDescription;

    /** nullable persistent field */
    private Resource mResource;
    
    /** nullable persistent field */
    private String mMetadataEntryID;
    
    /** nullable persistent field */
    private String mObjectTimeStamp;
    
    /** nullable persistent field */
    private String mObjectCount;

    public static final String TABLE = "OBJECT";
}
