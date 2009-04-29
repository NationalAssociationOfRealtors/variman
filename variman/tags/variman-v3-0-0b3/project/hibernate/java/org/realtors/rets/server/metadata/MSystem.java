/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004,2007 The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.realtors.rets.server.Util;

/**
 */
public class MSystem extends ServerMetadata implements Serializable
{
    public MSystem()
    {
        mResources = Collections.EMPTY_SET;
        mForeignKeys = Collections.EMPTY_SET;
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
    public String getSystemID()
    {
        return mSystemID;
    }

    public void setSystemID(String systemID)
    {
        mSystemID = systemID;
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
     * @return a String
     *
     */
    public String getComments()
    {
        return mComments;
    }

    public void setComments(String comments)
    {
        mComments = comments;
    }

    /**
     *
     * @return a integer
     *
     */
    public int getVersion()
    {
        return mVersion;
    }

    public void setVersion(int version)
    {
        mVersion = version;
    }

    public String getVersionString()
    {
    	return Util.getVersionString(mVersion);
    }

    /**
     *
     * @return a Date object
     *
     */
    public Date getDate()
    {
        return mDate;
    }

    public void setDate(Date date)
    {
        mDate = date;
    }

    /**
     *
     * @return a Set of Resource objects
     *
     */
    public Set getResources()
    {
        return mResources;
    }

    public Resource[] getResourcesAsArray()
    {
        return (Resource[]) mResources.toArray(new Resource[0]);
    }

    public void setResources(Set resources)
    {
        mResources = resources;
    }

    public void addResource(Resource resource)
    {
        if (mResources == Collections.EMPTY_SET)
        {
            mResources = new ListOrderedSet();
        }
        resource.setSystem(this);
        resource.updateLevel();
        mResources.add(resource);
    }

    /**
     *
     * @return a Set of ForeignKey objects
     *
     */
    public Set getForeignKeys()
    {
        return mForeignKeys;
    }

    public void setForeignKeys(Set foreignKeys)
    {
        mForeignKeys = foreignKeys;
    }

    /**
     * @return the TimeZoneOffset
     */
    public String getTimeZoneOffset()
    {
    	return mTimeZoneOffset;
    }
    
    public void setTimeZoneOffset(String timeZoneOffset)
    {
    	mTimeZoneOffset = timeZoneOffset;
    }
    
    public String getPath()
    {
        return "";
    }

    public List getChildren()
    {
        List children = new ArrayList();
        children.add(mResources.toArray(new Resource[0]));
        children.add(mForeignKeys.toArray(new ForeignKey[0]));
        return children;
    }

    public String getRetsId()
    {
        return null;
    }

    public String getTableName()
    {
        return TABLE;
    }

    public String getLevel()
    {
        return "";
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof MSystem))
        {
            return false;
        } 
        MSystem castOther = (MSystem) other;
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
    private String mSystemID;

    /** nullable persistent field */
    private String mDescription;

    /** nullable persistent field */
    private String mComments;

    /** nullable persistent field */
    private int mVersion;

    /** nullable persistent field */
    private Date mDate;

    /** persistent field */
    private Set mResources;

    /** persistent field */
    private Set mForeignKeys;
    
    /** persistent field */
    private String mTimeZoneOffset;

    public static final String TABLE = "SYSTEM";
}
