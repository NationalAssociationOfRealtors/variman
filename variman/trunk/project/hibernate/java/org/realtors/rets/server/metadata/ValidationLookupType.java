package org.realtors.rets.server.metadata;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_metadata_validationlookuptype"
 * @hibernate.jcs-cache usage="read-write"
 */
public class ValidationLookupType extends ServerMetadata implements Serializable
{
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
     * @return a String
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
     * @return a String
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
     * @return a ValidationLookupType object
     *
     * @hibernate.many-to-one
     * @hibernate.column name="validationlookupid"
     *   not-null="true" 
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
        mLevel = mValidationLookup.getPath();
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
    private ValidationLookup mValidationLookup;

    private String mLevel;
}
