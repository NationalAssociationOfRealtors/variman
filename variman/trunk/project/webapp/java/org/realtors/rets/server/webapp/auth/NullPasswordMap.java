/*
 */
package org.realtors.rets.server.webapp.auth;

public class NullPasswordMap implements PasswordMap
{
    public String getPassword(String username)
    {
        return null;
    }
}
