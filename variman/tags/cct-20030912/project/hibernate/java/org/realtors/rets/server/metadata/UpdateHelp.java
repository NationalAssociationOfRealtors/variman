package org.realtors.rets.server.metadata;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="rets_metadata_updatehelp"
 */
public class UpdateHelp implements Serializable
{
    public UpdateHelp(long id)
    {
        mId = new Long(id);
    }

    public UpdateHelp()
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
     * @hibernate.column name="updateHelpID"
     *   not-null="true"
     *   unique="true"
     *   index="rets_metadata_updatehelp_id_index"
     *   length="32"
     */
    public String getUpdateHelpID()
    {
        return mUpdateHelpID;
    }

    public void setUpdateHelpID(String updateHelpID)
    {
        mUpdateHelpID = updateHelpID;
    }

    /**
     *
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

    public String getPath()
    {
        return mResourceid.getPath() + ":" + mUpdateHelpID;
    }

    public String toString()
    {
        return mUpdateHelpID;
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof UpdateHelp)) return false;
        UpdateHelp castOther = (UpdateHelp) other;
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
    private String mUpdateHelpID;

    /** nullable persistent field */
    private String mValue;

    /** nullable persistent field */
    private Resource mResourceid;
}
