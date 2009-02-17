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
public class LookupType extends ServerMetadata implements Serializable
{
    public LookupType()
    {
    }

    public LookupType(long id)
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
     * @return a String
     * 
     */ 
    public String getLongValue()
    {
        return mLongValue;
    }

    public void setLongValue(String longValue)
    {
        mLongValue = longValue;
    }

    /**
     * 
     * @return a String
     * 
     */ 
    public String getShortValue()
    {
        return mShortValue;
    }

    public void setShortValue(String shortValue)
    {
        mShortValue = shortValue;
    }

    /**
     * 
     * @return a String
     * 
     */
    public String getValue()
    {
        return mValue;
    }

    public void setValue(String value)
    {
        mValue = value;
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
     * 
     * @return a Lookup object
     *
     */
    public Lookup getLookup()
    {
        return mLookup;
    }

    public void setLookup(Lookup lookup)
    {
        mLookup = lookup;
    }

    /**
     * Returns the hierarchy level for this metadata object.
     *
     */
    public String getLevel()
    {
        return mLevel;
    }

    public void setLevel(String level)
    {
        mLevel = level;
    }

    public void updateLevel()
    {
        mLevel = mLookup.getPath();
    }

    public String getTableName()
    {
        return TABLE;
    }

    public String getRetsId()
    {
        return mLongValue;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof LookupType))
        {
            return false;
        } 
        LookupType castOther = (LookupType) other;
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
    private String mLongValue;

    /** nullable persistent field */
    private String mShortValue;

    /** nullable persistent field */
    private String mValue;
    
    /** nullable persistent field */
    private String mMetadataEntryID;

    /** nullable persistent field */
    private Lookup mLookup;

    private String mLevel;

    public static final String TABLE = "LOOKUP_TYPE";
}
