/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import org.apache.log4j.Logger;

public class HibernateUtils
{
    public static Long save(Object object)
        throws HibernateException, RetsServerException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            Session session = helper.beginTransaction();
            Long id = (Long) session.save(object);
            helper.commit();
            return id;
        }
        finally
        {
            helper.close(LOG);
        }
    }

    public static void update(Object object)
        throws HibernateException, RetsServerException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            Session session = helper.beginTransaction();
            session.update(object);
            helper.commit();
        }
        finally
        {
            helper.close(LOG);
        }
    }

    public static void delete(Object object)
        throws HibernateException, RetsServerException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            Session session = helper.beginTransaction();
            session.delete(object);
            helper.commit();
        }
        finally
        {
            helper.close(LOG);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(HibernateUtils.class);
}
