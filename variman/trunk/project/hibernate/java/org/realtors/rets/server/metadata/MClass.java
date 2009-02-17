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
public class MClass extends ServerMetadata implements Serializable
{
    public MClass()
    {
        mTables = Collections.EMPTY_SET;
        mUpdates = Collections.EMPTY_SET;
    }
    
    public MClass(org.realtors.rets.common.metadata.types.MClass clazz)
    {
    	this();
    	mClass = clazz;
    }

    public MClass(long id)
    {
        this();
        mId = new Long(id);
    }

    public MClass(String className)
    {
        this();
        mClassName = className;
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
    public String getClassName()
    {
    	if (mClass != null)
    		return mClass.getClassName();
    	
    	return mClassName;
    }

    public void setClassName(String className)
    {
    	mClassName = className;
    }
     /**
     *
     * @return a ClassStandardNameEnum
     *
     */
    public ClassStandardNameEnum getStandardName()
    {
    	if (mClass != null)
    		return ClassStandardNameEnum.fromString(mClass.getStandardName());
    	return mStandardName;
    }
    
    public void setStandardName(ClassStandardNameEnum standardName)
    {
    	mStandardName = standardName;
    }

     /**
     *
     * @return a String
     *
     */
    public String getVisibleName()
    {
    	if (mClass != null)
        	return mClass.getVisibleName();
    	return mVisibleName;
    }
    
    public void setVisibleName(String visibleName)
    {
    	mVisibleName = visibleName;
    }

     /**
     *
     * @return a String
     *
     */
    public String getDescription()
    {
    	if (mClass != null)
    		return mClass.getDescription();
    	return mDescription;
    }
    
    public void setDescription(String description)
    {
    	mDescription = description;
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
     * @return a Set of Table objects
     *
     */
    public Set getTables()
    {
        return mTables;
    }

    public void setTables(Set tables)
    {
        mTables = tables;
    }

    public void addTable(Table table)
    {
        mTables = ensureNotEmptySet(mTables);
        table.setMClass(this);
        table.updateLevel();
        mTables.add(table);
    }

    private Set ensureNotEmptySet(Set set)
    {
        if (set == Collections.EMPTY_SET)
        {
            return new ListOrderedSet();
        }
        else
        {
            return set;
        }
    }

    /**
     *
     * @return a Set of Update objects
     *
     */
    public Set getUpdates()
    {
        return mUpdates;
    }

    public void setUpdates(Set updates)
    {
        mUpdates = updates;
    }

    public void addUpdate(Update update)
    {
        mUpdates = ensureNotEmptySet(mUpdates);
        update.setMClass(this);
        update.updateLevel();
        mUpdates.add(update);
    }

    /**
     * @return string with DbTable name
     * 
     */
    public String getDbTable()
    {
        return mDbTable;
    }

    /**
     * @param string string containing table name
     */
    public void setDbTable(String string)
    {
        mDbTable = string;
    }

    /**
     * Returns the level of this metadata object.
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

    /**
     * Returns the path to this metadata object.
     *
     * @return a string with the path.
     */
    public String getPath()
    {
    	if (mClass != null)
    		return mLevel + ":" + mClass.getClassName();
    	return mLevel + ":" + mClassName;
    }

    public List getChildren()
    {
        List children = new ArrayList();
        children.add(mTables.toArray(new Table[0]));
        children.add(mUpdates.toArray(new Update[0]));
        return children;
    }

    public String getRetsId()
    {
    	if (mClass != null)
    		return mClass.getVisibleName();
    	return mVisibleName;
    }

    public String getTableName()
    {
        return TABLE;
    }

    public String toString()
    {
    	if (mClass != null)
    		return mClass.getClassName();
    	return mClassName;
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof MClass))
        {
            return false;
        } 
        MClass castOther = (MClass) other;
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
    
    /**
     *
     * @return a String
     *
     */
    public String getClassTimeStamp()
    {
    	if (mClass != null)
    		return mClass.getClassTimeStamp();
    	return mClassTimeStamp;
    }
    
    public void setClassTimeStamp(String classTimeStamp)
    {
    	mClassTimeStamp = classTimeStamp;
    }

    /**
     *
     * @return a String
     *
     */
    public String getDeletedFlagField()
    {
    	if (mClass != null)
    		return mClass.getDeletedFlagField();
    	return mDeletedFlagField;
    }
    
    public void setDeletedFlagField(String deletedFlagField)
    {
    	mDeletedFlagField = deletedFlagField;
    }
    
    /**
     *
     * @return a String
     *
     */
    public String getDeletedFlagValue()
    {
    	if (mClass != null)
    		return mClass.getDeletedFlagValue();
    	return mDeletedFlagValue;
    }
    
    public void setDeletedFlagValue(String deletedFlagValue)
    {
    	mDeletedFlagValue = deletedFlagValue;
    }
    
    /**
     *
     * @return a boolean
     *
     */
    public boolean getHasKeyIndex()
    {
    	if (mClass != null)
    		return mClass.getHasKeyIndex();
    
    	return mHasKeyIndex;
	}
    
    public void setHasKeyIndex(boolean hasKeyIndex)
    {
    	mHasKeyIndex = hasKeyIndex;
    }
    /**
     *
     * @return a boolean
     *
     */
    public String [] getColumnNames()
    {
    	if (mClass != null)
    		return mClass.getAttributeNames();
    	
    	return null;
    }

    /** identifier field */
    private Long mId;

    private org.realtors.rets.common.metadata.types.MClass mClass = null;
    
    /** nullable persistent field */
    private String mClassName;

    /** nullable persistent field */
    private ClassStandardNameEnum mStandardName;

    /** nullable persistent field */
    private String mVisibleName;

    /** nullable persistent field */
    private String mDescription;

    /** nullable persistent field */
    private Resource mResource;

    /** persistent field */
    private Set mTables;

    /** persistent field */
    private Set mUpdates;

    private String mLevel;
    
    private String mDbTable;

    public static final String TABLE = "CLASS";
    
    // 1.7.2
    /** nullable persistent field */
    private String mClassTimeStamp;
    
    /** nullable persistent field */
    private String mDeletedFlagField;
    
    /** nullable persistent field */
    private String mDeletedFlagValue;
    
    /** persistent field */
    private boolean mHasKeyIndex;

}
