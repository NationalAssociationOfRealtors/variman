/*
 */
package org.realtors.rets.server.webapp.auth;

public class NullPasswordMap implements PasswordMap
{
    public boolean passwordIsA1()
    {
        return false;
    }

    public String getPassword(String username)
    {
        return null;
    }
}
