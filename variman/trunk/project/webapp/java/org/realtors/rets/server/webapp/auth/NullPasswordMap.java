/*
 */
package org.realtors.rets.server.webapp.auth;

public class NullPasswordMap implements PasswordMap
{
    public boolean passwordIsA1()
    {
        return false;
    }

    public PasswordInfo getPassword(String username)
    {
        return new PasswordInfo();
    }

}
