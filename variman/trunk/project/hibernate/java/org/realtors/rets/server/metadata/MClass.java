package org.realtors.rets.server.metadata;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="rets_metadata_class"
 */
public class MClass extends ServerMetadata implements Serializable
{
    public MClass()
    {
    }

    public MClass(long id)
    {
        mId = new Long(id);
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
     * @hibernate.column name="class_name"
     *   not-null="true"
     *   unique="true"
     *   index="class_name_index"
     *   length="32"
     */
    public String getClassName()
    {
        return mClassName;
    }

    public void setClassName(String className)
    {
        mClassName = className;
    }

    /**
     *
     * @return a ClassStandardNameEnum
     *
     * @hibernate.property
     * @hibernate.column name="standardName"
     *   not-null="false"
     *   unique="false"
     *   index="class_standard_name_index"
     */
    public ClassStandardNameEnum getStandardName()
    {
        return mStandardName;
    }

    public void setStandardName(ClassStandardNameEnum standardName)
    {
        mStandardName = standardName;
    }

    /**
     *
     * @return a String
     *
     * @hibernate.property
     * @hibernate.column name="visible_name"
     *   not-null="false"
     *   unique="false"
     *   index="class_visible_name_index"
     *   length="32"
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
     * @return a Resource object
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
     * @return a Set of Table objects
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="classid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.Table"
     */
    public Set getTables()
    {
        return mTables;
    }

    public void setTables(Set tables)
    {
        mTables = tables;
    }

    /**
     *
     * @return a Set of Update objects
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="classid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.Update"
     */
    public Set getUpdates()
    {
        return mUpdates;
    }

    public void setUpdates(Set updates)
    {
        mUpdates = updates;
    }

    /**
     * Returns the level of this metadata object.
     *
     * @return the level of this metadata object
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
        mLevel = mResourceid.getPath();
    }

    /**
     * Returns the path to this metadata object.
     *
     * @return a string with the path.
     */
    public String getPath()
    {
        return mLevel + ":" + mClassName;
    }

    public String toString()
    {
        return mClassName;
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof MClass)) return false;
        MClass castOther = (MClass) other;
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
    private String mClassName;

    /** nullable persistent field */
    private ClassStandardNameEnum mStandardName;

    /** nullable persistent field */
    private String mVisibleName;

    /** nullable persistent field */
    private String mDescription;

    /** nullable persistent field */
    private Resource mResourceid;

    /** persistent field */
    private Set mTables;

    /** persistent field */
    private Set mUpdates;

    private String mLevel;
}
