/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.metadata;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="rets_metadata_table_standard_name"
 *   mutable="false"
 */
public class TableStandardName implements Serializable
{
    public TableStandardName(String name)
    {
        mName = name;
    }

    public TableStandardName()
    {
        mName = null;
    }

    /**
     * Returns this object's unique ID.
     *
     * @return the unique ID.
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
     * Returns the standard name for this object.
     *
     * @return the standard name
     *
     * @hibernate.property length="32"
     *   not-null="true"
     *   unique="true"
     */
    public String getName()
    {
        return mName;
    }

    public void setName(String name)
    {
        mName = name;
    }

    public String toString()
    {
        return mName;
    }

    public boolean equals(Object object)
    {
        if (!(object instanceof Table))
        {
            return false;
        }
        TableStandardName other = (TableStandardName) object;
        return new EqualsBuilder()
            .append(mId, other.mId)
            .isEquals();
    }

    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(mId)
            .toHashCode();
    }

    private String mName;
    private Long mId;
}
