package org.realtors.rets.server.metadata;

import java.io.Serializable;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_metadata_validationexternaltype"
 */
public class ValidationExternalType implements Serializable
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
     * @return returns a ValdiationExternal object
     *
     * @hibernate.many-to-one
     */
    public ValidationExternal getValidationExternalID()
    {
        return mValidationExternalID;
    }

    public void setValidationExternalID(ValidationExternal validationExternalID)
    {
        mValidationExternalID = validationExternalID;
    }

    /**
     *
     * @return
     *
     * // todo fix XDoclet to support index-many-to-many
     * hibernate.map table="rets_validationexternaltype_resultfields"
     * hibernate.collection-key column="id"
     * hibernate.collection-index column="key"
     *   class="org.realtors.rets.server.metadata.Table"
     * hibernate.collection-many-to-many column="value"
     *   class="org.realtors.rets.server.metadata.Table"
     */
    public SortedMap getResultFields()
    {
        return mResultFields;
    }
    public void setResultFields(SortedMap resultFields)
    {
        mResultFields = resultFields;
    }

    /**
     *
     * @return a Set of Table objects
     *
     * @hibernate.set inverse="false"
     *   table="rets_validationexternaltype_searchfield"
     * @hibernate.collection-key column="id"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.Table"
     */
    public Set getSearchField()
    {
        return mSearchField;
    }

    public void setSearchField(Set searchField)
    {
        mSearchField = searchField;
    }

    /**
     *
     * @return a Set of Table objects
     *
     * @hibernate.set inverse="false"
     *   table="rets_validationexternaltype_displayfield"
     * @hibernate.collection-key column="id"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.Table"
     */
    public Set getDisplayField()
    {
        return mDisplayField;
    }

    public void setDisplayField(Set displayField)
    {
        mDisplayField = displayField;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof ValidationExternalType)) return false;
        ValidationExternalType castOther = (ValidationExternalType) other;
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
    private ValidationExternal mValidationExternalID;

    /** persistent field */
    private SortedMap mResultFields;

    /** persistent field */
    private Set mSearchField;

    /** persistent field */
    private Set mDisplayField;
}
