/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import org.apache.log4j.Logger;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;

public class AccountingStatisticsUtils
{
    public static AccountingStatistics findByUser(User user)
        throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            AccountingStatistics statistics = findByUser(user, helper);
            return statistics;
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

    public static AccountingStatistics findByUser(User user,
                                                  SessionHelper helper)
        throws HibernateException
    {
        Query query = helper.createQuery(
            "  FROM AccountingStatistics stats" +
            " WHERE stats.user.id = :user_id");
        query.setLong("user_id", user.getId().longValue());
        return (AccountingStatistics) query.uniqueResult();
    }

    private static final Logger LOG =
        Logger.getLogger(AccountingStatisticsUtils.class);
}
