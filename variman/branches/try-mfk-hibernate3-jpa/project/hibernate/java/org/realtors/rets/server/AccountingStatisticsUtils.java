/*
 * Variman RETS Server
 *
 * Author: Dave Dribin, Mark Klein
 * Copyright (c) 2004-2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import org.apache.log4j.Logger;

import javax.persistence.Query;

public class AccountingStatisticsUtils
{
    public static AccountingStatistics findByUser(User user)
        throws RetsServerException
    {
        EntityManagerHelper helper = RetsServer.createEntityManagerHelper();
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
                                                  EntityManagerHelper helper)
    {
        Query query = helper.createQuery(
            "  FROM AccountingStatistics stats" +
            " WHERE stats.user.id = :user_id");
        query.setParameter("user_id", user.getId().longValue());
        return (AccountingStatistics) query.getResultList();
    }

    private static final Logger LOG =
        Logger.getLogger(AccountingStatisticsUtils.class);
}
