/*
 */
package org.realtors.rets.server.metadata;

import java.io.Serializable;

/**
 * @hibernate.class table="rets_metadata_table_standard_name"
 */
public class TableStandardName implements Serializable
{
    public TableStandardName(String name)
    {
        mName = name;
    }

    public TableStandardName()
    {
        mName = null;
    }

    /**
     * Returns this object's unique ID.
     *
     * @return the unique ID.
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
     * Returns the standard name for this object.
     *
     * @return the standard name
     *
     * @hibernate.property length="32"
     *   not-null="true"
     */
    public String getName()
    {
        return mName;
    }

    public void setName(String name)
    {
        mName = name;
    }

    public String toString()
    {
        return mName;
    }

    private String mName;
    private Long mId;
}
