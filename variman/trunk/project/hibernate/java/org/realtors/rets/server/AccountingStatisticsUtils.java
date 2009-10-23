/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import org.apache.log4j.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class AccountingStatisticsUtils
{
    public static AccountingStatistics findByUser(User user)
        throws RetsServerException, HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            AccountingStatistics statistics = findByUser(user, helper);
            return statistics;
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
