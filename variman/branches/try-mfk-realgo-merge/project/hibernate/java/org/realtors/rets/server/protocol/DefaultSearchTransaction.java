/*
 * Variman RETS Server
 *
 * Author: Dave Dribin and modified by Danny Hurlburt
 * Copyright (c) 2007, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.protocol;


import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.realtors.rets.server.QueryCount;
import org.realtors.rets.server.QueryCountTable;
import org.realtors.rets.server.ReplyCode;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.RetsUtils;
import org.realtors.rets.server.UserUtils;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.config.SecurityConstraints;
import org.realtors.rets.server.dmql.DmqlParserMetadata;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.TableStandardName;
import org.realtors.rets.server.protocol.SearchSqlBuilder.SqlStatements;
import org.realtors.rets.server.protocol.SearchSqlBuilder.SqlStatements.Query;
import org.realtors.rets.server.protocol.SearchSqlBuilder.SqlStatements.SearchQuery;

/**
 * The default search transaction implementation uses a search SQL builder to
 * create the SQL statements to retrieve the requested data.
 */
public class DefaultSearchTransaction implements SearchTransaction
{
    public static final String SQL_LOG_NAME = "org.realtors.rets.server.sql_log";
    
    static final Logger SQL_LOG = Logger.getLogger(SQL_LOG_NAME);
    static final Logger LOG = Logger.getLogger(SearchTransaction.class);
    
    private SearchSqlBuilder mSearchSqlBuilder;
    
    public SearchSqlBuilder getSearchSqlBuilder() {
        return mSearchSqlBuilder;
    }

    public void setSearchSqlBuilder(SearchSqlBuilder searchSqlBuilder) {
        mSearchSqlBuilder = searchSqlBuilder;
    }

    /* (non-Javadoc)
     * @see org.realtors.rets.server.protocol.SearchTransaction#execute(java.io.PrintWriter, org.realtors.rets.server.protocol.SearchParameters)
     */
    public SearchTransactionStatistics execute(final PrintWriter out, final SearchParameters searchParameters, final MetadataManager metadataManager) throws RetsServerException
    {
        if (out == null) {
            throw new NullPointerException("Output print writer must not be null.");
        }
        if (searchParameters == null) {
            throw new NullPointerException("Search parameters must not be null.");
        }
        if (metadataManager == null) {
            throw new NullPointerException("Metadata manager must not be null.");
        }
        SearchSqlBuilder searchSqlBuilder = getSearchSqlBuilder();
        searchSqlBuilder.setMetadataManager(metadataManager);
        __SearchTransaction searchTransaction = new __SearchTransaction(searchSqlBuilder);
        SearchTransactionStatistics searchTransactionStatistics = searchTransaction.execute(out, searchParameters);
        return searchTransactionStatistics;
    }
    
    private static class __SearchTransaction
    {
        private SearchParameters mParameters;
        private SortedSet mGroups;
        private SearchSqlBuilder mSearchSqlBuilder;
        private Integer mLimit;
        private transient SqlStatements mSqlStatements;
        
        public __SearchTransaction(SearchSqlBuilder searchSqlBuilder)
        {
            setSearchSqlBuilder(searchSqlBuilder);
        }

        public SearchSqlBuilder getSearchSqlBuilder()
        {
            return mSearchSqlBuilder;
        }

        public void setSearchSqlBuilder(SearchSqlBuilder searchSqlBuilder)
        {
            mSearchSqlBuilder = searchSqlBuilder;
        }

        public SearchTransactionStatistics execute(final PrintWriter out, final SearchParameters searchParameters) throws RetsServerException
        {
            setParameters(searchParameters);
            final String resourceId = mParameters.getResourceId();
            final String className = mParameters.getClassName();
            final DmqlQuery dmqlQuery = mParameters.getDmqlQuery();
            final List/*DmqlQuery*/ dmqlConstraints = new ArrayList/*DmqlQuery*/();
            final ConditionRuleSet conditionRuleSet = RetsServer.getConditionRuleSet();
            final String dmqlConstraint = conditionRuleSet.findDmqlConstraint(mGroups, resourceId, className);
            if (StringUtils.isNotBlank(dmqlConstraint)) {
                dmqlConstraints.add(new DmqlQuery(dmqlConstraint));
            }
            final List/*String*/ sqlConstraints = new ArrayList/*String*/();
            final String sqlConstraint = conditionRuleSet.findSqlConstraint(mGroups, resourceId, className);
            sqlConstraints.add(sqlConstraint);
            final String[] fieldNames = mParameters.getSelect();
            final List/*String*/ selectList = findAllowedSelectList(resourceId, className, fieldNames, mParameters.isStandardNames());
            
            mSearchSqlBuilder.setResourceId(resourceId);
            mSearchSqlBuilder.setClassName(className);
            mSearchSqlBuilder.setDmqlQuery(dmqlQuery);
            mSearchSqlBuilder.setDmqlConstraints(dmqlConstraints);
            mSearchSqlBuilder.setSqlConstraints(sqlConstraints);
            mSearchSqlBuilder.setLimit(mLimit);
            mSearchSqlBuilder.setSelectList(selectList);
            
            SearchTransactionStatistics searchTransactionStatistics;
            SearchParameters.Count count = mParameters.getCount();
            if (count == SearchParameters.COUNT_ONLY) {
                searchTransactionStatistics = printCountOnly(out);
            } else {
                if (count == SearchParameters.COUNT_AND_DATA) {
                    searchTransactionStatistics = printCountAndData(out);
                } else {
                    searchTransactionStatistics = printDataOnly(out);
                }
            }
            return searchTransactionStatistics;
        }
        
        private List/*String*/ findAllowedSelectList(String resourceId, String className, String[] fieldNames, boolean isStandardNames) throws RetsReplyException
        {
            final List/*String*/ selectList = new ArrayList/*String*/();
            
            TableGroupFilter tableGroupFilter = RetsServer.getTableGroupFilter();
            Set/*Table*/ allowedTables = tableGroupFilter.findTables(mGroups, resourceId, className);
            
            if (fieldNames == null || fieldNames.length == 0) {
                for (Iterator/*Table*/ iter = allowedTables.iterator(); iter.hasNext(); ) {
                    final Table metaTable = (Table)iter.next();
                    final String fieldName = getFieldName(metaTable, isStandardNames);
                    if (fieldName != null) {
                        selectList.add(fieldName);
                    }
                }
            } else {
                Set/*String*/ allowedFieldNames = new HashSet/*String*/();
                for (Iterator/*Table*/ iter = allowedTables.iterator(); iter.hasNext(); ) {
                    final Table metaTable = (Table)iter.next();
                    final String fieldName = getFieldName(metaTable, isStandardNames);
                    if (fieldName != null) {
                        allowedFieldNames.add(fieldName);
                    }
                }
                final int numRequestedFields = fieldNames.length;
                for (int idx = 0; idx < numRequestedFields; ++idx) {
                    final String fieldName = fieldNames[idx];
                    if (allowedFieldNames.contains(fieldName)) {
                        selectList.add(fieldName);
                    } else {
                        throw new RetsReplyException(ReplyCode.INVALID_SELECT, fieldName);
                    }
                }
            }
            
            return selectList;
        }
        
        private String getFieldName(Table metaTable, boolean isStandardNames)
        {
            String fieldName = null;
            if (isStandardNames) {
                final TableStandardName tableStandardName = metaTable.getStandardName();
                if (tableStandardName != null) {
                    fieldName = tableStandardName.getName();
                }
            } else {
                fieldName = metaTable.getSystemName();
            }
            return fieldName;
        }

        private void setParameters(SearchParameters searchParameters) throws RetsServerException
        {
            mParameters = searchParameters;
            handleSearchParametersChangedEvent(mParameters);
        }
        
        private void handleSearchParametersChangedEvent(SearchParameters searchParameters) throws RetsServerException
        {
            try {
                mGroups = UserUtils.getGroups(searchParameters.getUser());
                
                assertQueryLimitNotExceeded(searchParameters, mGroups);
                
                resetLimit(searchParameters, mGroups);
            } catch (HibernateException e) {
                throw new RetsServerException(e);
            }
        }
        
        private void assertQueryLimitNotExceeded(SearchParameters searchParameters, Set/*Group*/ groups) throws RetsReplyException
        {
            QueryCountTable queryCountTable = RetsServer.getQueryCountTable();
            String username = searchParameters.getUser().getUsername();
            SecurityConstraints securityConstraints = RetsServer.getSecurityConstraints();
            List/*GroupRules*/ allRules = securityConstraints.getAllRulesForGroups(groups);
            QueryCount queryCount = queryCountTable.getQueryCountForUser(username, allRules);
            LOG.debug("Using query count of " + queryCount.dump());
            if (!queryCount.increment()) {
                LOG.info("Query limit exceeded for " + username);
                throw new RetsReplyException(20210, "Query limit exceeded");
            }
        }

        private void resetLimit(SearchParameters searchParameters, Set/*Group*/ group)
        {
            int userSuppliedLimit =  searchParameters.getLimit();
            LOG.debug("User supplied limit is " + userSuppliedLimit);
            int limit = userSuppliedLimit;
            SecurityConstraints securityConstraints = RetsServer.getSecurityConstraints();
            List allRules = securityConstraints.getAllRulesForGroups(group);
            for (Iterator/*GroupRules*/ iterator = allRules.iterator(); iterator.hasNext(); ) {
                GroupRules groupRules = (GroupRules)iterator.next();
                int recordLimit = groupRules.getRecordLimit();
                LOG.debug("Limit for group " + groupRules.getGroup().getName() + " is " + recordLimit);
                if ((recordLimit != 0) && (recordLimit < limit)) {
                    limit = recordLimit;
                }
            }
            LOG.debug("Using limit of " + limit);
            mLimit = Integer.valueOf(limit);
        }
        
        private SqlStatements createSqlStatements() throws RetsServerException
        {
            if (mSqlStatements == null) {
                mSqlStatements = mSearchSqlBuilder.createSqlStatements();
            }
            return mSqlStatements;
        }
        
        private SearchTransactionStatistics printCountOnly(PrintWriter out) throws RetsServerException
        {
            SearchTransactionStatisticsBuilder stsb = new SearchTransactionStatisticsBuilder();
            ReplyCode replyCode = ReplyCode.SUCCESSFUL;
            int replyCodeValue = replyCode.getValue();
            String replyCodeText = replyCode.getName();
            int count = getCount();
            
            stsb.setReplyCode(replyCodeValue);
            stsb.setReplyText(replyCodeText);
            stsb.setCount(Integer.valueOf(count));
            
//            RetsUtils.printXmlHeader(out); TODO: Check if this is suppose to be here.
            RetsUtils.printOpenRetsSuccess(out);
            printCount(out, count);
            RetsUtils.printCloseRets(out);
            SearchTransactionStatistics searchTransactionStatistics = stsb.build();
            return searchTransactionStatistics;
        }

        /*
         * Gets the count by querying the database. The count is the number of rows
         * matching the DMQL query.
         *
         * @throws RetsServerException
         */
        private int getCount() throws RetsServerException
        {
            int count = 0;
            if (!mParameters.countRequested()) {
                return count;
            }
            
            SqlStatements sqlStatements = createSqlStatements();
            Query countQuery = sqlStatements.getCountQuery();
            String countQuerySql = countQuery.getSql();
            LOG.debug("Count SQL: " + countQuerySql);
            Session session = null;
            Statement statement = null;
            ResultSet resultSet = null;
            try
            {
                logSql(countQuerySql);
                session = RetsServer.getSessions().openSession();
                Connection connection = session.connection();
                statement = connection.createStatement();
                resultSet = statement.executeQuery(countQuerySql);
                if (resultSet.next())
                {
                    count = resultSet.getInt(1);
                }
                else
                {
                    LOG.warn("COUNT(*) returned no rows");
                }
            }
            catch (HibernateException e)
            {
                LOG.error("Caught", e);
                throw new RetsServerException(e);
            }
            catch (SQLException e)
            {
                LOG.error("Caught", e);
                throw new RetsServerException(e);
            }
            finally
            {
                close(resultSet);
                close(statement);
                // TODO: Does the connection also need to be closed?
                close(session);
            }
            return count;
        }

        private SearchTransactionStatistics printDataOnly(PrintWriter out) throws RetsServerException
        {
            final boolean printCount = false;
            SearchTransactionStatistics searchTransactionStatistics = printData(out, printCount);
            return searchTransactionStatistics;
        }

        private SearchTransactionStatistics printCountAndData(PrintWriter out) throws RetsServerException
        {
            final boolean printCount = true;
            SearchTransactionStatistics searchTransactionStatistics = printData(out, printCount);
            return searchTransactionStatistics;
        }

        /*
         * Queries the database for the data and outputs the result.
         *
         * @param out
         * @throws RetsServerException
         */
        private SearchTransactionStatistics printData(PrintWriter out, boolean printCount) throws RetsServerException
        {
            SearchTransactionStatisticsBuilder stsb = new SearchTransactionStatisticsBuilder();
            Session session = null;
            Statement statement = null;
            ResultSet resultSet = null;
            try {
                SqlStatements sqlStatements = createSqlStatements();
                SearchQuery searchQuery = sqlStatements.getSearchQuery();
                String searchQuerySql = searchQuery.getSql();
                logSql(searchQuerySql);
                session = RetsServer.getSessions().openSession();
                Connection connection = session.connection();
                statement = connection.createStatement();
                resultSet = statement.executeQuery(searchQuerySql);
                advance(resultSet, mParameters.getOffset());
                printResults(out, searchQuery, resultSet, printCount, stsb);
            }
            catch (HibernateException e)
            {
                throw new RetsServerException(e);
            }
            catch (SQLException e)
            {
                throw new RetsServerException(e);
            }
            finally
            {
                close(resultSet);
                close(statement);
                close(session);
            }
            SearchTransactionStatistics searchTransactionStatistics = stsb.build();
            return searchTransactionStatistics;
        }
        
        private void printResults(PrintWriter out, SearchQuery searchQuery, ResultSet resultSet, boolean printCount, SearchTransactionStatisticsBuilder stsb) throws RetsServerException
        {
            DmqlParserMetadata dmqlParserMetadata = searchQuery.getDmqlParserMetadata();
            List/*String*/ selectedColumnNames = searchQuery.getSelectedColumnNames();
            SearchFormatterContext context = new SearchFormatterContext(out, resultSet, selectedColumnNames, dmqlParserMetadata);
            context.setLimit(mLimit.intValue());
            SearchResultsFormatter formatter = getFormatter();
        
            ReplyCode replyCode = ReplyCode.SUCCESSFUL;
            int replyCodeValue = replyCode.getValue();
            String replyCodeText = replyCode.getName();
            
            stsb.setReplyCode(replyCodeValue);
            stsb.setReplyText(replyCodeText);
            
            RetsUtils.printXmlHeader(out);
            RetsUtils.printOpenRetsSuccess(out);
            if (printCount) {
                int count = getCount();
                stsb.setCount(Integer.valueOf(count));
                printCount(out, count);
            }
            formatter.formatResults(context);
            int rowCount = context.getRowCount();
            stsb.setDataCount(Integer.valueOf(rowCount));
            LOG.debug("Row count: " + rowCount);
            RetsUtils.printCloseRets(out);
        }

        private void printCount(PrintWriter out, int count)
        {
            if (mParameters.countRequested()) {
                out.println("<COUNT Records=\"" + count + "\"/>");
            }
        }
        
        protected void logSql(final String sql)
        {
            LOG.debug("SQL: " + sql);

            String select = StringUtils.join(mParameters.getSelect(), ",");
            if (select == null) {
                select = "<empty>";
            }
            String message = "SELECT: " + select + " " +
                             "DMQL: " + mParameters.getDmqlQuery().getDmql() + " " +
                             "SQL: " + sql;
            SQL_LOG.info(message);
            LOG.info(message);
        }

        protected void advance(ResultSet resultSet, int offset) throws SQLException
        {
            // Todo: Add scrollable ResultSet support
            for (int i = 1; i < offset; i++) {
                resultSet.next();
            }
        }

        protected void close(Session session)
        {
            try
            {
                if (session != null)
                {
                    session.close();
                }
            }
            catch (HibernateException e)
            {
                LOG.error("Caught", e);
            }
        }

        protected void close(Statement statement) // TODO: Use the ones from Commons-DBUtils
        {
            try
            {
                if (statement != null)
                {
                    statement.close();
                }
            }
            catch (SQLException e)
            {
                LOG.error("Caught", e);
            }
        }

        protected void close(ResultSet resultSet) // TODO: Use the ones from Commons-DBUtils
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
            }
            catch (SQLException e)
            {
                LOG.error("Caught", e);
            }
        }

        protected SearchResultsFormatter getFormatter() throws RetsReplyException
        {
            SearchResultsFormatter formatter = null;
            SearchFormat searchFormat = mParameters.getFormat();
            if (searchFormat == SearchFormat.STANDARD_XML) {
                formatter = new ResidentialPropertyFormatter();
            } else if (searchFormat == SearchFormat.COMPACT_DECODED) {
                formatter = new CompactFormatter(CompactFormatter.DECODE_LOOKUPS);
            } else if (searchFormat == SearchFormat.COMPACT) {
                formatter = new CompactFormatter(CompactFormatter.NO_DECODING);
            } else {
                throw new RetsReplyException(ReplyCode.MISC_SEARCH_ERROR,
                                             "Unknown format: " +
                                             mParameters.getFormat());
            }
            
            return formatter;
        }
        
    }

}
