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
public class ValidationLookupType extends ServerMetadata implements Serializable
{
    public ValidationLookupType()
    {
    }

    public ValidationLookupType(long id)
    {
        this();
        mId = new Long(id);
    }

    /**
     *
     * @return a Long object
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
     */
    public String getValidText()
    {
        return mValidText;
    }

    public void setValidText(String validText)
    {
        mValidText = validText;
    }

    /**
     *
     * @return a String
     */
    public String getParent1Value()
    {
        return mParent1Value;
    }

    public void setParent1Value(String parent1Value)
    {
        mParent1Value = parent1Value;
    }

    /**
     *
     * @return a String
     */
    public String getParent2Value()
    {
        return mParent2Value;
    }

    public void setParent2Value(String parent2Value)
    {
        mParent2Value = parent2Value;
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
     * @return a ValidationLookupType object
     */
    public ValidationLookup getValidationLookup()
    {
        return mValidationLookup;
    }

    public void setValidationLookup(ValidationLookup validationLookup)
    {
        mValidationLookup = validationLookup;
    }

    /**
     * Returns the hierarchy level for this metadata object.
     *
     * @return the hierarchy level for this metadata object.
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
        mLevel = mValidationLookup.getPath();
    }

    public String getTableName()
    {
        return TABLE;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof ValidationLookupType))
        {
            return false;
        }
        ValidationLookupType castOther = (ValidationLookupType) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public Object accept(MetadataVisitor visitor)
    {
        return visitor.visit(this);
    }

    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }

    /** identifier field */
    private Long mId;

    /** nullable persistent field */
    private String mValidText;

    /** nullable persistent field */
    private String mParent1Value;

    /** nullable persistent field */
    private String mParent2Value;
    
    /** nullable persistent field */
    private String mMetadataEntryID;

    /** nullable persistent field */
    private ValidationLookup mValidationLookup;

    private String mLevel;

    public static final String TABLE = "VALIDATION_LOOKUP_TYPE";
}
