package org.realtors.rets.server.metadata;

import java.io.Serializable;
import java.util.Set;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_metadata_class"
 */
public class MClass implements Serializable
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
     * @return
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
     * @return
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
     * @return
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
     * @return
     *
     * @hibernate.property length="11"
     */
    public String getTableVersion()
    {
        return mTableVersion;
    }

    public void setTableVersion(String tableVersion)
    {
        mTableVersion = tableVersion;
    }

    /**
     *
     * @return
     *
     * @hibernate.property type="date"
     */
    public Date getTableDate()
    {
        return mTableDate;
    }

    public void setTableDate(java.util.Date tableDate)
    {
        mTableDate = tableDate;
    }

    /**
     *
     * @return
     *
     * @hibernate.property length="11"
     */
    public String getUpdateVersion()
    {
        return mUpdateVersion;
    }

    public void setUpdateVersion(String updateVersion)
    {
        mUpdateVersion = updateVersion;
    }

    /**
     *
     * @return
     *
     * @hibernate.property type="date"
     */
    public Date getUpdateDate()
    {
        return mUpdateDate;
    }

    public void setUpdateDate(java.util.Date updateDate)
    {
        mUpdateDate = updateDate;
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
     * @return
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
     * Returns the path to the metadata object.  Similar to how the
     * RETS Client does it.
     *
     * @return a string with the path.
     */
    public String getPath()
    {
        return mResourceid.getPath() + ":" + mClassName;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
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
    private String mTableVersion;

    /** nullable persistent field */
    private java.util.Date mTableDate;

    /** nullable persistent field */
    private String mUpdateVersion;

    /** nullable persistent field */
    private java.util.Date mUpdateDate;

    /** nullable persistent field */
    private Resource mResourceid;

    /** persistent field */
    private Set mTables;

    /** persistent field */
    private Set mUpdates;
}
