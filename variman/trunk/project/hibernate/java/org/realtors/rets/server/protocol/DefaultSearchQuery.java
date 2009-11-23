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

import org.realtors.rets.server.dmql.DmqlParserMetadata;
import org.realtors.rets.server.protocol.SqlStatements.SearchQuery;

/**
 * Default implementation of the {@link SearchQuery} interface. Extends the
 * {@link DefaultQuery} class. Implemented as an immutable object.
 */
public class DefaultSearchQuery extends DefaultQuery implements SearchQuery
{
    private List/*String*/ mSelectedColumnNames;
    private DmqlParserMetadata mDmqlParserMetadata;
    
    public DefaultSearchQuery(final String searchSql, final List/*String*/ selectedColumnNames, final DmqlParserMetadata dmqlParserMetadata)
    {
        super(searchSql);
        if (selectedColumnNames == null || selectedColumnNames.isEmpty()) {
            throw new IllegalArgumentException("DMQL parser metadata must not be null or empty.");
        }
        mSelectedColumnNames = selectedColumnNames;
        if (dmqlParserMetadata == null) {
            throw new NullPointerException("DMQL parser metadata must not be null.");
        }
        mDmqlParserMetadata = dmqlParserMetadata;
    }

    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder.SqlStatements.SearchQuery#getSelectedColumnNames()
     */
    public List/*String*/ getSelectedColumnNames() {
        return mSelectedColumnNames;
    }
    
    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder.SqlStatements.SearchQuery#getFieldNameToColumnNameMap()
     */
    public DmqlParserMetadata getDmqlParserMetadata() {
        return mDmqlParserMetadata;
    }

}
