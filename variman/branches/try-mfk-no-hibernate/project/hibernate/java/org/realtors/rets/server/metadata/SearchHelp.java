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
 */
public class SearchHelp extends ServerMetadata implements Serializable
{
    public SearchHelp()
    {
    }

    public SearchHelp(long id)
    {
        this();
        mId = new Long(id);
    }

    /**
     *
     * @return a Long object
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
     *
     * @return a String
     *
     */
    public String getSearchHelpID()
    {
        return mSearchHelpID;
    }

    public void setSearchHelpID(String searchHelpID)
    {
        mSearchHelpID = searchHelpID;
    }

    /**
     *
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
     *  @return a String containing the MetadataEntryID
     */
    public String getMetadataEntryID()
    {
    	return mMetadataEntryID;
    }
    
    public void setMetadataEntryID(String metadataEntryID)
    {
    	mMetadataEntryID = metadataEntryID;
    }
    
    /**
     *
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
        return mLevel + ":" + mSearchHelpID;
    }

    public String getTableName()
    {
        return TABLE;
    }

    public String toString()
    {
        return mSearchHelpID;
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof SearchHelp))
        {
            return false;
        }
        SearchHelp castOther = (SearchHelp) other;
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
    private String mSearchHelpID;

    /** nullable persistent field */
    private String mValue;
    
    /** nullable persistent field */
    private String mMetadataEntryID;

    /** nullable persistent field */
    private Resource mResource;

    private String mLevel;

    public static final String TABLE = "SEARCH_HELP";
}
