/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2007, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.enums.Enum;

/**
 * The <code>QueryLimit</code> the query-limit value and its period into one
 * object.
 * <p>
 * Designed to be immutable.</p>
 * 
 * @author Danny Hurlburt
 */
public class QueryLimit
{
    /*
     * Constructor used by the valueOf factory method.
     */
    private QueryLimit(final long limit, final Period period)
    {
        setLimit(limit);
        setPeriod(period);
    }
    
    /**
     * Creates or reuses pre-existing query-limits.
     * 
     * @param limit the maximum number of queries allowed to be made per
     *        specified period. Must be greater than or equal to zero.
     * @param period the time period the specified maximum number of queries
     *        pertain to. Must be one of the pre-defined periods defined in
     *        {@link QueryLimit.Period}.
     * @return a query-limit object representing the specified parameters.
     *         Never returns <code>null</code>.
     */
    public static QueryLimit valueOf(final long limit, final Period period)
    {
        if (limit == UNLIMITED || period == Period.NO_LIMIT) {
            return NO_QUERY_LIMIT;
        }
        return new QueryLimit(limit, period);
    }
    
    /**
     * Returns the maximum number of queries allowed per period. If this
     * query-limit is unlimited, returns {@link #UNLIMITED}.
     * 
     * @return the maximum number of queries allowed per period.
     */
    public long getLimit()
    {
        return mLimit;
    }
    
    private void setLimit(final long limit)
    {
        if (limit < 0) {
            throw new IllegalArgumentException("limit must be greater than or equal to zero.");
        }
        if (limit == UNLIMITED) {
            setNoLimit();
        } else {
            mLimit = limit;
        }
    }
    
    /**
     * Returns the time period the limit on the queries pertains to. Will
     * return one of the pre-defined periods from
     * <code>QueryLimit.Period</code>.
     *  
     * @return the time period the limit on the queries pertains to.
     */
    public Period getPeriod()
    {
        return mPeriod;
    }
    
    private void setPeriod(final Period period)
    {
        if (period == Period.NO_LIMIT) {
            setNoLimit();
        } else {
            List enumList = Period.getEnumList();
            if (!enumList.contains(period)) {
                throw new IllegalArgumentException("period must be one of the pre-defined periods. See QueryLimit.Period.");
            }
            mPeriod = period;
        }
    }
    
    protected void setNoLimit()
    {
        mLimit = UNLIMITED;
        mPeriod = Period.NO_LIMIT;
    }
    
    /**
     * Determines whether there is not limit on the number of queries.
     * 
     * @return <code>true</code> if there is not limit, otherwise
     *         <code>false</code>.
     */
    public boolean hasNoQueryLimit()
    {
        return (mLimit == UNLIMITED) || (mPeriod == Period.NO_LIMIT);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
        .append("limit", mLimit)
        .append("limitPeriod", mPeriod)
        .toString();
    }
    
    /**
     * An enumeration of valid query-limit periods.
     * <p>
     * <strong>WARNING:</strong> use <code>equals()</code> instead of
     * <code>==</code> if this class may be loaded by multiple classloaders.
     * </p>
     */
    public static final class Period extends Enum
    {
        public static final Period NO_LIMIT = null;
        public static final Period PER_MINUTE = new Period("per minute");
        public static final Period PER_HOUR = new Period("per hour");
        public static final Period PER_DAY = new Period("per day");
        
        Period(String period)
        {
            super(period);
        }
        
        /**
         * Returns a list of all periods in this enumeration.
         * 
         * @return a list of all periods in this enumeration.
         */
        public static List/*Period*/ getEnumList()
        {
            return getEnumList(Period.class);
        }

    }
    
    /**
     * The value to use to signal an unlimited query.
     */
    public static final long UNLIMITED = Long.MAX_VALUE;
    
    /**
     * A pre-defined query-limit that represents no limit on the number of
     * queries.
     */
    public static final QueryLimit NO_QUERY_LIMIT = new QueryLimit(UNLIMITED, Period.NO_LIMIT);

    private long mLimit;
    private Period mPeriod;
    
}
