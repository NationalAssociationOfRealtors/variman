/*
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.Session;

import org.apache.log4j.Logger;

/**
 * @web.servlet name="search-servlet"
 * @web.servlet-mapping  url-pattern="/rets/search"
 */
public class SearchServlet extends RetsServlet
{
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();

        try
        {
            SearchAction search = new SearchAction();
            search.setResourceId(request.getParameter("SearchType"));
            search.setClassName(request.getParameter("Class"));
            search.setQueryType(request.getParameter("QueryType"));
            search.setQuery(request.getParameter("Query"));
            search.setFormat(request.getParameter("Format"));
            LOG.debug(search);
            search.doSearch(out, getMetadataManager());
        }
        catch(RetsReplyException e)
        {
            out.println("<RETS ReplyCode=\"" + e.getReplyCode() +
                        "\" ReplyText=\"" + e.getMeaning() + "\"/>\n");
        }
        catch(Exception e)
        {
            LOG.error("Caught", e);
            out.println("<RETS ReplyCode=\"20513\" " +
                        "ReplyText=\"Miscellaneous error\"/>\n");
        }
    }

    private static final Logger LOG =
        Logger.getLogger(SearchServlet.class);
}
