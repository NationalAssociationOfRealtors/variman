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

/**
 */
public class ValidationExpression extends ServerMetadata implements Serializable
{
    public ValidationExpression(long id)
    {
        mId = new Long(id);
    }

    public ValidationExpression()
    {
        mId = null;
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
    public String getValidationExpressionID()
    {
        return mValidationExpressionID;
    }

    public void setValidationExpressionID(String validationExpressionID)
    {
        mValidationExpressionID = validationExpressionID;
    }

    /**
     *
     * @return a ValidationExpressionTypeEnum
     *
     */
    public ValidationExpressionTypeEnum getValidationExpressionType()
    {
        return mValidationExpressionType;
    }

    public void setValidationExpressionType(
        ValidationExpressionTypeEnum validationExpressionType)
    {
        mValidationExpressionType = validationExpressionType;
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
        mLevel = mResource.getPath();
    }

    public String getPath()
    {
        return mResource.getPath() + ":" + mValidationExpressionID;
    }

    public String getTableName()
    {
        return TABLE;
    }

    public String toString()
    {
        return mValidationExpressionID;
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof ValidationExpression))
        {
            return false;
        } 
        ValidationExpression castOther = (ValidationExpression) other;
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
    private String mValidationExpressionID;

    /** nullable persistent field */
    private ValidationExpressionTypeEnum mValidationExpressionType;

    /** nullable persistent field */
    private String mValue;

    /** nullable persistent field */
    private Resource mResource;

    private String mLevel;

    public static final String TABLE = "VALIDATION_EXPRESSION";
}
