package org.realtors.rets.server.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.realtors.rets.server.metadata.Table;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_history_data_dataelement"
 */
public class RetsHistoryDataElement implements Serializable
{
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
     * @return a Boolean object
     *
     * @hibernate.property
     */
    public Boolean getBooleanValue()
    {
        return mBooleanValue;
    }

    public void setBooleanValue(Boolean booleanValue)
    {
        mBooleanValue = booleanValue;
    }

    /**
     *
     * @return a String object
     *
     * @hibernate.property
     */
    public String getCharacterValue()
    {
        return mCharacterValue;
    }

    public void setCharacterValue(String characterValue)
    {
        mCharacterValue = characterValue;
    }

    /**
     *
     * @return a date object
     * 
     * @hibernate.property type="date"
     */
    public Date getDateValue()
    {
        return mDateValue;
    }

    public void setDateValue(Date dateValue)
    {
        mDateValue = dateValue;
    }

    /**
     *
     * @return a Date object
     *
     * @hibernate.property type="timestamp"
     */
    public Date getDateTimeValue()
    {
        return mDateTimeValue;
    }

    public void setDateTimeValue(Date dateTimeValue)
    {
        mDateTimeValue = dateTimeValue;
    }

    /**
     *
     * @return a Date
     *
     * @hibernate.property type="time"
     */
    public Date getTimeValue()
    {
        return mTimeValue;
    }

    public void setTimeValue(Date timeValue)
    {
        mTimeValue = timeValue;
    }

    /**
     *
     * @return a Long object
     *
     * @hibernate.property
     */
    public Long getIntValue()
    {
        return mIntValue;
    }

    public void setIntValue(Long intValue)
    {
        mIntValue = intValue;
    }

    /**
     *
     * @return a BigDecimal object
     *
     * @hibernate.property type="big_decimal"
     */
    public BigDecimal getDecimalValue()
    {
        return mDecimalValue;
    }

    public void setDecimalValue(BigDecimal decimalValue)
    {
        mDecimalValue = decimalValue;
    }

    /**
     *
     * @return a RetsHistoryData object
     *
     * @hibernate.many-to-one
     */
    public RetsHistoryData getData()
    {
        return mData;
    }

    public void setData(RetsHistoryData data)
    {
        mData = data;
    }

    /**
     *
     * @return a Table object
     *
     * @hibernate.many-to-one
     * @hibernate.column name="key"
     *   not-null="true"
     *   unique="false"
     *   index="rets_history_dataelement_index"
     */
    public Table getKey()
    {
        return mKey;
    }

    public void setKey(Table key)
    {
        mKey = key;
    }

    /**
     *
     * @return a RetsHistoryMultiSet object
     *
     * @hibernate.many-to-one
     */
    public RetsHistoryMultiSet getMulti()
    {
        return mMulti;
    }

    public void setMulti(RetsHistoryMultiSet multi)
    {
        mMulti = multi;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof RetsHistoryDataElement)) return false;
        RetsHistoryDataElement castOther = (RetsHistoryDataElement) other;
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
    private Boolean mBooleanValue;

    /** nullable persistent field */
    private String mCharacterValue;

    /** nullable persistent field */
    private Date mDateValue;

    /** nullable persistent field */
    private Date mDateTimeValue;

    /** nullable persistent field */
    private Date mTimeValue;

    /** nullable persistent field */
    private Long mIntValue;

    /** nullable persistent field */
    private BigDecimal mDecimalValue;

    /** nullable persistent field */
    private RetsHistoryData mData;

    /** persistent field */
    private Table mKey;

    /** nullable persistent field */
    private RetsHistoryMultiSet mMulti;
}
