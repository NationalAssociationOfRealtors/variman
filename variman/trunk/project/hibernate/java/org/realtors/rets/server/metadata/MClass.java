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
        return mVisibleName;
    }

    public String getTableName()
    {
        return TABLE;
    }

    public String toString()
    {
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

    /** identifier field */
    private Long mId;

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
}
