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

import javax.persistence.Query;

import org.apache.log4j.Logger;

public class GroupUtils
{
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
            "  FROM Group aGroup " +
            "ORDER BY aGroup.name");
        return query.getResultList();
    }

    public static Group findByName(String name)
    {
        EntityManagerHelper helper = RetsServer.createEntityManagerHelper();
        try
        {
            return findByName(name, helper);
        }
        finally
        {
            helper.close(LOG);
        }
    }

    public static Group findByName(String name, EntityManagerHelper helper)
    {
        Query query = helper.createQuery(
            " From Group aGroup " +
            "WHERE aGroup.name = :name");
        query.setParameter("name", name);
        return (Group) query.getSingleResult();
    }

    public static SortedSet getUsers(Group group)
    {
        EntityManagerHelper helper = RetsServer.createEntityManagerHelper();
        try
        {
            SortedSet users = group.getUsers();
            int i = users.size(); // Force initialization for the collection if it is lazy.
            return users;
        }
        finally
        {
            helper.close(LOG);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(GroupUtils.class);
}
