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

public class SearchTransaction extends RetsTransaction
{
    public SearchTransaction(SearchParameters parameters)
    {
        mParameters = parameters;
        mExecuteSql = true;
    }

    public void setExecuteSql(boolean executeSql)
    {
        mExecuteSql = executeSql;
    }

    public void execute(PrintWriter out, MetadataManager manager,
                        SessionFactory sessions)
        throws RetsServerException
    {
        MClass aClass = (MClass) manager.findByPath(
            MClass.TABLE,
            mParameters.getResourceId() + ":" + mParameters.getClassName());
        ServerDmqlMetadata metadata =
            new ServerDmqlMetadata(aClass, mParameters.isStandardNames());
        Collection columns = metadata.getAllColumns();
        String sql = generateSql(metadata, columns, aClass);
        LOG.debug("SQL: " + sql);
        Collection fields = columnsToFields(columns, metadata);
        executeSql(sql, out, fields, sessions);
    }

    private String generateSql(ServerDmqlMetadata metadata, Collection columns,
                               MClass aClass)
        throws RetsReplyException
    {
        try
        {
            SqlConverter sqlConverter = parse(metadata);
            StringWriter stringWriter = new StringWriter();
            PrintWriter sqlWriter = new PrintWriter(stringWriter);
            sqlWriter.print("SELECT ");
            sqlWriter.print(StringUtils.join(columns.iterator(), ", "));
            sqlWriter.print(" FROM ");
            sqlWriter.print(aClass.getDbTable());
            sqlWriter.print(" WHERE ");
            sqlConverter.toSql(sqlWriter);
            return stringWriter.toString();
        }
        catch (ANTLRException e)
        {
            // This is not an error as bad DMQL can cause this to be thrown.
            LOG.debug("Caught", e);
            throw new RetsReplyException(ReplyCode.INVALID_QUERY_SYNTAX,
                                         e.toString());
        }
    }

    private void executeSql(String sql, PrintWriter out, Collection fields,
                            SessionFactory sessions)
        throws RetsServerException
    {
        if (!mExecuteSql)
        {
            printEmptyRets(out, ReplyCode.NO_RECORDS_FOUND);
            return;
        }

        Session session = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try
        {
            session = sessions.openSession();
            Connection connection = session.connection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql.toString());
            printOpenRetsSuccess(out);
            out.println("<DELIMITER value=\"09\"/>");
            out.print("<COLUMNS>\t");
            out.print(StringUtils.join(fields.iterator(), "\t"));
            out.println("\t</COLUMNS>");
            int rowCount = 0;
            while (resultSet.next())
            {
                out.print("<DATA>\t");
                for (int i = 0; i < fields.size(); i++)
                {
                    out.print(resultSet.getString(i+1) + "\t");
                }
                out.println("\t</DATA>");
                rowCount++;
            }
            printCloseRets(out);
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
    private boolean mExecuteSql;
}
