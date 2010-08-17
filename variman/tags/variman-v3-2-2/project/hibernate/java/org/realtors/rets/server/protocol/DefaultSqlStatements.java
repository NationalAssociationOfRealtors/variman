/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.protocol;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.realtors.rets.server.dmql.DmqlParserMetadata;

/**
 * Default implementation of the {@link SqlStatements} interface. This
 * implementation is a simple "container" implementation where the associated
 * objects are passed into the constructor.
 * 
 * @author Danny
 */
public class DefaultSqlStatements implements SqlStatements
{
    private Query mCountQuery;
    private SearchQuery mSearchQuery;
    
    public DefaultSqlStatements(final Query countQuery, final SearchQuery searchQuery)
    {
        if (countQuery == null) {
            throw new NullPointerException("Count query must not be null.");
        }
        mCountQuery = countQuery;
        if (searchQuery == null) {
            throw new NullPointerException("Search query must not be null.");
        }
        mSearchQuery = searchQuery;
    }
    
    public DefaultSqlStatements(final String countQuerySql, final String searchQuerySql, final List/*String*/ selectedColumnNames, final DmqlParserMetadata dmqlParserMetadata)
    {
        if (StringUtils.isBlank(countQuerySql)) {
            throw new IllegalArgumentException("Count query SQL must not be null or empty.");
        }
        mCountQuery = new DefaultQuery(countQuerySql);
        mSearchQuery = new DefaultSearchQuery(searchQuerySql, selectedColumnNames, dmqlParserMetadata);
    }

    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder.SqlStatements#getCountQuery()
     */
    public Query getCountQuery() {
        return mCountQuery;
    }

    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder.SqlStatements#getSearchQuery()
     */
    public SearchQuery getSearchQuery() {
        return mSearchQuery;
    }
    
}
