package org.realtors.rets.server;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.realtors.rets.server.config.GroupRules;

public class QueryCountTable
{
    public QueryCountTable()
    {
        mQueryCountByUser = new HashMap();
        mCacheHit = 0;
    }

    public QueryCount getQueryCountForUser(String username)
    {
        return getQueryCountForUser(username, null);
    }

    public synchronized QueryCount getQueryCountForUser(String username,
                                                        List allGroupRules)
    {
        QueryCount count = (QueryCount) mQueryCountByUser.get(username);
        if (count != null)
        {
            mCacheHit++;
            return count;
        }

        // Start with an unlimited query count
        count = new QueryCount();
        // Search for the most restrictive query count in the group rules
        if (allGroupRules != null)
        {
            for (int i = 0; i < allGroupRules.size(); i++)
            {
                GroupRules rules = (GroupRules) allGroupRules.get(i);
                if (rules.hasNoQueryLimit())
                    continue;

                QueryCount newCount =
                    new QueryCount(rules.getQueryCountLimit(),
                                   rules.getQueryCountLimitPeriod());
                if (newCount.isMoreRestrictiveThan(count))
                    count = newCount;
            }
        }

        mQueryCountByUser.put(username, count);
        return count;
    }

    protected int getCacheHit()
    {
        return mCacheHit;
    }

    private Map mQueryCountByUser;
    private int mCacheHit;
}
