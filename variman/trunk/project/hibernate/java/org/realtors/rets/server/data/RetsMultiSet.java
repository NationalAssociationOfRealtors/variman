package org.realtors.rets.server.data;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_multi_data"
 */
public class RetsMultiSet implements Serializable
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
     * @hibernate.column name="key"
     *   not-null="true"
     *   unique="false"
     *   index="rets_multi_data_parent_index"
     */
    public RetsDataElement getParent()
    {
        return mParent;
    }

    public void setParent(RetsDataElement parent)
    {
        mParent = parent;
    }

    /**
     *
     * @return
     *
     * @hibernate.set inverse="false"
     *   table="rets_multi_data_set"
     * @hibernate.collection-key column="id"
     * @hibernate.collection-many-to-many column="value"
     *   class="org.realtors.rets.server.metadata.LookupType"
     */
    public Set getValues()
    {
        return mValues;
    }

    public void setValues(Set values)
    {
        mValues = values;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof RetsMultiSet)) return false;
        RetsMultiSet castOther = (RetsMultiSet) other;
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
    private RetsDataElement mParent;

    /** persistent field */
    private Set mValues;
}
