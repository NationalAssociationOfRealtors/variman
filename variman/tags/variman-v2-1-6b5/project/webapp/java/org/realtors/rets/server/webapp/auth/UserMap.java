/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp.auth;

import org.realtors.rets.server.User;
import org.realtors.rets.server.RetsServerException;

public interface UserMap
{
    /**
     * Finds a user object matching a username. <code>null</code> is returned
     * if no matching user is found. If the username is <code>null</code>, a
     * <code>null</code> <b>must</b> be returned.
     *
     * @param username The username to find
     * @return The matching user object, or <code>null</code>
     * @throws RetsServerException if an error occurs
     */
    User findUser(String username)
        throws RetsServerException;
}
