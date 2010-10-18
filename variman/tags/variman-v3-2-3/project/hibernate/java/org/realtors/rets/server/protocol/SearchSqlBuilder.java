/*
 * Variman RETS Server
 *
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.protocol;

import java.util.SortedSet;

import org.realtors.rets.server.Group;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.metadata.MetadataManager;

public interface SearchSqlBuilder
{
    // TODO - it would be better to make this totally stateless,
    // and just pass all input into the createSqlStatements method as a request object
    public void setParameters(SearchParameters parameters);

    public void setGroups(SortedSet<Group> groups);

    public void prepareForQuery(MetadataManager manager)
        throws RetsReplyException;

    public void setLimit(Integer limit);

    public SqlStatements createSqlStatements() throws RetsReplyException;
    
    public String getClassStandardName();
    
    public String getResourceStandardName();
}
