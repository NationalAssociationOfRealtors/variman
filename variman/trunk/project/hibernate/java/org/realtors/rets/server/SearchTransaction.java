/*
 */
package org.realtors.rets.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.realtors.rets.server.dmql.DmqlCompiler;
import org.realtors.rets.server.dmql.SqlConverter;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.ServerDmqlMetadata;

import antlr.ANTLRException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

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
        mMetadata = new ServerDmqlMetadata(aClass,
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
        Collection fields = columnsToFields(columns, mMetadata);
        executeSql(sql, out, fields);
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
        try
        {
            SqlConverter sqlConverter = parse(mMetadata);
            StringWriter stringWriter = new StringWriter();
            PrintWriter sqlWriter = new PrintWriter(stringWriter);
            sqlConverter.toSql(sqlWriter);
            mWhereCluase = stringWriter.toString();
        }
        catch (ANTLRException e)
        {
            // This is not an error as bad DMQL can cause this to be thrown.
            LOG.debug("Caught", e);
            throw new RetsReplyException(ReplyCode.INVALID_QUERY_SYNTAX,
                                         e.toString());
        }
    }

    private void printCountOnly(PrintWriter out)
    {
        RetsUtils.printOpenRetsSuccess(out);
        printCount(out);
        RetsUtils.printCloseRets(out);
    }

    private void executeSql(String sql, PrintWriter out, Collection fields)
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
            RetsUtils.printOpenRetsSuccess(out);
            printCount(out);
            out.println("<DELIMITER value=\"09\"/>");
            out.print("<COLUMNS>\t");
            out.print(StringUtils.join(fields.iterator(), "\t"));
            out.println("\t</COLUMNS>");
            int rowCount;
            for (rowCount = 0;
                 rowCount < mParameters.getLimit() && resultSet.next();
                 rowCount++)
            {
                out.print("<DATA>\t");
                for (int i = 0; i < fields.size(); i++)
                {
                    out.print(resultSet.getString(i+1) + "\t");
                }
                out.println("\t</DATA>");
            }
            RetsUtils.printCloseRets(out);
            LOG.debug("Row count: " + rowCount);
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

    private Collection columnsToFields(Collection columns,
                                 ServerDmqlMetadata metadata)
    {
        List fields = new ArrayList();
        for (Iterator i = columns.iterator(); i.hasNext();)
        {
            String column = (String) i.next();
            fields.add(metadata.columnToField(column));
        }
        return fields;
    }

    private SqlConverter parse(ServerDmqlMetadata metadata)
        throws ANTLRException
    {
        if (mParameters.getQueryType().equals(SearchParameters.DMQL))
        {
            LOG.debug("Parsing using DMQL");
            return DmqlCompiler.parseDmql(mParameters.getQuery(), metadata);
        }
        else // if (mParameters.getQueryType().equals(SearchParameters.DMQL2))
        {
            LOG.debug("Parsing using DMQL2");
            return DmqlCompiler.parseDmql2(mParameters.getQuery(),  metadata);
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
