package org.realtors.rets.server.metadata;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_metadata_validationexternaltype"
 */
public class ValidationExternalType
    extends ServerMetadata
    implements Serializable
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
     * @hibernate.column name="validationexternalid"
     *   not-null="true"
     */
    public ValidationExternal getValidationExternal()
    {
        return mValidationExternal;
    }

    public void setValidationExternal(ValidationExternal validationExternal)
    {
        mValidationExternal = validationExternal;
    }

    /**
     *
     * @return a Map of Strings that are key value pairs
     *
     * @hibernate.map table="rets_metadata_validationexternaltype_resultfields"
     * @hibernate.collection-key column="id"
     * @hibernate.collection-index column="key"
     *   type="string" length="32"
     * @hibernate.collection-element column="value"
     *   type="string" length="32"
     */
    public Map getResultFields()
    {
        return mResultFields;
    }
    public void setResultFields(Map resultFields)
    {
        mResultFields = resultFields;
    }

    /**
     *
     * @return a Set of Strings that are Table names
     *
     * @hibernate.set inverse="false"
     *   table="rets_metadata_validationexternaltype_searchfield"
     * @hibernate.collection-key column="id"
     * @hibernate.collection-element column="field"
     *   type="string" length="32" not-null="true"
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
     * @return a Set of Strings that are Table names
     *
     * @hibernate.set inverse="false"
     *   table="rets_metadata_validationexternaltype_displayfield"
     * @hibernate.collection-key column="id"
     * @hibernate.collection-element column="field"
     *   type="string" length="32" not-null="true"
     */
    public Set getDisplayField()
    {
        return mDisplayField;
    }

    public void setDisplayField(Set displayField)
    {
        mDisplayField = displayField;
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
        mLevel = mValidationExternal.getPath();
    }

    public String getTableName()
    {
        return TABLE;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof ValidationExternalType))
        {
            return false;
        } 
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
    private ValidationExternal mValidationExternal;

    /** persistent field */
    private Map mResultFields;

    /** persistent field */
    private Set mSearchField;

    /** persistent field */
    private Set mDisplayField;

    private String mLevel;

    public static final String TABLE = "VALIDATION_EXTERNAL_TYPE";
}
