/*
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.GetMetadataParameters;
import org.realtors.rets.server.GetMetadataTransaction;
import org.realtors.rets.server.HibernateMetadataFetcher;
import org.realtors.rets.server.ManagerMetadataFetcher;
import org.realtors.rets.server.MetadataFetcher;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.metadata.MetadataManager;

import org.apache.log4j.Logger;

/**
 * @web.servlet name="get-metadata-servlet"
 * @web.servlet-mapping  url-pattern="/rets/getMetadata"
 */
public class  GetMetadataServlet extends RetsServlet
{
    public void init() throws ServletException
    {
        mTransaction = new GetMetadataTransaction();
        if (USE_CACHE)
        {
            mMetadataFetcher = new WebAppMetadataFetcher();
        }
        else
        {
            mMetadataFetcher =
                new HibernateMetadataFetcher(WebApp.getSessions());
        }
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();

        try
        {
            GetMetadataParameters parameters =
                new GetMetadataParameters(request.getParameterMap());
            mTransaction.execute(out, parameters, mMetadataFetcher);
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

    private static final Logger LOG =
        Logger.getLogger(GetMetadataServlet.class);
    private GetMetadataTransaction mTransaction;
    private MetadataFetcher mMetadataFetcher;
    private static final boolean USE_CACHE = true;
}
