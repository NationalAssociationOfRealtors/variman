package org.realtors.rets.server.metadata;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_metadata_object"
 */
public class MObject implements Serializable
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
     * @hibernate.column name="objectType"
     *   not-null="false"
     *   unique="false"
     *   index="rets_metadata_objecttype_index"
     */
    public ObjectTypeEnum getObjectType()
    {
        return mObjectType;
    }

    public void setObjectType(ObjectTypeEnum objectType)
    {
        mObjectType = objectType;
    }

    /**
     *
     * @return
     *
     * @hibernate.property length="24"
     */
    public String getMimeType()
    {
        return mMimeType;
    }

    public void setMimeType(String mimeType)
    {
        mMimeType = mimeType;
    }

    /**
     *
     * @return
     *
     * @hibernate.property length="32"
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

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof MObject)) return false;
        MObject castOther = (MObject) other;
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
    private ObjectTypeEnum mObjectType;

    /** nullable persistent field */
    private String mMimeType;

    /** nullable persistent field */
    private String mVisibleName;

    /** nullable persistent field */
    private String mDescription;

    /** nullable persistent field */
    private org.realtors.rets.server.metadata.Resource mResourceid;
}