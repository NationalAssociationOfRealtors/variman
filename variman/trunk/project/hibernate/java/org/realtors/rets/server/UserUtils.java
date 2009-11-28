/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;

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
        
        // Added by RealGo - cache RETS users
        query.setCacheable(true);
        query.setCacheRegion("org.realtors.rets.server.User.query");
        
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
        // Does not need caching - only used by Admin
        Query query = helper.createQuery(
            "  FROM User user " +
            "ORDER BY user.lastName, user.firstName");
        return query.list();
    }

    public static void delete(User user) throws HibernateException, RetsServerException
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
        finally
        {
            helper.close(LOG);
        }
    }

    public static SortedSet getGroups(User user) throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            Session session = helper.beginSession();
            session.lock(user, LockMode.NONE);
            SortedSet groups = user.getGroups();
            Hibernate.initialize(groups);
            return groups;
        }
        finally
        {
            helper.close(LOG);
        }
    }

    public static void updateGroups(User user, SortedSet groups)
        throws HibernateException, RetsServerException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            helper.beginTransaction();
            user.setGroups(groups);
            HibernateUtils.update(user);
            helper.commit();
        }
        finally
        {
            helper.close(LOG);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(UserUtils.class);
}
