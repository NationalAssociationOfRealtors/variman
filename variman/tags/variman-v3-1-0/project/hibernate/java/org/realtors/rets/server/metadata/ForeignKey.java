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
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 */
public class ForeignKey extends ServerMetadata implements Serializable
{
    /**
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
     * @return a String
     *
     */
    public String getForeignKeyID()
    {
        return mForeignKeyID;
    }

    public void setForeignKeyID(String foreignKeyID)
    {
        mForeignKeyID = foreignKeyID;
    }

    /**
     * @return a MSystem object
     *
     */
    public MSystem getSystem()
    {
        return mSystem;
    }

    public void setSystem(MSystem system)
    {
        mSystem = system;
    }

    /**
     * @return a Table object
     *
     */
    public Table getParentTable()
    {
        return mParentTable;
    }

    public void setParentTable(Table parentTable)
    {
        mParentTable = parentTable;
    }

    /**
     * @return a Table object
     *
     */
    public Table getChildTable()
    {
        return mChildTable;
    }

    public void setChildTable(Table childTable)
    {
        mChildTable = childTable;
    }
    
    /**
     *  @return Conditional Parent Field
     */
    public String getConditionalParentField()
    {
    	return mConditionalParentField;
    }
    
    public void setConditionalParentField(String conditionalParentField)
    {
    	mConditionalParentField = conditionalParentField;
    }

    /**
     *  @return Conditional Parent Value
     */
    public String getConditionalParentValue()
    {
    	return mConditionalParentValue;
    }
    
    public void setConditionalParentValue(String conditionalParentValue)
    {
    	mConditionalParentValue = conditionalParentValue;
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
        if (!(other instanceof ForeignKey))
        {
            return false;
        } 
        ForeignKey castOther = (ForeignKey) other;
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
    private String mForeignKeyID;

    /** nullable persistent field */
    private MSystem mSystem;

    /** nullable persistent field */
    private Table mParentTable;

    /** nullable persistent field */
    private Table mChildTable;
    
    /** nullable persistent field */
    private String mConditionalParentField;
    
    /** nullable persistent field */
    private String mConditionalParentValue;
    
    public static final String TABLE = "FOREIGN_KEYS";
}
