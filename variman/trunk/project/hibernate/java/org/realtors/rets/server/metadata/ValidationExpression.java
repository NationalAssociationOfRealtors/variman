package org.realtors.rets.server.metadata;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="rets_metadata_validationexpression"
 */
public class ValidationExpression implements Serializable
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
     *   unique="true"
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
    public ValidationExpressionTypeEnum getValidationExpression()
    {
        return mValidationExpression;
    }

    public void setValidationExpression(ValidationExpressionTypeEnum validationExpression)
    {
        mValidationExpression = validationExpression;
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
     */
    public Resource getResourceid()
    {
        return mResourceid;
    }

    public void setResourceid(Resource resourceid)
    {
        mResourceid = resourceid;
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
        mLevel = mResourceid.getPath();
    }

    public String toString()
    {
        return mValidationExpressionID;
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof ValidationExpression)) return false;
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
    private ValidationExpressionTypeEnum mValidationExpression;

    /** nullable persistent field */
    private String mValue;

    /** nullable persistent field */
    private Resource mResourceid;

    private String mLevel;
}
