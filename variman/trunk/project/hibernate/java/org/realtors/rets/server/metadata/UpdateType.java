package org.realtors.rets.server.metadata;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_metadata_updatetype"
 */
public class UpdateType extends ServerMetadata implements Serializable
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
     * @hibernate.many-to-one
     * @hibernate.column name="systemName"
     *   not-null="true"
     *   index="updatetype_systemname_index"
     */
    public Table getTable()
    {
        return mTable;
    }

    public void setTable(Table table)
    {
        mTable = table;
    }

    /**
     *
     * @return an integer
     *
     * @hibernate.property
     */
    public int getSequence()
    {
        return mSequence;
    }

    public void setSequence(int sequence)
    {
        mSequence = sequence;
    }

    /**
     *
     * @return a String
     *
     * @hibernate.property column="r_default"
     */
    public String getDefault()
    {
        return mDefault;
    }

    public void setDefault(String aDefault)
    {
        mDefault = aDefault;
    }

    /**
     *
     * @return an Update object
     *
     * @hibernate.many-to-one
     * @hibernate.column name="updateid"
     *   not-null="true"
     */
    public Update getUpdate()
    {
        return mUpdate;
    }

    public void setUpdate(Update update)
    {
        mUpdate = update;
    }

    /**
     *
     * @return an UpdateHelp object
     *
     * @hibernate.many-to-one
     */
    public UpdateHelp getUpdateHelp()
    {
        return mUpdateHelp;
    }

    public void setUpdateHelp(UpdateHelp updateHelp)
    {
        mUpdateHelp = updateHelp;
    }

    /**
     *
     * @return a ValidationLookup object
     *
     * @hibernate.many-to-one
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
     *
     * @return a ValidationExternal object
     *
     * @hibernate.many-to-one
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
     * @return a Set of UpdateTypeAttributeEnums
     *
     * @hibernate.set inverse="false"
     *   table="rets_metadata_updatetype_attributes"
     * @hibernate.collection-key column="id"
     * @hibernate.collection-element column="attributes"
     *   type="org.realtors.rets.server.metadata.UpdateTypeAttributeEnum"
     */
    public Set getAttributes()
    {
        return mAttributes;
    }

    public void setAttributes(Set attributes)
    {
        mAttributes = attributes;
    }

    /**
     *
     * @return a Set of ValidationExpressions
     *
     * @hibernate.set inverse="false"
     *   table="rets_metadata_updatetype_validationexpressions"
     * @hibernate.collection-key column="id"
     * @hibernate.collection-many-to-many column="validation_expression"
     *   class="org.realtors.rets.server.metadata.ValidationExpression"
     */
    public Set getValidationExpressions()
    {
        return mValidationExpressions;
    }

    public void setValidationExpressions(Set validationExpressions)
    {
        mValidationExpressions = validationExpressions;
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
        mLevel = mUpdate.getPath();
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
        if (!(other instanceof UpdateType)) return false;
        UpdateType castOther = (UpdateType) other;
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
    private Table mTable;

    /** nullable persistent field */
    private int mSequence;

    /** nullable persistent field */
    private String mDefault;

    /** nullable persistent field */
    private Update mUpdate;

    /** nullable persistent field */
    private UpdateHelp mUpdateHelp;

    /** nullable persistent field */
    private ValidationLookup mValidationLookup;

    /** nullable persistent field */
    private ValidationExternal mValidationExternal;

    /** persistent field */
    private Set mAttributes;

    /** persistent field */
    private Set mValidationExpressions;

    private String mLevel;

    public static final String TABLE = "UPDATE_TYPE";
}
