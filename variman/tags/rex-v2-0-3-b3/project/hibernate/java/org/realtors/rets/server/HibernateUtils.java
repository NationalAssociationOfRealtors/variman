/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;

public class HibernateUtils
{
    public static Long save(Object object)
        throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            Session session = helper.beginTransaction();
            Long id = (Long) session.save(object);
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

    public static void update(Object object)
        throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            Session session = helper.beginTransaction();
            session.update(object);
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
        Logger.getLogger(HibernateUtils.class);
}
