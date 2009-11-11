/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.protocol;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import antlr.ANTLRException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.realtors.rets.client.RetsVersion;

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
import org.realtors.rets.server.dmql.DmqlCompiler;
import org.realtors.rets.server.dmql.SqlConverter;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.ServerDmqlMetadata;

public class DefaultSearchTransaction implements SearchTransaction
{
    public DefaultSearchTransaction()
    {
        LOG.debug("Creating DefaultSearchTransaction");
    }

    public SearchSqlBuilder getSearchSqlBuilder()
    {
        return mSearchSqlBuilder;
    }

    public void setSearchSqlBuilder(SearchSqlBuilder searchSqlBuilder)
    {
        mSearchSqlBuilder = searchSqlBuilder;
    }

    public void setParameters(SearchParameters parameters)
        throws RetsServerException
    {
        try
        {
            mParameters = parameters;
            mGroups = UserUtils.getGroups(mParameters.getUser());
            assertQueryLimitNotExceeded();

            mLimit = getLimit();
            mExecuteQuery = true;

            mSearchSqlBuilder.setParameters(mParameters);
            mSearchSqlBuilder.setGroups(mGroups);
        }
        catch (HibernateException e)
        {
            throw new RetsServerException(e);
        }
    }

    private void assertQueryLimitNotExceeded() throws RetsReplyException
    {
        QueryCountTable queryCountTable = RetsServer.getQueryCountTable();
        String username = mParameters.getUser().getUsername();
        SecurityConstraints securityConstraints =
            RetsServer.getSecurityConstraints();
        List allRules = securityConstraints.getAllRulesForGroups(mGroups);
        QueryCount queryCount = queryCountTable.getQueryCountForUser(username,
                                                                     allRules);
        LOG.debug("Using query count of " + queryCount.dump());
        if (!queryCount.increment())
        {
            LOG.info("Query limit exceeded for " + username);
            throw new RetsReplyException(20210, "Query limit exceeded");
        }
    }

    private int getLimit()
    {
        int userSuppliedLimit =  mParameters.getLimit();
        LOG.debug("User supplied limit is " + userSuppliedLimit);
        int limit = userSuppliedLimit;
        SecurityConstraints securityConstraints =
            RetsServer.getSecurityConstraints();
        List allRules = securityConstraints.getAllRulesForGroups(mGroups);
        for (Iterator iterator = allRules.iterator(); iterator.hasNext();)
        {
            GroupRules rules = (GroupRules) iterator.next();
            int recordLimit = rules.getRecordLimit();
            LOG.debug("Limit for group " + rules.getGroupName() + " is " +
                      recordLimit);
            if ((recordLimit != 0) && (recordLimit < limit))
            {
                limit = recordLimit;
            }
        }
        LOG.debug("Using limit of " + limit);
        return limit;
    }

    public void setExecuteQuery(boolean executeQuery)
    {
        mExecuteQuery = executeQuery;
    }

    public SearchTransactionStatistics execute(PrintWriter out, MetadataManager manager,
                        SessionFactory sessions)
        throws RetsServerException
    {
        mSessions = sessions;
        mSearchSqlBuilder.prepareForQuery(manager);
        generateWhereClause(mSearchSqlBuilder.getMetadataClass());

        if (!mExecuteQuery)
        {
            RetsUtils.printEmptyRets(out, ReplyCode.NO_RECORDS_FOUND);
            SearchTransactionStatistics statistics =
                new ImmutableSearchTransactionStatistics(ReplyCode.NO_RECORDS_FOUND.getValue(),
                        ReplyCode.NO_RECORDS_FOUND.getName(), 0, 0);
            return statistics;
        }
        else
        {
            getCount();
            if (mParameters.getCount() == SearchParameters.COUNT_ONLY)
            {
                return printCountOnly(out);
            }
            else
            {
                return printData(out);
            }
        }
    }

    /**
     * Queries the database for the data and outputs the result.
     *
     * @param out
     * @throws RetsServerException
     */
    private SearchTransactionStatistics printData(PrintWriter out)
        throws RetsServerException
    {
        Session session = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try
        {
            String sql = generateSql(mSearchSqlBuilder.getSelectClause());
            logSql(sql);
            session = mSessions.openSession();
            Connection connection = session.connection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            advance(resultSet);
            return printResults(out, mSearchSqlBuilder.getColumns(), resultSet);
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
    }

    /**
     * Gets the count by querying the database. The count is the number of rows
     * matching the DMQL query.
     *
     * @throws RetsServerException
     */
    private void getCount() throws RetsServerException
    {
        mCount = 0;
        if (!mParameters.countRequested())
        {
            return;
        }

        String countSql = generateSql("COUNT(*)");
        LOG.debug("Count SQL: " + countSql);
        Session session = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try
        {
            logSql(countSql);
            session = mSessions.openSession();
            Connection connection = session.connection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(countSql);
            if (resultSet.next())
            {
                mCount = resultSet.getInt(1);
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
            close(session);
        }
    }

    private String generateSql(String selectClause) throws RetsReplyException
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT ");
        buffer.append(selectClause);
        buffer.append(" FROM ");
        buffer.append(mSearchSqlBuilder.getFromClause());
        buffer.append(" WHERE ");
        buffer.append(mWhereClause);
        return buffer.toString();
    }

    private void generateWhereClause(MClass aClass)
        throws RetsServerException
    {
        ConditionRuleSet conditionRuleSet =
            RetsServer.getConditionRuleSet();
        String resourceName = aClass.getResource().getResourceID();
        String className = aClass.getClassName();
        String sqlConstraint =
            conditionRuleSet.findSqlConstraint(mGroups, resourceName,
                                               className);
        SqlConverter sqlConverter = parse(mSearchSqlBuilder.getMetadata());
        StringWriter stringWriter = new StringWriter();
        sqlConverter.toSql(new PrintWriter(stringWriter));
        mWhereClause = stringWriter.toString();

        if (StringUtils.isNotEmpty(sqlConstraint))
        {
            mWhereClause = "(" + mWhereClause + ") AND " + sqlConstraint;
        }
    }

    private SearchTransactionStatistics printCountOnly(PrintWriter out)
    {
        RetsUtils.printOpenRetsSuccess(out);
        printCount(out);
        RetsUtils.printCloseRets(out);

        SearchTransactionStatistics statistics = new ImmutableSearchTransactionStatistics(ReplyCode.SUCCESSFUL.getValue(), ReplyCode.SUCCESSFUL.getName(), mCount, null);
        return statistics;
    }

    private void logSql(String sql)
    {
        LOG.debug("SQL: " + sql);

        String select = StringUtils.join(mParameters.getSelect(), ",");
        if (select == null)
        {
            select = "<empty>";
        }
        String message = "SELECT: " + select + " " +
                         "DMQL: " + mParameters.getQuery() + " " +
                         "SQL: " + sql;
        SQL_LOG.info(message);
        LOG.info(message);
    }

    private SearchTransactionStatistics printResults(PrintWriter out, List columns,
                                     ResultSet resultSet)
        throws RetsServerException
    {
        SearchFormatterContext context =
            new SearchFormatterContext(out, resultSet, columns,
                                       mSearchSqlBuilder.getMetadata(),
                                       mParameters.getRetsVersion());
        context.setLimit(getLimit());
        mCount = Math.min(mCount, mLimit);
        SearchResultsFormatter formatter = getFormatter();

        RetsUtils.printXmlHeader(out);
        RetsUtils.printOpenRetsSuccess(out);
        printCount(out);
        formatter.formatResults(context);
        LOG.debug("Row count: " + context.getRowCount());
        RetsUtils.printCloseRets(out);

        SearchTransactionStatistics statistics = new ImmutableSearchTransactionStatistics(ReplyCode.SUCCESSFUL.getValue(), ReplyCode.SUCCESSFUL.getName(), mCount, context.getRowCount());
        return statistics;
	}

    private SearchResultsFormatter getFormatter() throws RetsServerException
    {
        SearchResultsFormatter formatter;
        SearchFormat searchFormat =
            SearchFormat.getEnum(mParameters.getFormat());
        if (searchFormat == SearchFormat.STANDARD_XML)
        {
            formatter = new ResidentialPropertyFormatter();
        }
        else if (searchFormat == SearchFormat.COMPACT_DECODED)
        {
            formatter =
                new CompactFormatter(CompactFormatter.DECODE_LOOKUPS, mParameters.getRetsVersion());
        }
        else if (searchFormat == SearchFormat.COMPACT)
        {
            formatter =
                new CompactFormatter(CompactFormatter.NO_DECODING, mParameters.getRetsVersion());
        }
        else
        {
            throw new RetsReplyException(ReplyCode.MISC_SEARCH_ERROR,
                                         "Unknown format: " +
                                          mParameters.getFormat());
        }

        return formatter;
    }

    private void printCount(PrintWriter out)
    {
        if (mParameters.countRequested())
        {
            out.println("<COUNT Records=\"" + mCount + "\"/>");
        }
    }

    private void advance(ResultSet resultSet) throws SQLException
    {
        int offset = mParameters.getOffset();
        LOG.debug("Advancing using offset: " + offset);
        // TODO: Add scrollable ResultSet support
        for (int i = 1; i < offset; i++)
        {
            resultSet.next();
        }
        if (mParameters.getCount() == SearchParameters.COUNT_AND_DATA)
        {
            mCount -= offset - 1;
        }
    }

    private SqlConverter parse(ServerDmqlMetadata metadata)
        throws RetsReplyException
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
            if (e.toString().contains("No such field"))
            	throw new RetsReplyException(ReplyCode.INVALID_QUERY_FIELD, e.toString());
            
            if (e.toString().contains("No such lookup value"))
            	throw new RetsReplyException(ReplyCode.INVALID_QUERY_FIELD, e.toString());
            
            throw new RetsReplyException(ReplyCode.INVALID_QUERY_SYNTAX,
                                         e.toString());
        }
    }

    private void close(Session session)
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

    private void close(Statement statement)
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

    private void close(ResultSet resultSet)
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

    private static final Logger LOG =
        Logger.getLogger(DefaultSearchTransaction.class);
    private static final String SQL_LOG_NAME =
        "org.realtors.rets.server.sql_log";
    private static final Logger SQL_LOG = Logger.getLogger(SQL_LOG_NAME);

    private SearchParameters mParameters;
    private SortedSet mGroups;
    private SearchSqlBuilder mSearchSqlBuilder;
    private boolean mExecuteQuery;
    private String mWhereClause;
    private int mCount;
    private SessionFactory mSessions;
    private int mLimit;
}
