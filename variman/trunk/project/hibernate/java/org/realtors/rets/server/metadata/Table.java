/*
 * Rex RETS Server
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

/**
 * @hibernate.class table="rets_metadata_table"
 */
public class Table extends ServerMetadata implements Serializable
{
    public Table(long id)
    {
        mId = new Long(id);
    }

    public Table()
    {
        mId = null;
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
     * @hibernate.column name="systemName"
     *   not-null="true"
     *   index="table_system_name_index"
     *   length="32"
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
     * @hibernate.property
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
     *
     * @hibernate.property length="32"
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
     * @hibernate.property length="10"
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
     * @hibernate.property length="24"
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
     * @hibernate.property
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
     * @hibernate.property
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
     * @hibernate.property
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
     * @hibernate.property
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
     * @hibernate.property
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
     * @hibernate.property
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
     * @hibernate.property
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
     * @hibernate.property
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
     * @hibernate.property
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
     * @return an integer
     *
     * @hibernate.property
     */
    public int getIndex()
    {
        return mIndex;
    }

    public void setIndex(int index)
    {
        mIndex = index;
    }

    /**
     *
     * @return an integer
     *
     * @hibernate.property
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
     * @hibernate.property
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
     * @hibernate.property column="r_default"
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
     * @hibernate.property
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
     * @hibernate.property column="r_unique"
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
     *
     * @return an MClass object
     *
     * @hibernate.many-to-one
     * @hibernate.column name="classid"
     *   not-null="true"
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
     * @hibernate.set inverse="false"
     *   table="rets_metadata_table_editmasks"
     * @hibernate.collection-key column="id"
     * @hibernate.collection-many-to-many column="editMask"
     *   class="org.realtors.rets.server.metadata.EditMask"
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
     * @hibernate.many-to-one
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
     * @hibernate.many-to-one
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
        mLevel = mMClass.getPath();
    }

    public String getPath()
    {
        return mLevel + ":" + mSystemName;
    }

    public String getRetsId()
    {
        return mLongName;
    }

    public String getTableName()
    {
        return TABLE;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
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
    private int mIndex;

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
