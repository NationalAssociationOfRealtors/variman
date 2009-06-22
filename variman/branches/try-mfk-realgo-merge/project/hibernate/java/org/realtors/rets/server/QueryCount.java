package org.realtors.rets.server;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class QueryCount
{
    /**
     * Creates a new query count with no limit.
     */
    public QueryCount()
    {
        init();
        setNoQueryLimit();
    }

    /**
     * Creates a new query count with the specified limit.
     *
     * @param limit Limit count
     * @param limitPeriod Time period
     */
    public QueryCount(QueryLimit queryLimit)
    {
        init();
        setLimit(queryLimit);
    }

    private void init()
    {
        mLastResetTime = System.currentTimeMillis();
        mQueryCount = 0;
    }

    public void setLimit(QueryLimit queryLimit)
    {
        if (queryLimit == null) {
            throw new NullPointerException("queryLimit is null.");
        }
        QueryLimit.Period period = queryLimit.getPeriod();
        if (period.equals(QueryLimit.Period.PER_MINUTE))
        {
            mResetPeriod = DateUtils.MILLIS_PER_MINUTE;
        }
        else if (period.equals(QueryLimit.Period.PER_HOUR))
        {
            mResetPeriod = DateUtils.MILLIS_PER_HOUR;
        }
        else if (period.equals(QueryLimit.Period.PER_DAY))
        {
            mResetPeriod = DateUtils.MILLIS_PER_DAY;
        }
        else
        {
            throw new IllegalArgumentException("Invalid limit period: " +
                    period);
        }
        mQueryLimit = queryLimit;
    }

    public void setNoQueryLimit()
    {
        mQueryLimit = QueryLimit.NO_QUERY_LIMIT;
        mResetPeriod = 0;
    }

    public boolean isNoQueryLimit()
    {
        return mQueryLimit.hasNoQueryLimit();
    }

    public long getLimit()
    {
        return mQueryLimit.getLimit();
    }

    public QueryLimit.Period getLimitPeriod()
    {
        return mQueryLimit.getPeriod();
    }

    protected long getCurrentCount()
    {
        return mQueryCount;
    }

    public boolean increment()
    {
        long timeSinceLastReset = System.currentTimeMillis() - mLastResetTime;
        // Must be '>=' so that a reset period of 0 causes a reset on
        // every increment.
        if (timeSinceLastReset >= mResetPeriod)
        {
            mQueryCount = 0;
            mLastResetTime = System.currentTimeMillis();
        }

        if (mQueryCount >= getLimit())
            return false;

        mQueryCount++;
        return true;
    }

    public void setLastResetTime(long time)
    {
        mLastResetTime = time;
    }

    public boolean isMoreRestrictiveThan(QueryCount queryCount)
    {
        // If we're no limit, everything is more restrictive
        if (isNoQueryLimit())
            return false;
        // If the other is no limit, and we're not, the it's more restrictive
        else if (queryCount.isNoQueryLimit())
            return true;

        // Neither are "no limit", so check the reset period
        if (queryCount.mResetPeriod < mResetPeriod)
            return false;
        else if (queryCount.mResetPeriod > mResetPeriod)
            return true;

        // Reset periods are equal, check the limit
        if (queryCount.getLimit() <= getLimit())
            return false;

        return true;
    }

    public String dump()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("limit", mQueryLimit)
            .append("queryCount", mQueryCount)
            .append("resetPeriod", mResetPeriod)
            .append("lastResetTime", mLastResetTime)
            .toString();
    }

    private QueryLimit mQueryLimit;
    private long mResetPeriod;
    private long mQueryCount;
    private long mLastResetTime;
}
