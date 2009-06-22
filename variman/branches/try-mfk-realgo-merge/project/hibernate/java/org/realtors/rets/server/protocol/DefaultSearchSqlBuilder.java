/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2007, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.protocol;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.realtors.rets.server.ReplyCode;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.dmql.DmqlCompiler;
import org.realtors.rets.server.dmql.DmqlParserMetadata;
import org.realtors.rets.server.dmql.SqlConverter;
import org.realtors.rets.server.dmql.DmqlCompiler.ParserResults;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.ServerDmqlMetadata;
import org.realtors.rets.server.protocol.DmqlQuery.Version;
import org.realtors.rets.server.protocol.SearchSqlBuilder.SqlStatements.Query;
import org.realtors.rets.server.protocol.SearchSqlBuilder.SqlStatements.SearchQuery;

import antlr.ANTLRException;

public class DefaultSearchSqlBuilder implements SearchSqlBuilder
{
    private static final Logger LOG = Logger.getLogger(SearchSqlBuilder.class);
    
    private String mResourceId;
    private String mClassName;
    private List/*String*/ mSelectList; // Must not be null or empty.
    private DmqlQuery mDmqlQuery;
    private List/*DmqlQuery*/ mDmqlConstraints;
    private List/*String*/ mSqlConstraints;
    private Integer mLimit;
    
    private transient MetadataManager mMetadataManager;
    private transient MClass mMetaClass;
    private transient DmqlParserMetadata mDmqlParserMetadata;
    
    /**
     * Constructs a new <code>DefaultSearchSqlBuilder</code>.
     * <p>
     * Be sure to set the <code>MetadataManager</code> to complete
     * initialization.</p>
     */
    public DefaultSearchSqlBuilder()
    {
        // Be sure to set the MetadataManager before using.
    }
    
    public DefaultSearchSqlBuilder(final MetadataManager metadataManager)
    {
        _setMetadataManager(metadataManager);
    }
    
    public MetadataManager getMetadataManager()
    {
        return mMetadataManager;
    }
    
    public void setMetadataManager(final MetadataManager metadataManager)
    {
        _setMetadataManager(metadataManager);
    }
    
    /*
     * Removes duplicate code in the constructor and the setter.
     */
    private void _setMetadataManager(final MetadataManager metadataManager)
    {
        if (metadataManager == null) {
            throw new NullPointerException("Metadata Manager must not be null.");
        }
        mMetadataManager = metadataManager;
    }
    
    protected MClass getMetadataClass() throws RetsReplyException
    {
        if (mMetaClass == null) {
            final String resourceId = getResourceId();
            final String className = getClassName();
            MClass potentialMetaClass = (MClass)mMetadataManager.findByPath(MClass.TABLE, resourceId + ":" + className);
            if (potentialMetaClass == null) {
                throw new RetsReplyException(ReplyCode.MISC_SEARCH_ERROR, "Invalid class: " + className);
            }
            mMetaClass = potentialMetaClass;
        }
        return mMetaClass;
    }
    
    protected DmqlParserMetadata getDmqlParserMetadata() throws RetsReplyException
    {
        if (mDmqlParserMetadata == null) {
            final DmqlQuery dmqlQuery = getDmqlQuery();
            final boolean isStandardNames = dmqlQuery.isStandardNames();
            mDmqlParserMetadata = new ServerDmqlMetadata(getMetadataClass(), isStandardNames);
        }
        return mDmqlParserMetadata;
    }
    
    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder#createSqlStatements()
     */
    public SqlStatements createSqlStatements() throws RetsServerException
    {
        Query countQuery = getCountQuery();
        SearchQuery searchQuery = getSearchQuery();
        
        final SqlStatements sqlStatements = new DefaultSqlStatements(countQuery, searchQuery);
        return sqlStatements;
    }

    protected Query getCountQuery() throws RetsReplyException
    {
        StringBuffer buffer = new StringBuffer();
        final Integer limit = getLimit();
        if (limit == null) {
            buffer.append("SELECT COUNT(*)");
        } else {
            buffer.append("SELECT TOP " + limit.intValue() + " COUNT(*)");
        }
        buffer.append(" FROM ");
        buffer.append(getFromClause());
        buffer.append(" WHERE ");
        buffer.append(getWhereClause());
        final String countQuerySql = buffer.toString();
        final Query countQuery = new DefaultQuery(countQuerySql);
        return countQuery;
    }

    protected SearchQuery getSearchQuery() throws RetsReplyException {
        StringBuffer buffer = new StringBuffer();
        final Integer limit = getLimit();
        if (limit == null) {
            buffer.append("SELECT ");
        } else {
            buffer.append("SELECT TOP " + limit.intValue() + " ");
        }
        final List/*String*/ selectList = getSelectList();
        final DmqlParserMetadata fieldToColumnResolver = getDmqlParserMetadata();
        final List/*String*/ selectedColumnNames = getColumnNames(selectList, fieldToColumnResolver);
        final String selectClause = getSelectClause(selectedColumnNames);
        buffer.append(selectClause);
        buffer.append(" FROM ");
        buffer.append(getFromClause());
        buffer.append(" WHERE ");
        buffer.append(getWhereClause());
        final String searchQuerySql = buffer.toString();
        final SearchQuery searchQuery = new DefaultSearchQuery(searchQuerySql, selectedColumnNames, fieldToColumnResolver);
        return searchQuery;
    }
    
    private String getSelectClause(final List/*String*/ columnNames)
    {
        return StringUtils.join(columnNames.iterator(), ",");
    }
    
    private List/*String*/ getColumnNames(final List/*String*/ selectList, final DmqlParserMetadata fieldToColumnResolver) throws RetsReplyException
    {
        List/*String*/ columnNames = new ArrayList/*String*/();
        
        for (Iterator/*String*/ iter = selectList.iterator(); iter.hasNext(); ) {
            final String fieldName = (String)iter.next();
            final String columnName = (String)fieldToColumnResolver.fieldToColumn(fieldName);
            if (columnName == null) {
                throw new RetsReplyException(ReplyCode.INVALID_SELECT, fieldName);
            }
            columnNames.add(columnName);
        }
        
        return columnNames;
    }

    protected String getFromClause() throws RetsReplyException
    {
        return getMetadataClass().getDbTable();
    }

    protected String getWhereClause() throws RetsReplyException
    {
        StringBuffer whereClause = new StringBuffer();
        final DmqlQuery dmqlQuery = getDmqlQuery();
        
        // Generate sql where clause fragment from the specified RETS client
        // query.
        ParserResults parserResults = parseDmql(dmqlQuery, getDmqlParserMetadata());
        SqlConverter sqlConverter = parserResults.getSqlConverter();
        StringWriter stringWriter = new StringWriter();
        sqlConverter.toSql(new PrintWriter(stringWriter));
        final String dmqlQueryAsSql = stringWriter.toString();
        appendToWhereClause(whereClause, dmqlQueryAsSql);
        
        // Check for DMQL-constraint
        final List/*DmqlQuery*/ dmqlConstraints = getDmqlConstraints();
        for (Iterator/*DmqlQuery*/ iter = dmqlConstraints.iterator(); iter.hasNext(); ) {
            final DmqlQuery dmqlConstraint = (DmqlQuery)iter.next();
            parserResults = parseDmql(dmqlConstraint, getDmqlParserMetadata());
            sqlConverter = parserResults.getSqlConverter();
            stringWriter = new StringWriter();
            sqlConverter.toSql(new PrintWriter(stringWriter));
            final String dmqlConstraintAsSql = stringWriter.toString();
            appendToWhereClause(whereClause, dmqlConstraintAsSql);
        }
        
        // Check for SQL-constraint
        final List/*String*/ sqlConstraints = getSqlConstraints();
        for (Iterator/*String*/ iter = sqlConstraints.iterator(); iter.hasNext(); ) {
            final String sqlConstraint = (String)iter.next();
            appendToWhereClause(whereClause, sqlConstraint);
        }
        
        return whereClause.toString();
    }
    
    private void appendToWhereClause(StringBuffer whereClause, String whereClauseSegment)
    {
        if (!StringUtils.isBlank(whereClauseSegment)) {
            if (whereClause.length() > 0) {
                whereClause.insert(0, "(").append(") AND ");    
            }
            whereClause.append(whereClauseSegment);
        }
    }

    private ParserResults parseDmql(final DmqlQuery dmqlQuery, final DmqlParserMetadata metadata) throws RetsReplyException
    {
        try {
            final String dmql = dmqlQuery.getDmql();
            final Version dmqlVersion = dmqlQuery.getVersion();
            if (DmqlQuery.Version.DMQL.equals(dmqlVersion)) {
                LOG.debug("Parsing using DMQL");
                return DmqlCompiler.parseDmql(dmql, metadata);
            } else { // if DMQL2
                LOG.debug("Parsing using DMQL2");
                return DmqlCompiler.parseDmql2(dmql, metadata);
            }
        } catch (ANTLRException e) {
            // This is not an error as bad DMQL can cause this to be thrown.
            LOG.debug("Caught", e);
            throw new RetsReplyException(ReplyCode.INVALID_QUERY_SYNTAX, e.toString());
        }
    }

    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder#getResourceId()
     */
    public String getResourceId() {
        return mResourceId;
    }

    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder#setResourceId(java.lang.String)
     */
    public void setResourceId(final String resourceId) {
        if (resourceId == null) {
            throw new NullPointerException("Resource ID must not be null.");
        }
        mResourceId = resourceId;
    }

    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder#getClassName()
     */
    public String getClassName() {
        return mClassName;
    }

    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder#setClassName(java.lang.String)
     */
    public void setClassName(final String className) {
        if (className == null) {
            throw new NullPointerException("Classname must not be null.");
        }
        mClassName = className;
    }

    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder#getSelectList()
     */
    public List/*String*/ getSelectList() {
        return mSelectList;
    }

    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder#setSelectList(java.util.List)
     */
    public void setSelectList(List/*String*/ selectList) {
        if (selectList == null || selectList.isEmpty()) {
            throw new NullPointerException("Select list must not be null or empty.");
        }
        mSelectList = selectList;
    }

    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder#getDmqlQuery()
     */
    public DmqlQuery getDmqlQuery() {
        return mDmqlQuery;
    }

    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder#setDmqlQuery(org.realtors.rets.server.protocol.DmqlQuery)
     */
    public void setDmqlQuery(final DmqlQuery dmqlQuery) {
        if (dmqlQuery == null) {
            throw new NullPointerException("DMQL query must not be null.");
        }
        mDmqlQuery = dmqlQuery;
    }

    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder#getSqlConstraints()
     */
    public List getSqlConstraints() {
        return mSqlConstraints;
    }

    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder#setSqlConstraints(java.util.List)
     */
    public void setSqlConstraints(List sqlConstraints) {
        mSqlConstraints = sqlConstraints;
    }
    
    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder#getDmqlConstraints()
     */
    public List/*DmqlQuery*/ getDmqlConstraints() {
        return mDmqlConstraints;
    }

    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder#setDmqlConstraints(java.util.List)
     */
    public void setDmqlConstraints(List/*DmqlQuery*/ dmqlConstraints) {
        mDmqlConstraints = dmqlConstraints;
    }

    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder#getLimit()
     */
    public Integer getLimit() {
        return mLimit;
    }

    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchSqlBuilder#setLimit(int)
     */
    public void setLimit(Integer limit) {
        if (limit != null && limit.intValue() < 0) {
            throw new IllegalArgumentException("limit must be greater than or equal to zero.");
        }
        mLimit = limit;
    }

}
