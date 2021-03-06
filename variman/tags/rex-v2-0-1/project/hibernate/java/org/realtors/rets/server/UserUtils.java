/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.LockMode;

import org.apache.log4j.Logger;

public class UserUtils
{
    public static User findByUsername(String username)
        throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            return findByUsername(username, helper);
        }
        finally
        {
            helper.close(LOG);
        }
    }

    public static User findByUsername(String username, SessionHelper helper)
        throws HibernateException
    {
        Query query = helper.createQuery(
            " FROM User user " +
            "WHERE user.username = :username");
        query.setString("username", username);
        return (User) query.uniqueResult();
    }

    public static List findAll() throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            return findAll(helper);
        }
        finally
        {
            helper.close(LOG);
        }
    }

    public static List findAll(SessionHelper helper) throws HibernateException
    {
        Query query = helper.createQuery(
            "  FROM User user " +
            "ORDER BY user.lastName, user.firstName");
        return query.list();
    }

    public static void delete(User user) throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            Session session = helper.beginTransaction();
            // Must reassociate this object with this session, otherwise the
            // User object from the statistics gets loaded. This causes two
            // User objects with the same ID to be loaded into the same
            // session, which pisses off Hibernate.
            LOG.debug("Reassociating user");
            session.lock(user, LockMode.NONE);
            LOG.debug("Getting accounting statistics");
            AccountingStatistics statistics =
                AccountingStatisticsUtils.findByUser(user, helper);
            if (statistics != null)
            {
                session.delete(statistics);
            }
            session.delete(user);
            helper.commit();
        }
        catch (HibernateException e)
        {
            helper.rollback(LOG);
            throw e;
        }
        finally
        {
            helper.close(LOG);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(UserUtils.class);
}
