/*
 */
package org.realtors.rets.server;

import java.io.PrintWriter;
import java.io.StringWriter;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
import java.util.List;

//import net.sf.hibernate.HibernateException;
//import net.sf.hibernate.Session;

import org.realtors.rets.server.dmql.DmqlCompiler;
import org.realtors.rets.server.dmql.SqlConverter;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.ServerDmqlMetadata;
import org.realtors.rets.server.metadata.Table;

import antlr.ANTLRException;
//import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class SearchTransaction
{
    public SearchTransaction(SearchParameters parameters)
    {
        mParameters = parameters;
    }

    public String getQuery()
    {
        return mQuery;
    }

    public void setQuery(String query)
    {
        mQuery = query.trim();
    }

    public String getSql(MetadataManager manager) throws ANTLRException
    {
        List classes = manager.find(Table.TABLE,
                                    mParameters.getResourceId() + ":" +
                                    mParameters.getClassName());
        ServerDmqlMetadata metadata =
            new ServerDmqlMetadata(classes, mParameters.isStandardNames());
        SqlConverter sqlConverter = parse(metadata);
        StringWriter stringWriter = new StringWriter();
        sqlConverter.toSql(new PrintWriter(stringWriter));
        return  stringWriter.toString();
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

/*
    public void doSearch(PrintWriter out, MetadataManager manager)
        throws ANTLRException, HibernateException, SQLException
    {
        List tables = manager.find(Table.TABLE,
                                   mParameters.getResourceId() +
                                   ":" + mParameters.getClassName());
        List columns = new ArrayList();
        for (int i = 0; i < tables.size(); i++)
        {
            Table table = (Table) tables.get(i);
            columns.add(table.getDbName());
        }

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append(StringUtils.join(columns.iterator(), ", "));
        sql.append(" FROM ");
        sql.append(getSqlTable());
        if (!mQuery.equals(""))
        {
            sql.append(" WHERE ");
//            sql.append(getSql());
        }
        sql.append(";");

        LOG.debug("SQL=" + sql.toString());

        Session session = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try
        {
            session = InitServlet.openSession();
            Connection connection = session.connection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql.toString());
            out.println("<RETS ReplyCode=\"0\" " +
                        "ReplyText=\"Operation Successful\">");
            out.println("<DELIMITER value=\"09\"/>");
            out.print("<COLUMNS>\t");
            out.print(StringUtils.join(columns.iterator(), "\t"));
            out.println("\t</COLUMNS>");
            int rowCount = 0;
            while (resultSet.next())
            {
                out.print("<DATA>\t");
                for (int i = 0; i < columns.size(); i++)
                {
                    out.print(resultSet.getString(i+1) + "\t");
                }
                out.println("\t</DATA>");
                rowCount++;
            }
            out.println("</RETS>");
            LOG.debug("Row count: " + rowCount);
        }
        finally
        {
            closeResultSet(resultSet);
            closeStatement(statement);
            closeSession(session);
        }
    }

    private void closeSession(Session session)
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

    private void closeStatement(Statement statement)
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

    private void closeResultSet(ResultSet resultSet)
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

    private String getSqlTable()
    {
        return "rets_" + mParameters.getResourceId().toLowerCase() + "_" +
            mParameters.getClassName().toLowerCase();
    }

    private static final Logger LOG =
        Logger.getLogger(SearchTransaction.class);
        */
    private static final Logger LOG =
        Logger.getLogger(SearchTransaction.class);
    private String mQuery;
    private SearchParameters mParameters;
}
