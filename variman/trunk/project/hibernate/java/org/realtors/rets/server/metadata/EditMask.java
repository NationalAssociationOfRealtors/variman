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
 *
 */
public class EditMask extends ServerMetadata implements Serializable
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

    public String getTableName()
    {
        return TABLE;
    }

    public String toString()
    {
        return mEditMaskID;
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof EditMask))
        {
            return false;
        } 
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

    public Object accept(MetadataVisitor visitor)
    {
        return visitor.visit(this);
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

    public static final String TABLE = "EDIT_MASK";
}
