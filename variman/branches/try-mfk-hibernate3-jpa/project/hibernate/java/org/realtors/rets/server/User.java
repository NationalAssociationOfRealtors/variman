/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

import java.io.Serializable;
import java.util.SortedSet;

import javax.persistence.*;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Type;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *  This is hibernate specific. Most JPA providers also have such an
 *  extension, so if the JPA provider is switched from hibernate, this
 *  and the Type definition below will need to change.
 */
@TypeDef(name="PasswordMethodType", typeClass=PasswordMethodType.class)

@Entity
@Table(name = "rets-user")
public class User implements Serializable, Comparable
{
    @Id @GeneratedValue
    @Column(name = "id")
    private Long mId;
    
    @Column(name = "firstname",length=80,nullable=false)
    private String mFirstName;
    
    @Column(name = "lastname",length=80,nullable=false)
    private String mLastName;
    
    @Column(name = "username",length=32,nullable=false,unique=true)
    private String mUsername;
    
    @Column(name = "password",length=80)
    private String mPassword;
    
    @Column(name = "passwordmethod")
    @Type(type="PasswordMethodType")
    private PasswordMethod mPasswordMethod;
    
    @Column(name = "agentcode",length=80)
    private String mAgentCode;
    
    @Column(name="brokercode",length=80)
    private String mBrokerCode;

    @ManyToMany(
            targetEntity=Group.class,
            fetch=FetchType.LAZY)
    @JoinTable(
            name = "rets_user_groups", 
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns=@JoinColumn(name="group_id"))
    @Sort(type = SortType.NATURAL)
    private SortedSet mGroups;
    
    public User()
    {
        mPasswordMethod = PasswordMethod.getDefaultMethod();
    }

    /**
     *
     * @return a Long object
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
     */
    public String getBrokerCode()
    {
        return mBrokerCode;
    }

    public void setBrokerCode(String brokerCode)
    {
        mBrokerCode = brokerCode;
    }

    protected SortedSet getGroups()
    {
        return mGroups;
    }

    protected void setGroups(SortedSet groups)
    {
        mGroups = groups;
    }

    public String toString()
    {
        return getName();    
    }

    public String dump()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("id", mId)
            .append("username", mUsername)
            .append("password", mPassword)
            .append("password method", mPasswordMethod)
            .toString();
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof User))
        {
            return false;
        }
        User rhs = (User) obj;
        return new EqualsBuilder()
            .append(mLastName, rhs.mLastName)
            .append(mFirstName, rhs.mFirstName)
            .append(mUsername, rhs.mUsername)
            .isEquals();
    }

    public int hashCode()
    {
        return new HashCodeBuilder()
            .append(mUsername)
            .toHashCode();
    }

    public int compareTo(Object obj)
    {
        User rhs = (User) obj;
        return new CompareToBuilder()
            .append(mLastName, rhs.mLastName)
            .append(mFirstName, rhs.mFirstName)
            .append(mUsername, rhs.mUsername)
            .toComparison();
    }


}
