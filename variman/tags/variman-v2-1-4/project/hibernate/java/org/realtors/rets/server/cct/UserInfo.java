/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 * Created on Aug 22, 2003
 *
 */
package org.realtors.rets.server.cct;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.realtors.rets.server.User;
import org.realtors.rets.server.Util;

/**
 * @hibernate.class table="rets_cct_userinfo"
 */
public class UserInfo implements Serializable
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
     * @hibernate.set table="rets_cct_userinfo_roles"
     * @hibernate.collection-key column="userinfo_id"
     * @hibernate.collection-element column="role" type="string" length="40"
     */
    public Set getRoles()
    {
        return mRoles;
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
    
    public boolean isUserInRole(String role)
    {
        return mRoles.contains(role);
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
     */
    public void setRoles(Set set)
    {
        mRoles = set;
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
            .append("UserAgent", mUserAgent)
            .append("Roles", mRoles).toString();
    }

    private String mCompany;
    private String mEmail;
    private Long mId;
    private String mProductName;
    private String mProductVersion;
    private Set mRoles;
    private User mUser;
    private String mUserAgent;
}