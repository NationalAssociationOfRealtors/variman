package org.realtors.rets.server.metadata;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_metadata_validationlookup"
 */
public class ValidationLookup implements Serializable
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
     * @hibernate.property
     * @hibernate.column name="validationLookupName"
     *   not-null="true"
     *   unique="true"
     *   index="rets_metadata_validationlookup_name_index"
     *   length="32"
     */
    public String getValidationLookupName()
    {
        return mValidationLookupName;
    }

    public void setValidationLookupName(String validationLookupName)
    {
        mValidationLookupName = validationLookupName;
    }

    /**
     *
     * @return a Table Object
     *
     * @hibernate.many-to-one
     */
    public Table getParent1Field()
    {
        return mParent1Field;
    }

    public void setParent1Field(Table parent1Field)
    {
        mParent1Field = parent1Field;
    }

    /**
     *
     * @return a Table Object
     *
     * @hibernate.many-to-one
     */
    public Table getParent2Field()
    {
        return mParent2Field;
    }

    public void setParent2Field(Table parent2Field)
    {
        mParent2Field = parent2Field;
    }

    /**
     *
     * @return a String
     *
     * @hibernate.property length="11"
     */
    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    /**
     *
     * @return a Date object
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
     * @return a Resouce object
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
     * @return a Set of ValidationLookupTypes
     *
     * @hibernate.set inverse="false"
     * @hibernate.collection-key column="validationLookupID"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.ValidationLookupType"
     */
    public Set getValidationLookupTypes()
    {
        return mValidationLookupTypes;
    }

    public void setValidationLookupTypes(Set validationLookupTypes)
    {
        mValidationLookupTypes = validationLookupTypes;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof ValidationLookup)) return false;
        ValidationLookup castOther = (ValidationLookup) other;
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
    private String mValidationLookupName;

    /** nullable persistent field */
    private Table mParent1Field;

    /** nullable persistent field */
    private Table mParent2Field;

    /** nullable persistent field */
    private String version;

    /** nullable persistent field */
    private Date mDate;

    /** nullable persistent field */
    private Resource mResourceid;

    /** persistent field */
    private Set mValidationLookupTypes;
}
