/*
 */
package org.realtors.rets.server.webapp.auth;

import org.realtors.rets.server.User;

public interface UserMap
{
    /**
     * Finds a user object matching a username. <code>null</code> is returned
     * if no matching user is found. If the username is <code>null</code>, a
     * <code>null</code> <b>must</b> be returned.
     *
     * @param username The username to find
     * @return The matching user object, or <code>null</code>
     */
    User findUser(String username);
}
