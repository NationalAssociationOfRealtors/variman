package org.realtors.rets.server.metadata;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_metadata_validationexternal"
 */
public class ValidationExternal implements Serializable
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
     * @hibernate.column name="validationExternalName"
     *   not-null="true"
     *   unique="true"
     *   index="rets_metadata_validationexternal_name_index"
     *   length="32"
     */
    public String getValidationExternalName()
    {
        return mValidationExternalName;
    }

    public void setValidationExternalName(String validationExternalName)
    {
        mValidationExternalName = validationExternalName;
    }

    /**
     *
     * @return
     *
     * *@hibernate.property length="11"
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
     * @hibernate.property column="r_date"
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
     * Returns the search resource. Will be drived from SearchClass.
     *
     * @return a Resource.
     */
    public Resource getSearchResource()
    {
        return mSearchClass.getResourceid();
    }

    /**
     *
     * @return
     *
     * @hibernate.many-to-one
     */
    public MClass getSearchClass()
    {
        return mSearchClass;
    }

    public void setSearchClass(MClass searchClass)
    {
        mSearchClass = searchClass;
    }

    /**
     *
     * @return
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="validationExternalID"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.ValidationExternalType"
     */
    public Set getValidationExternalTypes()
    {
        return mValidationExternalTypes;
    }

    public void setValidationExternalTypes(Set validationExternalTypes)
    {
        mValidationExternalTypes = validationExternalTypes;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof ValidationExternal)) return false;
        ValidationExternal castOther = (ValidationExternal) other;
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
    private String mValidationExternalName;

    /** nullable persistent field */
    private String mVersion;

    /** nullable persistent field */
    private Date mDate;

    /** nullable persistent field */
    private Resource mResourceid;

    /** nullable persistent field */
    private MClass mSearchClass;

    /** persistent field */
    private Set mValidationExternalTypes;
}
