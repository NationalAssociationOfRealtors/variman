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
public class ValidationExternal extends ServerMetadata implements Serializable
{
    public ValidationExternal(long id)
    {
        this();
        mId = new Long(id);
    }

    public ValidationExternal()
    {
        mId = null;
        mValidationExternalTypes = Collections.EMPTY_SET;
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
    public String getValidationExternalName()
    {
        return mValidationExternalName;
    }

    public void setValidationExternalName(String validationExternalName)
    {
        mValidationExternalName = validationExternalName;
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
     * @return a Resource Object
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
     * Returns the search resource. Will be drived from SearchClass.
     *
     * @return a Resource.
     */
    public Resource getSearchResource()
    {
        return mSearchClass.getResource();
    }

    /**
     *
     * @return a MClass object
     *
     */
    public MClass getSearchClass()
    {
        return mSearchClass;
    }

    public void setSearchClass(MClass searchClass)
    {
        mSearchClass = searchClass;
    }

    /**
     *
     * @return a Set of ValidationExternalType objects
     *
     */
    public Set getValidationExternalTypes()
    {
        return mValidationExternalTypes;
    }

    public void setValidationExternalTypes(Set validationExternalTypes)
    {
        mValidationExternalTypes = validationExternalTypes;
    }

    public void addValidationExternalType(
        ValidationExternalType validationExternalType)
    {
        if (mValidationExternalTypes == Collections.EMPTY_SET)
        {
            mValidationExternalTypes = new ListOrderedSet();
        }
        validationExternalType.setValidationExternal(this);
        validationExternalType.updateLevel();
        mValidationExternalTypes.add(validationExternalType);
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
        return mLevel + ":" + mValidationExternalName;
    }

    public List getChildren()
    {
        List children = new ArrayList();
        children.add(
            mValidationExternalTypes.toArray(new ValidationExternalType[0]));
        return children;
    }

    public String getTableName()
    {
        return TABLE;
    }

    public String toString()
    {
        return mValidationExternalName;
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof ValidationExternal))
        {
            return false;
        } 
        ValidationExternal castOther = (ValidationExternal) other;
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
    private String mValidationExternalName;

    /** nullable persistent field */
    private Resource mResource;

    /** nullable persistent field */
    private MClass mSearchClass;
    
    /** nullable persistent field */
    private String mMetadataEntryID;

    /** persistent field */
    private Set mValidationExternalTypes;

    private String mLevel;

    public static final String TABLE = "VALIDATION_EXTERNAL";
}
