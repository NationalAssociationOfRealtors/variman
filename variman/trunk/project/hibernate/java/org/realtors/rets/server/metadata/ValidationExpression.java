package org.realtors.rets.server.metadata;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="rets_metadata_validationexpression"
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
     * @hibernate.id generator-class="native"
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
     * @hibernate.property
     * @hibernate.column name="validationExpressionID"
     *   not-null="true"
     *   index="rets_metadata_validationexpression_id_index"
     *   length="32"
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
     * @hibernate.property
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
     * @hibernate.property length="512"
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
     * @hibernate.many-to-one
     * @hibernate.column name="resourceid"
     *   not-null="true"
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
     * @hibernate.property length="64"
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
