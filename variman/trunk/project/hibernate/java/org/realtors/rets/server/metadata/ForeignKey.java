package org.realtors.rets.server.metadata;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_metadata_foreignkey"
 */
public class ForeignKey implements Serializable
{
    /**
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
     * @return
     *
     * @hibernate.property
     * @hibernate.column name="foreign_key_id"
     *   not-null="true"
     *   unique="true"
     *   index="rets_metadata_foreignkey_id_index"
     *   length="32"
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
     * @return
     *
     * @hibernate.many-to-one
     */
    public MSystem getSystemid()
    {
        return mSystemid;
    }

    public void setSystemid(MSystem systemid)
    {
        mSystemid = systemid;
    }

    /**
     * @return
     *
     * @hibernate.many-to-one
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
     * @return
     *
     * @hibernate.many-to-one
     */
    public Table getChildTable()
    {
        return mChildTable;
    }

    public void setChildTable(Table childTable)
    {
        mChildTable = childTable;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof ForeignKey)) return false;
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

    /** identifier field */
    private Long mId;

    /** nullable persistent field */
    private String mForeignKeyID;

    /** nullable persistent field */
    private MSystem mSystemid;

    /** nullable persistent field */
    private Table mParentTable;

    /** nullable persistent field */
    private Table mChildTable;

}
