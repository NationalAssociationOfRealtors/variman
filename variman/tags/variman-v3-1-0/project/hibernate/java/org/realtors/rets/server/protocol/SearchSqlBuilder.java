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
    public void setParameters(SearchParameters parameters);

    public void setGroups(SortedSet groups);

    public void prepareForQuery(MetadataManager manager)
        throws RetsReplyException;

    public MClass getMetadataClass();

    public List getColumns()
        throws RetsReplyException;

    public ServerDmqlMetadata getMetadata();

    public String getSelectClause() throws RetsReplyException;

    public String getFromClause() throws RetsReplyException;
}
