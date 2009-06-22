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

import junit.framework.TestCase;

public class QueryLimitTest extends TestCase
{
    public void testNoLimit()
    {
        QueryLimit.Period anyPeriod = QueryLimit.Period.PER_DAY;
        QueryLimit queryLimit = QueryLimit.valueOf(QueryLimit.UNLIMITED, anyPeriod);
        
        assertNoLimit(queryLimit);
        
        anyPeriod = QueryLimit.Period.PER_HOUR;
        queryLimit = QueryLimit.valueOf(QueryLimit.UNLIMITED, anyPeriod);
        
        assertNoLimit(queryLimit);
        
        anyPeriod = QueryLimit.Period.PER_MINUTE;
        queryLimit = QueryLimit.valueOf(QueryLimit.UNLIMITED, anyPeriod);
        
        assertNoLimit(queryLimit);
        
        anyPeriod = QueryLimit.Period.NO_LIMIT;
        queryLimit = QueryLimit.valueOf(QueryLimit.UNLIMITED, anyPeriod);
        
        assertNoLimit(queryLimit);
        
        long anyLimit = 0;
        QueryLimit.Period noLimitPeriod = QueryLimit.Period.NO_LIMIT;
        queryLimit = QueryLimit.valueOf(anyLimit, noLimitPeriod);
        
        assertNoLimit(queryLimit);
    }
    
    public void assertNoLimit(QueryLimit queryLimit)
    {
        assertTrue(queryLimit.hasNoQueryLimit());
        
        assertTrue(QueryLimit.NO_QUERY_LIMIT == queryLimit);
        
        long limit = queryLimit.getLimit();
        assertEquals(QueryLimit.UNLIMITED, limit);
        
        QueryLimit.Period period = queryLimit.getPeriod();
        assertTrue(QueryLimit.Period.NO_LIMIT == period);
    }
    
}
