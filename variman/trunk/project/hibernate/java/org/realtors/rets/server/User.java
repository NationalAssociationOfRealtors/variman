/*
 */
package org.realtors.rets.server;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_user"
 */
public class User
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
     * @hibernate.property unique="true"
     *   not-null="true"
     *   length="32"
     */
    public String getUsername()
    {
        return mUsername;
    }

    public void setUsername(String username)
    {
        mUsername = username;
    }

    /**
     *
     * @return
     *
     * @hibernate.property not-null="true"
     *   length="32"
     */
    public String getPassword()
    {
        return mPassword;
    }

    public void setPassword(String password)
    {
        mPassword = password;
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("id", mId)
            .append("username", mUsername)
            .append("password", mPassword)
            .toString();
    }

    private Long mId;
    private String mUsername;
    private String mPassword;
}
