package org.realtors.rets.server;

import java.io.Serializable;
import java.util.SortedSet;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_group"
 */
public class Group implements Serializable, Comparable
{
    public Group(String name)
    {
        mName = name;
    }

    protected Group()
    {
        // Empty
    }

    /**
     * Returns the group ID.
     *
     * @return the group ID.
     *
     * @hibernate.id generator-class="native"
     */
    protected Long getId()
    {
        return mId;
    }

    protected void setId(Long id)
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

    protected void setName(String name)
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

    /**
     * @hibernate.set table="rets_user_groups" lazy="true"
     *   sort="natural" inverse="true"
     * @hibernate.key column="group_id"
     * @hibernate.many-to-many column="user_id"
     *   class="org.realtors.rets.server.User"
     */
    protected SortedSet getUsers()
    {
        return mUsers;
    }

    protected void setUsers(SortedSet users)
    {
        mUsers = users;
    }

    public String toString()
    {
        return mName;
    }

    public String dump()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("id", mId)
            .append("name", mName)
            .append("description", mDescription)
            .toString();
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof Group))
        {
            return false;
        }
        Group rhs = (Group) obj;
        return new EqualsBuilder()
            .append(mName, rhs.mName)
            .isEquals();
    }

    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(mName)
            .toHashCode();
    }

    public int compareTo(Object obj)
    {
        Group rhs = (Group) obj;
        return new CompareToBuilder()
            .append(mName, rhs.mName)
            .toComparison();
    }

    private Long mId;
    private String mName;
    private String mDescription;
    private SortedSet mUsers;
}
