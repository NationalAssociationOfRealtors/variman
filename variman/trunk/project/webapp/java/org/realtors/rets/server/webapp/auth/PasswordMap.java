/*
 */
package org.realtors.rets.server.webapp.auth;

public interface PasswordMap
{
    /**
     * Returns <code>true</code> if the passwords returned by this
     * implementation are pre-hashed to A1. Implementing interfaces should
     * always return the same value. User's may call this method only once and
     * cache it.
     *
     * @return <code>true</code if the passwords returned are A1.
     */
    boolean passwordIsA1();

    /**
     *
     * @param username
     * @return
     */
    String getPassword(String username);
}
