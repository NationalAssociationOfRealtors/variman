/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_user"
 */
public class User implements Serializable
{
    public User()
    {
        mPasswordMethod = PasswordMethod.getDefaultMethod();
    }

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

    public String getName()
    {
        return mFirstName + " " + mLastName;
    }

    /**
     *
     * @return a String
     *
     * @hibernate.property length="80"
     *   not-null="true"
     */
    public String getFirstName()
    {
        return mFirstName;
    }

    public void setFirstName(String firstName)
    {
        mFirstName = firstName;
    }

    /**
     *
     * @return a String
     *
     * @hibernate.property length="80"
     *   not-null="true"
     */
    public String getLastName()
    {
        return mLastName;
    }

    public void setLastName(String lastName)
    {
        mLastName = lastName;
    }

    /**
     *
     * @return a String
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
     * @return a PasswordMethod object
     *
     * @hibernate.property
     *   type="org.realtors.rets.server.PasswordMethodType"
     */
    public PasswordMethod getPasswordMethod()
    {
        return mPasswordMethod;
    }

    public boolean isPasswordMethod(String method)
    {
        return mPasswordMethod.getMethod().equals(method);
    }

    /**
     * Sets the method used to hash the password for backend storage. The
     * default method is plain text, i.e. no hashing.
     *
     * @param passwordMethod
     */
    public void setPasswordMethod(PasswordMethod passwordMethod)
    {
        mPasswordMethod = passwordMethod;
    }

    /**
     * @return The hashed password
     *
     * @hibernate.property length="80"
     */
    public String getPassword()
    {
        return mPassword;
    }

    /**
     * Sets the hashed password. This should only be called by hibernate when
     * loaded from the database. To change the password use changePassword()
     * as it correctly hashes the password using the current password method.
     *
     *  @param password Hashed password
     */
    public void setPassword(String password)
    {
        mPassword = password;
    }

    /**
     * Changes this user's password. The plain text password may be hashed so
     * getPassword() may not be the same as passed in here.
     *
     * @param plainTextPassword New password, in plain text
     */
    public void changePassword(String plainTextPassword)
    {
        mPassword = mPasswordMethod.hash(mUsername, plainTextPassword);
    }

    public boolean verifyPassword(String passwordToVerify)
    {
        return mPasswordMethod.verifyPassword(mPassword, passwordToVerify);
    }

    /**
     * Returns the agent code.
     *
     * @return The agent code
     *
     * @hibernate.property length="80"
     */
    public String getAgentCode()
    {
        return mAgentCode;
    }

    public void setAgentCode(String agentCode)
    {
        mAgentCode = agentCode;
    }

    /**
     * Returns the broker code.
     *
     * @return The broker code
     *
     * @hibernate.property length="80"
     */
    public String getBrokerCode()
    {
        return mBrokerCode;
    }

    public void setBrokerCode(String brokerCode)
    {
        mBrokerCode = brokerCode;
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("id", mId)
            .append("username", mUsername)
            .append("password", mPassword)
            .append("password method", mPasswordMethod)
            .toString();
    }

    private Long mId;
    private String mFirstName;
    private String mLastName;
    private String mUsername;
    private String mPassword;
    private PasswordMethod mPasswordMethod;
    private String mAgentCode;
    private String mBrokerCode;
}
