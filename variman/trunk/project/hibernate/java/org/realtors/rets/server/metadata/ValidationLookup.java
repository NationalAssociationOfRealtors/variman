package org.realtors.rets.server.metadata;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="rets_metadata_validationlookup"
 */
public class ValidationLookup extends ServerMetadata implements Serializable
{
    public ValidationLookup(long id)
    {
        mId = new Long(id);
    }

    public ValidationLookup()
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
     * @return a Table SystemName
     *
     * @hibernate.property
     */
    public String getParent1Field()
    {
        return mParent1Field;
    }

    public void setParent1Field(String parent1Field)
    {
        mParent1Field = parent1Field;
    }

    /**
     *
     * @return a Table SystemName
     *
     * @hibernate.property
     */
    public String getParent2Field()
    {
        return mParent2Field;
    }

    public void setParent2Field(String parent2Field)
    {
        mParent2Field = parent2Field;
    }

    /**
     *
     * @return a Resouce object
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
     *
     * @return a Set of ValidationLookupTypes
     *
     * @hibernate.set inverse="false"
     * @hibernate.collection-key column="validationlookupid"
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
        return mLevel + ":" + mValidationLookupName;
    }

    public String toString()
    {
        return mValidationLookupName;
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
    private String mParent1Field;

    /** nullable persistent field */
    private String mParent2Field;

    /** nullable persistent field */
    private Resource mResource;

    /** persistent field */
    private Set mValidationLookupTypes;

    private String mLevel;
}
