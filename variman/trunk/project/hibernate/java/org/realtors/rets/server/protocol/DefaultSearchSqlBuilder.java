/*
 * Variman RETS Server
 *
 * Copyright (c) 2004-2010, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.protocol;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.server.Group;
import org.realtors.rets.server.ReplyCode;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.dmql.DmqlCompiler;
import org.realtors.rets.server.dmql.SqlConverter;
import org.realtors.rets.server.dmql.DmqlCompiler.ParserResults;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.ServerDmqlMetadata;
import org.realtors.rets.server.protocol.SqlStatements.Query;
import org.realtors.rets.server.protocol.SqlStatements.SearchQuery;

import antlr.ANTLRException;

public class DefaultSearchSqlBuilder implements SearchSqlBuilder
{
    public void setParameters(SearchParameters parameters)
    {
        mParameters = parameters;
    }

    public void setLimit(Integer limit) {
        this.mLimit = limit;
    }

    public void setGroups(SortedSet<Group> groups)
    {
        mGroups = groups;
    }

    public void prepareForQuery(MetadataManager manager)
        throws RetsReplyException
    {
        String resourceId = mParameters.getResourceId();
        mResource = (MResource) manager.findByPath(MetadataType.RESOURCE.name(), resourceId);
        if (mResource == null)
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

        ConditionRuleSet conditionRuleSet = RetsServer.getConditionRuleSet();
        mSqlConstraint =
            conditionRuleSet.findSqlConstraint(mGroups, resourceId,
                                               className);
    }

    public SqlStatements createSqlStatements() throws RetsReplyException
    {
        Query countQuery = getCountQuery();
        SearchQuery searchQuery = getSearchQuery();

        SqlStatements sqlStatements = new DefaultSqlStatements(countQuery, searchQuery);
        return sqlStatements;
    }
    
    public String getClassStandardName()
    {
        if (mClass != null)
            return mClass.getStandardName();
        return "";
    }

    protected Query getCountQuery() throws RetsReplyException
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT COUNT(*)");
        buffer.append(" FROM ");
        buffer.append(getFromClause());
        buffer.append(" WHERE ");
        buffer.append(getWhereClause());
        String countQuerySql = buffer.toString();
        Query countQuery = new DefaultQuery(countQuerySql);
        return countQuery;
    }

    public String getResourceStandardName()
    {
        if (mResource != null)
            return mResource.getStandardName();
        return "";
    }

    protected SearchQuery getSearchQuery() throws RetsReplyException
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT ");

        List/*String*/ selectedColumnNames = getColumns();

        String selectClause = getSelectClause();
        buffer.append(selectClause);
        buffer.append(" FROM ");
        buffer.append(getFromClause());
        buffer.append(" WHERE ");
        buffer.append(getWhereClause());
        String searchQuerySql = buffer.toString();
        SearchQuery searchQuery = new DefaultSearchQuery(searchQuerySql, selectedColumnNames, mMetadata);
        return searchQuery;
    }

    protected String getWhereClause() throws RetsReplyException
    {
        StringBuffer whereClause = new StringBuffer();

        String dmqlQuery = mParameters.getQuery();
        ParserResults parserResults = parse(mMetadata);
        SqlConverter sqlConverter = parserResults.getSqlConverter();
        StringWriter stringWriter = new StringWriter();
        sqlConverter.toSql(new PrintWriter(stringWriter));
        final String dmqlQueryAsSql = stringWriter.toString();
        appendToWhereClause(whereClause, dmqlQueryAsSql);

        appendToWhereClause(whereClause, mSqlConstraint);

        return whereClause.toString();
    }

    protected void appendToWhereClause(StringBuffer whereClause, String whereClauseSegment)
    {
        if (!StringUtils.isBlank(whereClauseSegment)) {
            if (whereClause.length() > 0) {
                whereClause.insert(0, "(").append(") AND ");    
            }
            whereClause.append(whereClauseSegment);
        }
    }

    protected ParserResults parse(ServerDmqlMetadata metadata) throws RetsReplyException
    {
        try
        {
            if (mParameters.getQueryType().equals(SearchParameters.DMQL))
            {
                LOG.debug("Parsing using DMQL");
                return DmqlCompiler.parseDmql(mParameters.getQuery(), metadata);
            }
            else // if DMQL2
            {
                LOG.debug("Parsing using DMQL2");
                return DmqlCompiler.parseDmql2(mParameters.getQuery(),
                                               metadata);
            }
        }
        catch (ANTLRException e)
        {
            // This is not an error as bad DMQL can cause this to be thrown.
            LOG.debug("Caught", e);
            // This is ugly .. we need to match the error message to determine which
            // RETS error to throw. Can't handle this another way because the grammar
            // is built first and therefore, can't yet import the Rets Exception classes.
            if (e.toString().contains("No such field")) {
                throw new RetsReplyException(ReplyCode.INVALID_QUERY_FIELD, e.toString());
            }
            
            if (e.toString().contains("No such lookup value")) {
                throw new RetsReplyException(ReplyCode.INVALID_QUERY_FIELD, e.toString());
            }
            
            throw new RetsReplyException(ReplyCode.INVALID_QUERY_SYNTAX,
                                         e.toString());
        }
    }

    protected List getColumns() throws RetsReplyException
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
            if (columns.isEmpty()) {
                throw new RetsReplyException(ReplyCode.MISC_SEARCH_ERROR, "No fields accessible from " + mParameters.getResourceId() + ":" + mParameters.getClassName());
            }
            return columns;
        }
    }

    protected String getSelectClause() throws RetsReplyException
    {
        return StringUtils.join(getColumns().iterator(), ",");
    }

    protected String getFromClause()
    {
        return mClass.getXDBName();
    }

    private static final Logger LOG =
        Logger.getLogger(DefaultSearchSqlBuilder.class);
    private MClass mClass;
    private MResource mResource;
    private Collection<MTable> mTables;
    private ServerDmqlMetadata mMetadata;
    private SearchParameters mParameters;
    private SortedSet<Group> mGroups;
    private Integer mLimit;
    private String mSqlConstraint;
}
