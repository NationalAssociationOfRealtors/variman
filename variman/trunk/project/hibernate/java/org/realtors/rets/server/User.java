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
     * Sets the plain text password for this user. The plain text password is
     * not persisted into the database, only the hashed password. The hashed
     * password is in the same format as A1 as described in the HTTP Digest
     * Authentcation specification:
     *
     *   A1 = MD5(username ":" realm ":" password).
     *
     * @param password Plain text password
     *
     * @see #REALM
     */
    public void setPassword(String password)
    {
        mA1Password = HashUtils.md5(mUsername + ":" + REALM + ":" +
                                    password);
    }

    /**
     * Returns the hashed password as defined by A1 in Digest Authentication.
     *
     * @return Hash A1 password
     *
     * @hibernate.property not-null="true"
     *   length="32"
     */
    public String getA1Password()
    {
        return mA1Password;
    }

    public void setA1Password(String a1Password)
    {
        mA1Password = a1Password;
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("id", mId)
            .append("username", mUsername)
            .append("A1 password", mA1Password)
            .toString();
    }

    private Long mId;
    private String mUsername;
    private String mA1Password;

    /** The realm used for A1 hashed password generation: "RETS Server". */
    private static final String REALM = "RETS Server";
}
