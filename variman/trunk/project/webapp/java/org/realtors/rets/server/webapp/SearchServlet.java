/*
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.RetsVersion;
import org.realtors.rets.server.SearchParameters;
import org.realtors.rets.server.SearchTransaction;

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
        response.setBufferSize(64*1024);
        LOG.debug("Buffer size: " + response.getBufferSize());

        try
        {
            SearchParameters parameters =
                new SearchParameters(request.getParameterMap(),
                                     RetsVersion.RETS_1_0);
            LOG.debug(parameters);
            SearchTransaction search = new SearchTransaction(parameters);
            search.execute(out, WebApp.getMetadataManager(),
                           WebApp.getSessions());
        }
        catch (RetsReplyException e)
        {
            out.println("<RETS ReplyCode=\"" + e.getReplyCode() +
                        "\" ReplyText=\"" + e.getMeaning() + "\"/>\n");
        }
        catch (Exception e)
        {
            LOG.error("Caught", e);
            out.println("<RETS ReplyCode=\"20513\" " +
                        "ReplyText=\"Miscellaneous error\"/>\n");
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doGet(req, resp);
    }

    private static final Logger LOG =
        Logger.getLogger(SearchServlet.class);
}
