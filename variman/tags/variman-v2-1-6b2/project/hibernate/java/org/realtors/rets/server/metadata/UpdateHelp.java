/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="rets_metadata_updatehelp"
 */
public class UpdateHelp extends ServerMetadata implements Serializable
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
     * @hibernate.column name="resourceid"
     *   not-null="true"
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
        return mLevel + ":" + mUpdateHelpID;
    }

    public String getTableName()
    {
        return TABLE;
    }

    public String toString()
    {
        return mUpdateHelpID;
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof UpdateHelp))
        {
            return false;
        } 
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

    public Object accept(MetadataVisitor visitor)
    {
        return visitor.visit(this);
    }

    /** identifier field */
    private Long mId;

    /** nullable persistent field */
    private String mUpdateHelpID;

    /** nullable persistent field */
    private String mValue;

    /** nullable persistent field */
    private Resource mResource;

    private String mLevel;

    public static final String TABLE = "UPDATE_HELP";
}
