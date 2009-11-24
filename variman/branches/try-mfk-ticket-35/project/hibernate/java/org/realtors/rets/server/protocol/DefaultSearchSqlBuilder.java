package org.realtors.rets.server.protocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

import org.apache.commons.lang.StringUtils;

import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.server.Group;
import org.realtors.rets.server.ReplyCode;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.ServerDmqlMetadata;

public class DefaultSearchSqlBuilder implements SearchSqlBuilder
{
    public void setParameters(SearchParameters parameters)
    {
        mParameters = parameters;
    }

    public void setGroups(SortedSet<Group> groups)
    {
        mGroups = groups;
    }

    public void prepareForQuery(MetadataManager manager)
        throws RetsReplyException
    {
        String resourceId = mParameters.getResourceId();
        MetaObject resource = manager.findByPath(MetadataType.RESOURCE.name(), resourceId);
        if (resource == null)
        {
            throw new RetsReplyException(ReplyCode.MISC_SEARCH_ERROR,
                                        "Invalid resource: " + resourceId);
        }

        String className = mParameters.getClassName();
        mClass = (MClass) manager.findByPath(
                MetadataType.CLASS.name(), resourceId + ":" + className);
        if (mClass == null)
        {
            throw new RetsReplyException(ReplyCode.MISC_SEARCH_ERROR,
                                         "Invalid class: " + className);
        }

        TableGroupFilter groupFilter = RetsServer.getTableGroupFilter();
        mTables = groupFilter.findTables(mGroups, resourceId, className);
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
        return mClass.getXDBName();
    }

    private MClass mClass;
    private Collection<MTable> mTables;
    private ServerDmqlMetadata mMetadata;
    private SearchParameters mParameters;
    private SortedSet<Group> mGroups;
}
