/*
 */
package org.realtors.rets.server;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @hibernate.class table="rets_user"
 */
public class User
{
    public User()
    {
        mPasswordMethod = PasswordMethod.getInstance("A1",  REALM);
    }
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
     * @hibernate.property
     *   type="org.realtors.rets.server.PasswordMethodType"
     */

    public PasswordMethod getPasswordMethod()
    {
        return mPasswordMethod;
    }

    public void setPasswordMethod(PasswordMethod passwordMethod)
    {
        mPasswordMethod = passwordMethod;
    }

    /**
     * Sets the plain text password for this user. The plain text password is
     * not persisted into the database, only the hashed password. The hashed
     * password is in the same format as A1 as described in the HTTP Digest
     * Authentcation specification:
     *
     *   A1 = MD5(username ":" realm ":" password).
     *
     * @see #REALM
     *
     * @hibernate.property length="80"
     */

    public String getPassword()
    {
        return mPassword;
    }

    protected void setPassword(String password)
    {
        mPassword = password;
    }

    public void changePassword(String plainTextPassword)
    {
        mPassword = mPasswordMethod.hash(mUsername, plainTextPassword);
    }

    public boolean verifyPassword(String passwordToVerify)
    {
        return mPasswordMethod.verifyPassword(mPassword, passwordToVerify);
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
    private String mUsername;
    private String mPassword;

    /** The realm used for A1 hashed password generation: "RETS Server". */
    private static final String REALM = "RETS Server";
    private PasswordMethod mPasswordMethod;
}
