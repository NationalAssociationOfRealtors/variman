/*
 * Created on Aug 22, 2003
 *
 */
package org.realtors.rets.server.cct;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.realtors.rets.server.User;
import org.realtors.rets.server.Util;

/**
 * @hibernate.class table="rets_cct_userinfo"
 */
public class UserInfo
{
    /**
     * @hibernate.property length="80" not-null="true" 
     */
    public String getCompany()
    {
        return mCompany;
    }

    /**
     * @hibernate.property length="80" not-null="true"
     */
    public String getEmail()
    {
        return mEmail;
    }

    /**
     * @hibernate.id generator-class="native"
     */
    public Long getId()
    {
        return mId;
    }

    /**
     * @hibernate.property length="80" not-null="true" 
     */
    public String getProductName()
    {
        return mProductName;
    }

    /**
     * @hibernate.property length="80" not-null="true" 
     */
    public String getProductVersion()
    {
        return mProductVersion;
    }

    /**
     * @hibernate.many-to-one
     * @hibernate.column name="user_id" not-null="true"
     */
    public User getUser()
    {
        return mUser;
    }

    /**
     * @hibernate.property length="80" not-null="true"
     */
    public String getUserAgent()
    {
        return mUserAgent;
    }

    /**
     * 
     * @param string
     */
    public void setCompany(String string)
    {
        mCompany = string;
    }

    /**
     * 
     * @param string
     */
    public void setEmail(String string)
    {
        mEmail = string;
    }

    /**
     * 
     * @param long1
     */
    public void setId(Long long1)
    {
        mId = long1;
    }

    /**
     * 
     * @param string
     */
    public void setProductName(String string)
    {
        mProductName = string;
    }

    /**
     * 
     * @param string
     */
    public void setProductVersion(String string)
    {
        mProductVersion = string;
    }

    /**
     * 
     * @param user
     */
    public void setUser(User user)
    {
        mUser = user;
    }

    /**
     * 
     * @param string
     */
    public void setUserAgent(String string)
    {
        mUserAgent = string;
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("id", mId)
            .append("Company", mCompany)
            .append("Email", mEmail)
            .append("ProductName", mProductName)
            .append("ProductVersion", mProductVersion)
            .append("user.Username", mUser.getUsername())
            .append("UserAgent", mUserAgent).toString();
    }

    private String mCompany;
    private String mEmail;
    private Long mId;
    private String mProductName;
    private String mProductVersion;
    private User mUser;
    private String mUserAgent;
}
