package org.realtors.rets.server.metadata;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_metadata_system"
 */
public class MSystem implements Serializable
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
     * @hibernate.property length="10"
     */
    public String getSystemID()
    {
        return mSystemID;
    }

    public void setSystemID(String systemID)
    {
        mSystemID = systemID;
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
     * @hibernate.property
     */
    public String getComments()
    {
        return mComments;
    }

    public void setComments(String comments)
    {
        mComments = comments;
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
     * @hibernate.property type="date"
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
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="systemid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.Resource"
     */
    public Set getResources()
    {
        return mResources;
    }

    public void setResources(Set resources)
    {
        mResources = resources;
    }

    /**
     *
     * @return
     *
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="systemid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.ForeignKey"
     */
    public Set getForeignKeys()
    {
        return mForeignKeys;
    }

    public void setForeignKeys(Set foreignKeys)
    {
        mForeignKeys = foreignKeys;
    }

    public String getPath()
    {
        return "";
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof MSystem)) return false;
        MSystem castOther = (MSystem) other;
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
    private String mSystemID;

    /** nullable persistent field */
    private String mDescription;

    /** nullable persistent field */
    private String mComments;

    /** nullable persistent field */
    private String mVersion;

    /** nullable persistent field */
    private Date mDate;

    /** persistent field */
    private Set mResources;

    /** persistent field */
    private Set mForeignKeys;
}
