/*
 */
package org.realtors.rets.server.webapp.auth;

import org.realtors.rets.server.User;

public class NullUserMap implements UserMap
{
    public User findUser(String username)
    {
        return null;
    }
}
