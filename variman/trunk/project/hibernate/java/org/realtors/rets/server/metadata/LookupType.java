package org.realtors.rets.server.metadata;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_metadata_lookuptype"
 */ 
public class LookupType implements Serializable
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
     * @hibernate.property length="32"
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
     * @hibernate.property length="32"
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
     * @return a Lookup object
     *
     * @hibernate.many-to-one
     * @hibernate.column name="lookupid"
     *   not-null="true" 
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
        mLevel = mLookup.getPath();
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof LookupType)) return false;
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

    /** identifier field */
    private Long mId;

    /** nullable persistent field */
    private String mLongValue;

    /** nullable persistent field */
    private String mShortValue;

    /** nullable persistent field */
    private String mValue;

    /** nullable persistent field */
    private Lookup mLookup;

    private String mLevel;
}
