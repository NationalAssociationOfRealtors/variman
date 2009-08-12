/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import org.realtors.rets.server.Util;

/**
 */
public class Table extends ServerMetadata implements Serializable
{
    public Table(long id)
    {
        this(new Long(id));
    }

    public Table()
    {
        this((Long) null);
    }

    public Table(String systemName)
    {
        this();
        mSystemName = systemName;
    }

    private Table(Long id)
    {
        mId = id;
        mDefault = Integer.MAX_VALUE;
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
    public String getSystemName()
    {
        return mSystemName;
    }

    public void setSystemName(String systemName)
    {
        mSystemName = systemName;
    }

    /**
     *
     * @return a TableStandardNameEnum
     *
     */
    public TableStandardName getStandardName()
    {
        return mStandardName;
    }

   public void setStandardName(TableStandardName standardName)
    {
        mStandardName = standardName;
    }

    /**
     *
     * @return a String
     */
    public String getLongName()
    {
        return mLongName;
    }

    public void setLongName(String longName)
    {
        mLongName = longName;
    }

    /**
     *
     * @return a String
     *
     */
    public String getDbName()
    {
        return mDbName;
    }

    public void setDbName(String dbName)
    {
        mDbName = dbName;
    }


    /**
     *
     * @return a String
     *
     */
    public String getShortName()
    {
        return mShortName;
    }

    public void setShortName(String shortName)
    {
        mShortName = shortName;
    }

    /**
     * Get the MaximumLength value.
     * @return the MaximumLength value.
     *
     */
    public int getMaximumLength()
    {
        return mMaximumLength;
    }

    /**
     * Set the MaximumLength value.
     * @param newMaximumLength The new MaximumLength value.
     */
    public void setMaximumLength(int newMaximumLength)
    {
        mMaximumLength = newMaximumLength;
    }

    /**
     *
     * @return a DataTypeEnum object
     *
     */
    public DataTypeEnum getDataType()
    {
        return mDataType;
    }

    public void setDataType(DataTypeEnum dataType)
    {
        mDataType = dataType;
    }

    /**
     *
     * @return an integer
     *
     */
    public int getPrecision()
    {
        return mPrecision;
    }

    public void setPrecision(int precision)
    {
        mPrecision = precision;
    }

    /**
     *
     * @return a boolean
     *
     */
    public boolean isSearchable()
    {
        return mSearchable;
    }

    public void setSearchable(boolean searchable)
    {
        mSearchable = searchable;
    }

    /**
     *
     * @return an InterpretationEnum object
     *
     */
    public InterpretationEnum getInterpretation()
    {
        return mInterpretation;
    }

    public void setInterpretation(InterpretationEnum interpretation)
    {
        mInterpretation = interpretation;
    }

    /**
     *
     * @return an AlignmentEnum object
     *
     */
    public AlignmentEnum getAlignment()
    {
        return mAlignment;
    }

    public void setAlignment(AlignmentEnum alignment)
    {
        mAlignment = alignment;
    }

    /**
     *
     * @return a boolean
     *
     */
    public boolean isUseSeparator()
    {
        return mUseSeparator;
    }

    public void setUseSeparator(boolean useSeparator)
    {
        mUseSeparator = useSeparator;
    }

    /**
     *
     * @return an integer
     *
     */
    public int getMaxSelect()
    {
        return mMaxSelect;
    }

    public void setMaxSelect(int maxSelect)
    {
        mMaxSelect = maxSelect;
    }

    /**
     *
     * @return an UnitEnum object
     *
     */
    public UnitEnum getUnits()
    {
        return mUnits;
    }

    public void setUnits(UnitEnum units)
    {
        mUnits = units;
    }

    /**
     *
     * @return a boolean that if TRUE indicates the field is part of an index.
     *
     */
    public boolean getIndex()
    {
        return mIndex;
    }

    public void setIndex(boolean index)
    {
        mIndex = index;
    }

    /**
     *
     * @return an integer
     *
     */
    public int getMinimum()
    {
        return mMinimum;
    }

    public void setMinimum(int minimum)
    {
        mMinimum = minimum;
    }

    /**
     *
     * @return an integer
     *
     */
    public int getMaximum()
    {
        return mMaximum;
    }

    public void setMaximum(int maximum)
    {
        mMaximum = maximum;
    }

    /**
     *
     * @return an integer
     *
     */
    public int getDefault()
    {
        return mDefault;
    }

    public void setDefault(int aDefault)
    {
        mDefault = aDefault;
    }

    /**
     *
     * @return an integer
     *
     */
    public int getRequired()
    {
        return mRequired;
    }

    public void setRequired(int required)
    {
        mRequired = required;
    }

    /**
     *
     * @return a boolean
     *
     */
    public boolean isUnique()
    {
        return mUnique;
    }

    public void setUnique(boolean unique)
    {
        mUnique = unique;
    }
    
    /**
     *  @return a String
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
     * @return a boolean
     */
    public boolean getModTimeStamp()
    {
    	return mModTimeStamp;
    }
    
    public void setModTimeStamp(boolean modTimeStamp)
    {
    	mModTimeStamp = modTimeStamp;
    }
    
    /**
     *  @return a String
     */
    public String getForeignKeyName()
    {
    	return mForeignKeyName;
    }
    
    public void setForeignKeyName(String foreignKeyName)
    {
    	mForeignKeyName = foreignKeyName;
    }
    
    /**
     * @return a String
     */
    public String getForeignField()
    {
    	return mForeignField;
    }
    
    public void setForeignField(String foreignField)
    {
    	mForeignField = foreignField;
    }
    
    /**
     * @return a boolean
     */
    public boolean getKeyQuery()
    {
    	return mKeyQuery;
    }
    
    public void setKeyQuery(boolean keyQuery)
    {
    	mKeyQuery = keyQuery;
    }
    
    /**
     * @return a boolean
     */
    public boolean getKeySelect()
    {
    	return mKeySelect;
    }
    
    public void setKeySelect(boolean keySelect)
    {
    	mKeySelect = keySelect;
    }

    /**
     * @return a boolean
     */
    public boolean getInKeyIndex()
    {
    	return mInKeyIndex;
    }
    
    public void setInKeyIndex(boolean inKeyIndex)
    {
    	mInKeyIndex = inKeyIndex;
    }
    
    /**
     *
     * @return an MClass object
     *
     */
    public MClass getMClass()
    {
        return mMClass;
    }

    public void setMClass(MClass iMClass)
    {
        mMClass = iMClass;
    }

    /**
     *
     * @return a Set of EditMask objects
     *
     */
    public Set getEditMasks()
    {
        return mEditMasks;
    }

    public void setEditMasks(Set editMasks)
    {
        mEditMasks = editMasks;
    }

    /**
     *
     * @return a Lookup Object
     *
     */
    public Lookup getLookup()
    {
        return mLookup;
    }

    public void setLookup(Lookup lookup)
    {
        mLookup = lookup;
    }

    /**
     *
     * @return a SearchHelp object
     *
     */
    public SearchHelp getSearchHelp()
    {
        return mSearchHelp;
    }

    public void setSearchHelp(SearchHelp searchHelp)
    {
        mSearchHelp = searchHelp;
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
        mLevel = mMClass.getPath();
    }

    public String getPath()
    {
        return mLevel + ":" + mSystemName;
    }

    public String getRetsId()
    {
        return mSystemName;
    }

    public String getTableName()
    {
        return TABLE;
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("id", getId())
            .append("system name", mSystemName)
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof Table))
        {
            return false;
        } 
        Table castOther = (Table) other;
        return new EqualsBuilder()
            .append(mSystemName, castOther.mSystemName)
            .isEquals();
    }

    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(mSystemName)
            .toHashCode();
    }

    public Object accept(MetadataVisitor visitor)
    {
        return visitor.visit(this);
    }

    /** identifier field */
    private Long mId;

    /** nullable persistent field */
    private String mSystemName;

    /** nullable persistent field */
    private TableStandardName mStandardName;

    /** nullable persistent field */
    private String mLongName;

    /** nullable persistent field */
    private String mDbName;

    /** nullable persistent field */
    private String mShortName;

    /** nullable persistent field */
    private int mMaximumLength;

    /** nullable persistent field */
    private DataTypeEnum mDataType;

    /** nullable persistent field */
    private int mPrecision;

    /** nullable persistent field */
    private boolean mSearchable;

    /** nullable persistent field */
    private InterpretationEnum mInterpretation;

    /** nullable persistent field */
    private AlignmentEnum mAlignment;

    /** nullable persistent field */
    private boolean mUseSeparator;

    /** nullable persistent field */
    private int mMaxSelect;

    /** nullable persistent field */
    private UnitEnum mUnits;

    /** nullable persistent field */
    private boolean mIndex;

    /** nullable persistent field */
    private int mMinimum;

    /** nullable persistent field */
    private int mMaximum;

    /** nullable persistent field */
    private int mDefault;

    /** nullable persistent field */
    private int mRequired;

    /** nullable persistent field */
    private boolean mUnique;
    
    /** nullable persistent field */
    private String mMetadataEntryID;
    
    /** nullable persistent field */
    private boolean mModTimeStamp;
    
    /** nullable persistent field */
    private String mForeignKeyName;
    
    /** nullable persistent field */
    private String mForeignField;
    
    /** nullable persistent field */
    private boolean mKeyQuery;
    
    /** nullable persistent field */
    private boolean mKeySelect;
    
    /** nullable persistent field */
    private boolean mInKeyIndex;


    /** nullable persistent field */
    private MClass mMClass;

    /** nullable persistent field */
    private Set mEditMasks;

    /** nullable persistent field */
    private Lookup mLookup;

    /** nullable persistent field */
    private SearchHelp mSearchHelp;

    private String mLevel;

    public static final String TABLE = "TABLE";
}
