/*
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.SearchParameters;
import org.realtors.rets.server.SearchTransaction;
import org.realtors.rets.server.RetsVersion;

import antlr.ANTLRException;
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
        LOG.debug("Buffer size: " + response.getBufferSize());

        try
        {
            SearchParameters parameters =
                new SearchParameters(request.getParameterMap(),
                                     RetsVersion.RETS_1_0);
            LOG.debug(parameters);
            SearchTransaction search = new SearchTransaction(parameters);
//            search.setResourceId(request.getParameter("SearchType"));
//            search.setClassName(request.getParameter("Class"));
//            search.setQueryType(request.getParameter("QueryType"));
//            search.setQuery(request.getParameter("Query"));
//            search.setFormat(request.getParameter("Format"));
//            search.doSearch(out, getMetadataManager());
            LOG.debug(search.getSql(getMetadataManager()));
            out.println("<RETS ReplyCode=\"20201\" " +
                        "ReplyText=\"No Records Found\"/>");
        }
        catch (ANTLRException e)
        {
            LOG.error("Caught", e);
            out.println("<RETS ReplyCode=\"20206\" ReplyText=\"" +
                        e.toString() + "\"/>");
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
