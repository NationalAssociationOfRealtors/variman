package org.realtors.rets.server.metadata;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="rets_metadata_editmask"
 */
public class EditMask implements Serializable
{
    public EditMask(long id)
    {
        mId = new Long(id);
    }

    public EditMask()
    {
        mId = null;
    }

    /**
     * Returns the ID.
     *
     * @return the ID
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
     * @return a String
     *
     * @hibernate.property
     * @hibernate.column name="edit_mask_id"
     *   not-null="true"
     *   unique="true"
     *   length="32"
     *   index="rets_metadata_editmask_id_index"
     */
    public String getEditMaskID()
    {
        return mEditMaskID;
    }

    public void setEditMaskID(String editMaskID)
    {
        mEditMaskID = editMaskID;
    }

    /**
     * @return a String
     *
     * @hibernate.property length="256"
     */
    public String getValue()
    {
        return mValue;
    }

    public void setValue(String value)
    {
        mValue = value;
    }

    /**
     * @return a Resource object
     *
     * @hibernate.many-to-one
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
        return mLevel + ":" + mEditMaskID;
    }

    public String toString()
    {
        return mEditMaskID;
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof EditMask)) return false;
        EditMask castOther = (EditMask) other;
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
    private String mEditMaskID;

    /** nullable persistent field */
    private String mValue;

    /** nullable persistent field */
    private Resource mResource;

    private String mLevel;
}
