/*
 */
package org.realtors.rets.server.webapp;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import net.sf.hibernate.Session;
import net.sf.hibernate.HibernateException;

import org.realtors.rets.server.dmql.DmqlCompiler;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.Table;

import antlr.ANTLRException;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class SearchAction
{
    public String getResourceId()
    {
        return mResourceId;
    }

    public void setResourceId(String resourceId)
    {
        mResourceId = resourceId;
    }

    public String getClassName()
    {
        return mClassName;
    }

    public void setClassName(String className)
    {
        mClassName = className;
    }

    public String getQueryType()
    {
        return mQueryType;
    }

    public void setQueryType(String queryType) throws RetsReplyException
    {
        if (!queryType.equals("DMQL2"))
        {
            throw new RetsReplyException(20203, "Miscellaneous Search Error");
        }
        mQueryType = queryType;
    }

    public String getQuery()
    {
        return mQuery;
    }

    public void setQuery(String query)
    {
        mQuery = query.trim();
    }

    public String getFormat()
    {
        return mFormat;
    }

    public void setFormat(String format)
    {
        mFormat = format;
    }

    public String getSql() throws ANTLRException
    {
        return DmqlCompiler.dmqlToSql(mQuery);
    }

    public String toString()
    {
        return new ToStringBuilder(this)
            .append("resourceId", mResourceId)
            .append("className", mClassName)
            .append("queryType", mQueryType)
            .append("query", mQuery)
            .append("format", mFormat)
            .toString();
    }

    public void doSearch(PrintWriter out, MetadataManager manager)
        throws ANTLRException, HibernateException, SQLException
    {
        List tables = manager.find(Table.TABLE, mResourceId + ":" + mClassName);
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
            sql.append(getSql());
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
            LOG.error("Caugtht", e);
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
        return "rets_" + mResourceId.toLowerCase() + "_" +
            mClassName.toLowerCase();
    }

    private static final Logger LOG =
        Logger.getLogger(SearchAction.class);
    private String mResourceId;
    private String mClassName;
    private String mQueryType;
    private String mQuery;
    private String mFormat;
}
