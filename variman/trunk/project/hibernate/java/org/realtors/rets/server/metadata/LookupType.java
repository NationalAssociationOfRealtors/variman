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
     * @return
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
     * @return
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
     * @return
     *
     * @hibernate.many-to-one 
     */
    public Lookup getLookupid()
    {
        return mLookupid;
    }

    public void setLookupid(Lookup lookupid)
    {
        mLookupid = lookupid;
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
    private Lookup mLookupid;

}
