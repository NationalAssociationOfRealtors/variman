/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.GetMetadataParameters;
import org.realtors.rets.server.GetMetadataTransaction;
import org.realtors.rets.server.ManagerMetadataFetcher;
import org.realtors.rets.server.MetadataFetcher;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.webapp.WebApp;

import org.apache.log4j.Logger;

public class GetMetadataHandler extends BaseServletHandler
{
    public GetMetadataHandler()
    {
        mTransaction = new GetMetadataTransaction();
        mMetadataFetcher = new ServletMetadataFetcher();
    }

    public String getName()
    {
        return NAME;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        super.doGet(request, response);
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();

        try
        {
            GetMetadataParameters parameters =
                new GetMetadataParameters(request.getParameterMap());
            mTransaction.execute(out, parameters, mMetadataFetcher);
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

    /**
     * Gets the metadata manager from the servlet request.
     */
    class ServletMetadataFetcher extends ManagerMetadataFetcher
    {
        protected MetadataManager getMetadataManager()
        {
            return WebApp.getMetadataManager();
        }
    }

    public static final String NAME = "/getMetadata";
    private static final Logger LOG =
        Logger.getLogger(GetMetadataHandler.class);
    private GetMetadataTransaction mTransaction;
    private MetadataFetcher mMetadataFetcher;
}
