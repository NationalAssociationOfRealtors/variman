package org.realtors.rets.server.metadata;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_metadata_lookup"
 */
public class Lookup implements Serializable
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
     * @hibernate.property
     * @hibernate.column name="lookup_name"
     *   not-null="true"
     *   unique="true"
     *   index="rets_metadata_lookup_lookupname_index"
     *   length="32"
     */
    public String getLookupName()
    {
        return mLookupName;
    }

    public void setLookupName(String lookupName)
    {
        mLookupName = lookupName;
    }

    /**
     *
     * @return
     *
     * @hibernate.property length="32"
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
     * @return
     *
     * @hibernate.property length="11"
     */
    public String getVersion()
    {
        return mVersion;
    }

    public void setVersion(String version)
    {
        mVersion = version;
    }

    /**
     *
     * @return
     *
     * @hibernate.property column="rdate"
     *   type="date"
     */
    public Date getDate()
    {
        return mDate;
    }

    public void setDate(Date date)
    {
        mDate = date;
    }

    /**
     *
     * @return
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
     *
     * @return
     *
     * @hibernate.set
     * @hibernate.collection-key column="lookupid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.LookupType"
     */
    public Set getLookupTypes()
    {
        return mLookupTypes;
    }

    public void setLookupTypes(Set lookupTypes)
    {
        mLookupTypes = lookupTypes;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof Lookup)) return false;
        Lookup castOther = (Lookup) other;
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
    private String mLookupName;

    /** nullable persistent field */
    private String mVisibleName;

    /** nullable persistent field */
    private String mVersion;

    /** nullable persistent field */
    private Date mDate;

    /** nullable persistent field */
    private Resource mResourceid;

    /** persistent field */
    private Set mLookupTypes;
}
