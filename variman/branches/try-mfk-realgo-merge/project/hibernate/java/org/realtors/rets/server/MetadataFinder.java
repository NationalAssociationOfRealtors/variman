/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

// FIXME: This should be a static inner class of HibernateMetadataFetcher.
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

    public List findMetadata(String[] levels, SessionFactory sessions)
        throws RetsReplyException
    {
        assertLevelLength(levels);
        String level = StringUtils.join(levels, ":");
        SessionHelper helper = new SessionHelper(sessions);
        List metadata = null;
        try
        {
            Session session = helper.beginTransaction();
            Query query = session.createQuery(mHql);
            if (mExpectedLevels > 0)
            {
                query.setString("level", level);
            }
            metadata = query.list();
        }
        catch (HibernateException e)
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
