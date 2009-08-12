/*
 * Variman RETS Server
 *
 * Author: Dave Dribin, Mark Klein
 * Copyright (c) 2004-2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class MetadataFinder
{
    public MetadataFinder(String hqlClass, int expectedLevels)
    {
        mHqlClass = hqlClass;
        mExpectedLevels = expectedLevels;
        if (mExpectedLevels == 0)
        {
            mHql = "from " + mHqlClass;
        }
        else
        {
            mHql = "from " + mHqlClass + " as c " +
                "where c.level = :level";
        }
    }

    public List findMetadata(String[] levels, EntityManagerFactory entityManagerFactory)
        throws RetsReplyException
    {
        assertLevelLength(levels);
        String level = StringUtils.join(levels, ":");
        EntityManagerHelper helper = new EntityManagerHelper(entityManagerFactory);
        List metadata = null;
        try
        {
            EntityManager entityManager = helper.beginTransaction();
            Query query = entityManager.createQuery(mHql);
            if (mExpectedLevels > 0)
            {
                query.setParameter("level", level);
            }
            metadata = query.getResultList();
        }
        catch (Exception e)
        {
            LOG.warn("Caught", e);
            helper.rollback(LOG);
        }
        finally
        {
            helper.close(LOG);
        }
        return metadata;
    }

    private void assertLevelLength(String[] levels)
        throws RetsReplyException
    {
        if (levels.length != mExpectedLevels)
        {
            LOG.warn("Invalid levels length, expected " + mExpectedLevels +
                     ", was " + levels.length);
            throw new RetsReplyException(ReplyCode.INVALID_IDENTIFIER);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(MetadataFinder.class);
    private String mHqlClass;
    private int mExpectedLevels;
    private String mHql;
}
