/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.PrintWriter;

import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.SearchParameters;
import org.realtors.rets.server.SearchTransaction;
import org.realtors.rets.server.RetsServer;

import org.apache.log4j.Logger;

/**
 * @web.servlet name="search-servlet"
 * @web.servlet-mapping  url-pattern="/rets/search"
 */
public class SearchServlet extends RetsServlet
{
    protected void doRets(RetsServletRequest request,
                          RetsServletResponse response)
        throws RetsServerException, IOException
    {
        PrintWriter out = response.getXmlWriter();
        SearchParameters parameters =
            new SearchParameters(request.getParameterMap(),
                                 request.getRetsVersion());
        LOG.debug(parameters);
        SearchTransaction search = new SearchTransaction(parameters);
        search.execute(out, WebApp.getMetadataManager(),
                       RetsServer.getSessions());
    }

    private static final Logger LOG =
        Logger.getLogger(SearchServlet.class);
}
