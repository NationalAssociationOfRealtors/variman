/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import java.io.PrintWriter;

import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.SearchParameters;
import org.realtors.rets.server.SearchTransaction;
import org.realtors.rets.server.webapp.RetsServletRequest;
import org.realtors.rets.server.webapp.RetsServletResponse;
import org.realtors.rets.server.webapp.WebApp;

import org.apache.log4j.Logger;

public class SearchHandler extends BaseServletHandler
{
    public String getName()
    {
        return NAME;
    }

    protected void serviceRets(RetsServletRequest request,
                               RetsServletResponse response)
        throws RetsServerException, IOException
    {
        PrintWriter out = response.getXmlWriter();
        SearchParameters parameters =
            new SearchParameters(request.getParameterMap(),
                                 request.getRetsVersion());
        LOG.debug(parameters);
        SearchTransaction search = new SearchTransaction(parameters);
        search.setExecuteQuery(false);
        search.execute(out, WebApp.getMetadataManager(), WebApp.getSessions());
    }

    public static final String NAME = "/search";
    private static final Logger LOG =
        Logger.getLogger(SearchHandler.class);
}
