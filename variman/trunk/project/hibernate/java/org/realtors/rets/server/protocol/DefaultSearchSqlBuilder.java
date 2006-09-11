package org.realtors.rets.server.protocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

import org.apache.commons.lang.StringUtils;

import org.realtors.rets.server.ReplyCode;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.ServerDmqlMetadata;
import org.realtors.rets.server.metadata.ServerMetadata;

public class DefaultSearchSqlBuilder implements SearchSqlBuilder
{
    public void setParameters(SearchParameters parameters)
    {
        mParameters = parameters;
    }

    public void setGroups(SortedSet groups)
    {
        mGroups = groups;
    }

    public void perpareForQuery(MetadataManager manager)
        throws RetsReplyException
    {
       String resourceName = mParameters.getResourceId();
        ServerMetadata resource =
            manager.findByPath(Resource.TABLE, resourceName);
        if (resource == null)
        {
            throw new RetsReplyException(ReplyCode.MISC_SEARCH_ERROR,
                                        "Invalid resource: " + resourceName);
        }

        String className = mParameters.getClassName();
        mClass = (MClass) manager.findByPath(
            MClass.TABLE, resourceName + ":" + className);
        if (mClass == null)
        {
            throw new RetsReplyException(ReplyCode.MISC_SEARCH_ERROR,
                                         "Invalid class: " + className);
        }

        TableGroupFilter groupFilter = RetsServer.getTableGroupFilter();
        mTables = groupFilter.findTables(mGroups, resourceName, className);
        mMetadata = new ServerDmqlMetadata(mTables,
                                           mParameters.isStandardNames());
    }

    public MClass getMetadataClass()
    {
        return mClass;
    }

    public List getColumns()
        throws RetsReplyException
    {
        String[] fields = mParameters.getSelect();
        if (fields == null)
        {
            return mMetadata.getAllColumns();
        }
        else
        {
            List columns = new ArrayList();
            for (int i = 0; i < fields.length; i++)
            {
                String column = mMetadata.fieldToColumn(fields[i]);
                if (column == null)
                {
                    throw new RetsReplyException(ReplyCode.INVALID_SELECT,
                                                 fields[i]);
                }
                columns.add(column);
            }
            return columns;
        }
    }

    public ServerDmqlMetadata getMetadata()
    {
        return mMetadata;
    }

    public String getSelectClause() throws RetsReplyException
    {
        return StringUtils.join(getColumns().iterator(), ",");
    }

    public String getFromClause()
    {
        return mClass.getDbTable();
    }

    private MClass mClass;
    private Collection mTables;
    private ServerDmqlMetadata mMetadata;
    private SearchParameters mParameters;
    private SortedSet mGroups;
}
