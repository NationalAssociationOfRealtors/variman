/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004,2007 The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 */
public class Lookup extends ServerMetadata implements Serializable
{
    public Lookup()
    {
        mId = null;
        mLookupTypes = Collections.EMPTY_SET;
    }

    public Lookup(long id)
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
    public String getLookupName()
    {
        return mLookupName;
    }

    public void setLookupName(String lookupName)
    {
        mLookupName = lookupName;
    }

    /**
     *
     * @return a String
     *
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
     *
     * @return a Set of LookupType objects
     *
     */
    public Set getLookupTypes()
    {
        return mLookupTypes;
    }

    public void setLookupTypes(Set lookupTypes)
    {
        mLookupTypes = lookupTypes;
    }

    public void addLookupType(LookupType lookupType)
    {
        if (mLookupTypes == Collections.EMPTY_SET)
        {
            mLookupTypes = new ListOrderedSet();
        }
        lookupType.setLookup(this);
        lookupType.updateLevel();
        mLookupTypes.add(lookupType);
    }

    /**
     * Returns the hierarchy level for this metadata object.
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
        return mLevel + ":" + mLookupName;
    }

    public List getChildren()
    {
        List children = new ArrayList();
        children.add(mLookupTypes.toArray(new LookupType[0]));
        return children;
    }

    public String getTableName()
    {
        return TABLE;
    }

    public String getRetsId()
    {
        return mVisibleName;
    }

    public String toString()
    {
        return mLookupName;
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof Lookup))
        {
            return false;
        }
        Lookup castOther = (Lookup) other;
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
    private String mLookupName;

    /** nullable persistent field */
    private String mVisibleName;

    /** nullable persistent field */
    private Resource mResource;
    
    /** nullable persistent field */
    private String mMetadataEntryID;

    /** persistent field */
    private Set mLookupTypes;

    private String mLevel;

    public static final String TABLE = "LOOKUP";
}
