package org.realtors.rets.server;

import org.apache.commons.lang.enums.Enum;
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
    public QueryCount(long limit,  LimitPeriod limitPeriod)
    {
        init();
        setLimit(limit, limitPeriod);
    }

    private void init()
    {
        mLastResetTime = System.currentTimeMillis();
        mQueryCount = 0;

    }

    public void setLimit(long limit, LimitPeriod limitPeriod)
    {
        if (limitPeriod.equals(PER_MINUTE))
        {
            mResetPeriod = DateUtils.MILLIS_PER_MINUTE;
        }
        else if (limitPeriod.equals(PER_HOUR))
        {
            mResetPeriod = DateUtils.MILLIS_PER_HOUR;
        }
        else if (limitPeriod.equals(PER_DAY))
        {
            mResetPeriod = DateUtils.MILLIS_PER_DAY;
        }
        else
        {
            throw new IllegalArgumentException("Invalid LimitPeriod: " +
                                               limitPeriod);
        }
        mLimit = limit;
        mLimitPeriod = limitPeriod;
    }

    public void setNoQueryLimit()
    {
        mLimit = Long.MAX_VALUE;
        mResetPeriod = 0;
        mLimitPeriod = null;
    }

    public boolean isNoQueryLimit()
    {
        return (mLimitPeriod == null);
    }

    public long getLimit()
    {
        return mLimit;
    }

    public LimitPeriod getLimitPeriod()
    {
        return mLimitPeriod;
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

        if (mQueryCount >= mLimit)
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
        if (queryCount.mLimit <= mLimit)
            return false;

        return true;
    }

    public String dump()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("limit", mLimit)
            .append("limitPeriod", mLimitPeriod)
            .append("queryCount", mQueryCount)
            .append("resetPeriod", mResetPeriod)
            .append("lastResetTime", mLastResetTime)
            .toString();
    }

    public static final class LimitPeriod extends Enum
    {
        public LimitPeriod(String s)
        {
            super(s);
        }
    }

    public static final LimitPeriod PER_MINUTE = new LimitPeriod("per minute");
    public static final LimitPeriod PER_HOUR = new LimitPeriod("per hour");
    public static final LimitPeriod PER_DAY = new LimitPeriod("per day");

    private long mLimit;
    private LimitPeriod mLimitPeriod;
    private long mResetPeriod;
    private long mQueryCount;
    private long mLastResetTime;
}
