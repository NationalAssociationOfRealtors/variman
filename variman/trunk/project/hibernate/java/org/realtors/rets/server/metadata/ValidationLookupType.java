package org.realtors.rets.server.metadata;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_metadata_validationlookuptype"
 */
public class ValidationLookupType implements Serializable
{
    /**
     *
     * @return
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
     * @return
     *
     * @hibernate.property length="32"
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
     * @return
     *
     * @hibernate.property length="32"
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
     * @return
     *
     * @hibernate.property length="32"
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
     *
     * @return
     *
     * @hibernate.many-to-one 
     */
    public ValidationLookup getValidationLookupID()
    {
        return mValidationLookupID;
    }

    public void setValidationLookupID(ValidationLookup validationLookupID)
    {
        mValidationLookupID = validationLookupID;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof ValidationLookupType)) return false;
        ValidationLookupType castOther = (ValidationLookupType) other;
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
    private String mValidText;

    /** nullable persistent field */
    private String mParent1Value;

    /** nullable persistent field */
    private String mParent2Value;

    /** nullable persistent field */
    private ValidationLookup mValidationLookupID;
}
