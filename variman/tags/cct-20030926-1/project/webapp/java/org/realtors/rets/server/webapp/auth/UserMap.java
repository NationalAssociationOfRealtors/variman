/*
 */
package org.realtors.rets.server.webapp.auth;

import org.realtors.rets.server.User;

public interface UserMap
{
    User findUser(String username);
}
