package org.realtors.rets.server.metadata;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_metadata_update"
 */
public class Update implements Serializable
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
     * @hibernate.column name="updateName"
     *   not-null="true"
     *   unique="true"
     *   index="update_update_name_index"
     *   length="32"
     */
    public String getUpdateName()
    {
        return mUpdateName;
    }

    public void setUpdateName(String updateName)
    {
        mUpdateName = updateName;
    }

    /**
     *
     * @return a String
     *
     * @hibernate.property length="64"
     */
    public String getDescription()
    {
        return mDescription;
    }

    public void setDescription(String description)
    {
        mDescription = description;
    }

    /**
     *
     * @return a String
     *
     * @hibernate.property length="32"
     */
    public String getKeyField()
    {
        return mKeyField;
    }

    public void setKeyField(String keyField)
    {
        mKeyField = keyField;
    }

    /**
     *
     * @return a MClass object
     *
     * @hibernate.many-to-one
     */
    public MClass getMClass()
    {
        return mMClass;
    }

    public void setMClass(MClass MClass)
    {
        mMClass = MClass;
    }

    /**
     *
     * @return a Set of UpdateType objects
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="updateid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.UpdateType"
     */
    public Set getUpdateTypes()
    {
        return mUpdateTypes;
    }

    public void setUpdateTypes(Set updateTypes)
    {
        mUpdateTypes = updateTypes;
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
        mLevel = mMClass.getPath();
    }

    public String getPath()
    {
        return mLevel + ":" + mUpdateName;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof Update)) return false;
        Update castOther = (Update) other;
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
    private String mUpdateName;

    /** nullable persistent field */
    private String mDescription;

    /** nullable persistent field */
    private String mKeyField;

    /** nullable persistent field */
    private MClass mMClass;

    /** persistent field */
    private Set mUpdateTypes;

    private String mLevel;
}
