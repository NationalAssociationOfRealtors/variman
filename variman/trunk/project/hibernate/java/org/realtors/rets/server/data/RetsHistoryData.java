package org.realtors.rets.server.data;

import java.io.Serializable;
import java.util.Date;
import java.util.SortedMap;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_history_data"
 */
public class RetsHistoryData implements Serializable
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
     * @return a Date object
     *
     * @hibernate.property type="date"
     */
    public Date getDateChanged()
    {
        return mDateChanged;
    }

    public void setDateChanged(Date dateChanged)
    {
        mDateChanged = dateChanged;
    }

    /**
     *
     * @return a RetsData object
     *
     * @hibernate.many-to-one
     */
    public RetsData getOriginalData()
    {
        return mOriginalData;
    }

    public void setOriginalData(RetsData originalData)
    {
        mOriginalData = originalData;
    }

    /**
     *
     * @return
     * // todo: fix xdoclet index-many-to-many
     */
    public SortedMap getDataElements()
    {
        return mDataElements;
    }

    public void setDataElements(SortedMap dataElements)
    {
        mDataElements = dataElements;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof RetsHistoryData)) return false;
        RetsHistoryData castOther = (RetsHistoryData) other;
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
    private Date mDateChanged;

    /** nullable persistent field */
    private RetsData mOriginalData;

    /** persistent field */
    private SortedMap mDataElements;
}
