/*
 */
package org.realtors.rets.server.webapp;

import java.util.List;

import net.sf.hibernate.Session;
import net.sf.hibernate.Query;
import net.sf.hibernate.HibernateException;

import org.realtors.rets.server.SessionHelper;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

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

    public List findMetadata(String[] levels)
        throws RetsReplyException
    {
        assertLevelLength(levels);
        String level = StringUtils.join(levels, ":");
        SessionHelper helper = InitServlet.createHelper();
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
            throw new RetsReplyException(20502, "Invalid Identifier");
        }
    }

    private static final Logger LOG =
        Logger.getLogger(MetadataFinder.class);
    private String mHqlClass;
    private int mExpectedLevels;
    private String mHql;
}
