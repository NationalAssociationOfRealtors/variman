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

import org.hibernate.HibernateException;

import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.User;
import org.realtors.rets.server.UserUtils;

public class HibernateUserMap implements UserMap
{
    public User findUser(String username)
        throws RetsServerException
    {
        try
        {
            return UserUtils.findByUsername(username);
        }
        catch (HibernateException e)
        {
            throw new RetsServerException(e);
        }
    }
}
