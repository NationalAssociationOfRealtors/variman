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

import javax.servlet.ServletException;

import org.realtors.rets.server.GetMetadataParameters;
import org.realtors.rets.server.GetMetadataTransaction;
import org.realtors.rets.server.HibernateMetadataFetcher;
import org.realtors.rets.server.MetadataFetcher;
import org.realtors.rets.server.RetsServerException;

/**
 * @web.servlet name="get-metadata-servlet"
 * @web.servlet-mapping  url-pattern="/rets/getMetadata"
 */
public class  GetMetadataServlet extends RetsServlet
{
    public void init() throws ServletException
    {
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

    protected void doRets(RetsServletRequest request,
                          RetsServletResponse response)
        throws RetsServerException, IOException
    {
        PrintWriter out = response.getXmlWriter();
        GetMetadataParameters parameters =
            new GetMetadataParameters(request.getParameterMap());
        GetMetadataTransaction transaction =
            new GetMetadataTransaction(out, parameters, mMetadataFetcher);
        transaction.execute();
    }

    private MetadataFetcher mMetadataFetcher;
    private static final boolean USE_CACHE = true;
}
