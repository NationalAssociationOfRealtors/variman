package org.realtors.rets.server;

import java.io.Serializable;

/**
 * @hibernate.class table="rets_group"
 */
public class Group implements Serializable
{
    /**
     * Returns the group ID.
     *
     * @return the group ID.
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
     * Returns the group name.
     *
     * @return the group name.
     *
     * @hibernate.property length="32"
     *   unique="true"
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

    /**
     * Returns the description.
     *
     * @return the description.
     *
     * @hibernate.property length="80"
     */
    public String getDescription()
    {
        return mDescription;
    }

    public void setDescription(String description)
    {
        mDescription = description;
    }

    private Long mId;
    private String mName;
    private String mDescription;
}
