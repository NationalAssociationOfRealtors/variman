/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="rets_metadata_validationexternal"
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
     * @hibernate.column name="validationExternalName"
     *   not-null="true"
     *   index="rets_metadata_validationexternal_name_index"
     *   length="32"
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
     *
     * @return a Resource Object
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
     * @hibernate.many-to-one
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
     * @hibernate.set inverse="true"
     * @hibernate.collection-key column="validationexternalid"
     * @hibernate.collection-one-to-many
     *   class="org.realtors.rets.server.metadata.ValidationExternalType"
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
            mValidationExternalTypes = new HashSet();
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

    /** persistent field */
    private Set mValidationExternalTypes;

    private String mLevel;

    public static final String TABLE = "VALIDATION_EXTERNAL";
}
