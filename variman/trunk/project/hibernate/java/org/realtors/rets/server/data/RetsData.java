package org.realtors.rets.server.data;

import java.io.Serializable;
import java.util.SortedMap;

import org.realtors.rets.server.metadata.MClass;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_data"
 */
public class RetsData implements Serializable
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
     * @hibernate.many-to-one
     */
    public MClass getClazz()
    {
        return mClazz;
    }

    public void setClazz(MClass clazz)
    {
        mClazz = clazz;
    }

    /**
     *
     * @return
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
        if (!(other instanceof RetsData)) return false;
        RetsData castOther = (RetsData) other;
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
    private MClass mClazz;

    /** persistent field */
    private SortedMap mDataElements;
}
