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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import antlr.ANTLRException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import org.realtors.rets.server.ReplyCode;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.RetsUtils;
import org.realtors.rets.server.User;
import org.realtors.rets.server.UserUtils;
import org.realtors.rets.server.dmql.DmqlCompiler;
import org.realtors.rets.server.dmql.SqlConverter;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.ServerDmqlMetadata;

public class SearchTransaction
{
    public SearchTransaction(SearchParameters parameters)
    {
        mParameters = parameters;
        mExecuteQuery = true;
    }

    public void setExecuteQuery(boolean executeQuery)
    {
        mExecuteQuery = executeQuery;
    }

    public void execute(PrintWriter out, MetadataManager manager,
                        SessionFactory sessions)
        throws RetsServerException
    {
        mSessions = sessions;
        MClass aClass = (MClass) manager.findByPath(
            MClass.TABLE,
            mParameters.getResourceId() + ":" + mParameters.getClassName());
        Collection tables = getTables(aClass);
        mMetadata = new ServerDmqlMetadata(tables,
                                           mParameters.isStandardNames());
        mFromClause = aClass.getDbTable();
        generateWhereClause();
        if (!mExecuteQuery)
        {
            RetsUtils.printEmptyRets(out, ReplyCode.NO_RECORDS_FOUND);
        }
        else
        {
            getCount();
            if (mParameters.getCount() == SearchParameters.COUNT_ONLY)
            {
                printCountOnly(out);
            }
            else
            {
                printData(out);
            }
        }
    }

    private Collection getTables(MClass aClass)
        throws RetsServerException
    {
        try
        {
            User user = mParameters.getUser();
            SortedSet groups = UserUtils.getGroups(user);
            TableGroupFilter groupFilter = RetsServer.getTableGroupFilter();
            String resourceName = aClass.getResource().getResourceID();
            String className = aClass.getClassName();
            return groupFilter.findTables(groups, resourceName, className);
        }
        catch (HibernateException e)
        {
            throw new RetsServerException(e);
        }
    }

    /**
     * Queries the database for the data and outputs the result.
     *
     * @param out
     * @throws RetsServerException
     */
    private void printData(PrintWriter out)
        throws RetsServerException
    {
        Collection columns = getColumns(mMetadata);
        String sql = generateSql(StringUtils.join(columns.iterator(), ","));
        LOG.debug("SQL: " + sql);
        executeSql(sql, out, columns);
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

    private Collection getColumns(ServerDmqlMetadata metadata)
        throws RetsReplyException
    {
        String[] fields = mParameters.getSelect();
        if (fields == null)
        {
            return metadata.getAllColumns();
        }
        else
        {
            List columns = new ArrayList();
            for (int i = 0; i < fields.length; i++)
            {
                String column = metadata.fieldToColumn(fields[i]);
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

    private String generateSql(String selectClause)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT ");
        buffer.append(selectClause);
        buffer.append(" FROM ");
        buffer.append(mFromClause);
        buffer.append(" WHERE ");
        buffer.append(mWhereCluase);
        return buffer.toString();
    }

    private void generateWhereClause()
        throws RetsReplyException
    {
        SqlConverter sqlConverter = parse(mMetadata);
        StringWriter stringWriter = new StringWriter();
        sqlConverter.toSql(new PrintWriter(stringWriter));
        mWhereCluase = stringWriter.toString();
    }

    private void printCountOnly(PrintWriter out)
    {
        RetsUtils.printOpenRetsSuccess(out);
        printCount(out);
        RetsUtils.printCloseRets(out);
    }

    private void executeSql(String sql, PrintWriter out, Collection columns)
        throws RetsServerException
    {
        Session session = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try
        {
            session = mSessions.openSession();
            Connection connection = session.connection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            advance(resultSet);
            printResults(out, columns, resultSet);
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

    private void printResults(PrintWriter out, Collection columns,
                                     ResultSet resultSet)
        throws RetsServerException
    {
        SearchFormatterContext context =
            new SearchFormatterContext(out, resultSet, columns, mMetadata);
        context.setLimit(mParameters.getLimit());
        SearchResultsFormatter formatter = getFormatter();

        RetsUtils.printXmlHeader(out);
        RetsUtils.printOpenRetsSuccess(out);
        printCount(out);
        formatter.formatResults(context);
        LOG.debug("Row count: " + context.getRowCount());
        RetsUtils.printCloseRets(out);
    }

    private SearchResultsFormatter getFormatter()
    {
        SearchResultsFormatter formatter;
        if (mParameters.getFormat().equals("STANDARD-XML"))
        {
            formatter = new ResidentialPropertyFormatter();
        }
        else
        {
            formatter = new CompactFormatter();
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
        // Todo: Add scrollable ResultSet support
        for (int i = 1; i < mParameters.getOffset(); i++)
        {
            resultSet.next();
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
        Logger.getLogger(SearchTransaction.class);
    private SearchParameters mParameters;
    private boolean mExecuteQuery;
    private String mFromClause;
    private String mWhereCluase;
    private int mCount;
    private SessionFactory mSessions;
    private ServerDmqlMetadata mMetadata;
}
