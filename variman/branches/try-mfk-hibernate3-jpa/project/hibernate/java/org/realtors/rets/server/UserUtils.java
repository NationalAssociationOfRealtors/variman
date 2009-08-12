/*
 * Variman RETS Server
 *
 * Author: Dave Dribin, Mark Klein
 * Copyright (c) 2004-2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class UserUtils
{
    public static User findByUsername(String username)
    {
        EntityManagerHelper helper = RetsServer.createEntityManagerHelper();
        try
        {
            return findByUsername(username, helper);
        }
        finally
        {
            helper.close(LOG);
        }
    }

    public static User findByUsername(String username, EntityManagerHelper helper)
    {
        Query query = helper.createQuery(
            " FROM User user " +
            "WHERE user.username = :username");
        query.setParameter("username", username);
        return (User) query.getSingleResult();
    }

    public static List findAll()
    {
        EntityManagerHelper helper = RetsServer.createEntityManagerHelper();
        try
        {
            return findAll(helper);
        }
        finally
        {
            helper.close(LOG);
        }
    }

    public static List findAll(EntityManagerHelper helper)
    {
        Query query = helper.createQuery(
            "  FROM User user " +
            "ORDER BY user.lastName, user.firstName");
        return query.getResultList();
    }

    public static void delete(User user) throws RetsServerException
    {
        EntityManagerHelper helper = RetsServer.createEntityManagerHelper();
        try
        {
            EntityManager entityManager = helper.beginTransaction();
            
            AccountingStatistics statistics =
                AccountingStatisticsUtils.findByUser(user, helper);
            if (statistics != null)
            {
                entityManager.remove(statistics);
            }
            entityManager.remove(user);
            helper.commit();
        }
        finally
        {
            helper.close(LOG);
        }
    }

    public static SortedSet getGroups(User user)
    {
        EntityManagerHelper helper = RetsServer.createEntityManagerHelper();
        try
        {
            SortedSet groups = user.getGroups();
            int i = groups.size(); // Force it to be loaded if set for lazy loading.
            return groups;
        }
        finally
        {
            helper.close(LOG);
        }
    }

    public static void updateGroups(User user, SortedSet groups)
        throws RetsServerException
    {
        EntityManagerHelper helper = RetsServer.createEntityManagerHelper();
        try
        {
            helper.beginTransaction();
            user.setGroups(groups);
            ORMUtils.update(user);
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
