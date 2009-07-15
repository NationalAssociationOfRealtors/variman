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

public class NullUserMap implements UserMap
{
    public User findUser(String username)
    {
        return null;
    }
}
