/*
 * Variman RETS Server
 *
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.protocol;

import java.util.List;

import org.realtors.rets.server.dmql.DmqlParserMetadata;

/**
 * Contains the sql statements created by a SearchSqlBuilder implementation.
 */
public interface SqlStatements
{
    public Query getCountQuery();

    public SearchQuery getSearchQuery();

    public interface Query
    {
        public String getSql();
    }

    public interface SearchQuery extends Query
    {
        public List<String> getSelectedColumnNames();
        public DmqlParserMetadata getDmqlParserMetadata();
    }

}
