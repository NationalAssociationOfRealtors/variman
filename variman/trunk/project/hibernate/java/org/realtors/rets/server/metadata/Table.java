package org.realtors.rets.server.metadata;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_metadata_table"
 */
public class Table implements Serializable
{
    /**
     *
     * @return
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
     * @return
     *
     * @hibernate.property
     * @hibernate.column name="systemName"
     *   not-null="true"
     *   unique="true"
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
     * @return
     *
     * @hibernate.property
     */
    public TableStandardNameEnum getStandardName()
    {
        return mStandardName;
    }

    public void setStandardName(TableStandardNameEnum standardName)
    {
        mStandardName = standardName;
    }

    /**
     *
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
     *
     * @hibernate.many-to-one
     */
    public MClass getClassid()
    {
        return mClassid;
    }

    public void setClassid(MClass classid)
    {
        mClassid = classid;
    }

    /**
     *
     * @return
     *
     * @hibernate.many-to-one
     */
    public EditMask getEditMask()
    {
        return mEditMask;
    }

    public void setEditMask(EditMask editMask)
    {
        mEditMask = editMask;
    }

    /**
     *
     * @return
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
     * @return
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

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof Table)) return false;
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

    /** identifier field */
    private Long mId;

    /** nullable persistent field */
    private String mSystemName;

    /** nullable persistent field */
    private TableStandardNameEnum mStandardName;

    /** nullable persistent field */
    private String mLongName;

    /** nullable persistent field */
    private String mDbName;

    /** nullable persistent field */
    private String mShortName;

    /** nullable persistent field */
    private int mMaximumLength;

    /** nullable persistent field */
    private org.realtors.rets.server.metadata.DataTypeEnum mDataType;

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
    private MClass mClassid;

    /** nullable persistent field */
    private EditMask mEditMask;

    /** nullable persistent field */
    private Lookup mLookup;

    /** nullable persistent field */
    private SearchHelp mSearchHelp;
}
