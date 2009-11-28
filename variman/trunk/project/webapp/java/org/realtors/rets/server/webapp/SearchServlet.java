/*
 * Variman RETS Server
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

import org.apache.log4j.Logger;

import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.protocol.SearchParameters;
import org.realtors.rets.server.protocol.SearchTransaction;
import org.realtors.rets.server.protocol.SearchTransactionStatistics;

/**
 * @web.servlet name="search-servlet"
 * @web.servlet-mapping  url-pattern="/rets/search"
 */
public class SearchServlet extends RetsServlet
{
    public static final String SEARCH_TRANSACTION_STATISTICS_KEY = SearchTransactionStatistics.class.getName();

    protected void doRets(RetsServletRequest request,
                          RetsServletResponse response)
        throws RetsServerException, IOException
    {
        PrintWriter out = response.getXmlWriter();
        SearchParameters parameters =
            new SearchParameters(request.getParameterMap(),
                                 request.getRetsVersion(),
                                 getUser(request.getSession()));
        LOG.info(parameters);
        SearchTransaction search = RetsServer.createSearchTransaction();
        search.setParameters(parameters);
        SearchTransactionStatistics searchTransactionStatistics =
            search.execute(out, RetsServer.getMetadataManager(), RetsServer.getSessions());
        request.setAttribute(SEARCH_TRANSACTION_STATISTICS_KEY, searchTransactionStatistics);
    }

    private static final Logger LOG =
        Logger.getLogger(SearchServlet.class);
}
