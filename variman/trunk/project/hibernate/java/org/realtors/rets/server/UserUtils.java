/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Hibernate;

import org.apache.log4j.Logger;

public class UserUtils
{
    public static Long save(User user) throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            Session session = helper.beginTransaction();
            Long id = (Long) session.save(user);
            helper.commit();
            return id;
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

    public static User findByUsername(String username)
        throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            Query query = helper.createQuery(
                " FROM User as user " +
                "WHERE user.username = :username");
            query.setString("username", username);
            return (User) query.uniqueResult();
        }
        finally
        {
            helper.close(LOG);
        }
    }

    public static void delete(User user) throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            Session session = helper.beginTransaction();
            // Must load this object into this session, otherwise the User
            // object from gets loaded. This causes two User objects with the
            // same ID to be loaded, which pisses off Hibernate.
            session.load(user, user.getId());
            session.delete(" FROM AccountingStatistics as stats " +
                           "WHERE stats.user.id = ?",
                           user.getId(), Hibernate.LONG);
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
