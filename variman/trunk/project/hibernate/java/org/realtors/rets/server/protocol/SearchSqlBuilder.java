package org.realtors.rets.server.protocol;

import java.util.SortedSet;
import java.util.List;

import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.ServerDmqlMetadata;
import org.realtors.rets.server.RetsReplyException;

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: Sep 11, 2006
 * Time: 4:22:35 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SearchSqlBuilder
{
    void setParameters(SearchParameters parameters);

    void setGroups(SortedSet groups);

    void perpareForQuery(MetadataManager manager)
        throws RetsReplyException;

    MClass getMetadataClass();

    List getColumns()
        throws RetsReplyException;

    ServerDmqlMetadata getMetadata();

    String getSelectClause() throws RetsReplyException;

    String getFromClause();
}
