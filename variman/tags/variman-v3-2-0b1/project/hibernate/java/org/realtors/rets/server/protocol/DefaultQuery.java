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

import org.apache.commons.lang.StringUtils;
import org.realtors.rets.server.protocol.SqlStatements.Query;

/**
 * Default implementation of the {@link Query} interface. Implemented as an
 * immutable object.
 */
public class DefaultQuery implements Query
{
    private String mSql;
    
    public DefaultQuery(final String sql)
    {
        if (StringUtils.isBlank(sql)) {
            throw new IllegalArgumentException("Query SQL must not be null or empty.");
        }
        mSql = sql;
    }

    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder.SqlStatements.Query#getSql()
     */
    public String getSql() {
        return mSql;
    }
    
}
