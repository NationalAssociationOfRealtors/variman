/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import java.io.PrintWriter;

import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.SearchParameters;
import org.realtors.rets.server.SearchTransaction;
import org.realtors.rets.server.webapp.RetsServletRequest;
import org.realtors.rets.server.webapp.RetsServletResponse;
import org.realtors.rets.server.webapp.WebApp;

import antlr.ANTLRException;
import org.apache.log4j.Logger;

public class SearchHandler extends BaseServletHandler
{
    public String getName()
    {
        return NAME;
    }

    protected void serviceRets(RetsServletRequest request,
                               RetsServletResponse response)
        throws RetsReplyException, IOException
    {
        try
        {
            PrintWriter out = response.getXmlWriter();
            SearchParameters parameters =
                new SearchParameters(request.getParameterMap());
            LOG.debug(parameters);
            SearchTransaction search = new SearchTransaction(parameters);
            LOG.debug(search.getSql(WebApp.getMetadataManager()));
            out.println("<RETS ReplyCode=\"20201\" " +
                        "ReplyText=\"No Records Found\"/>");
        }
        catch (ANTLRException e)
        {
            LOG.debug("Caught", e);
            throw new RetsReplyException(20206, e.toString());
        }
    }

    public static final String NAME = "/search";
    private static final Logger LOG =
        Logger.getLogger(SearchHandler.class);
}
