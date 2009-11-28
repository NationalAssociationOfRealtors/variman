/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.server.GetMetadataParameters;
import org.realtors.rets.server.GetMetadataTransaction;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.User;

/**
 * @web.servlet name="get-metadata-servlet"
 * @web.servlet-mapping  url-pattern="/rets/getMetadata"
 */
public class  GetMetadataServlet extends RetsServlet
{
    protected void doRets(RetsServletRequest request,
                          RetsServletResponse response)
        throws RetsServerException, IOException
    {
        PrintWriter out = response.getXmlWriter();
        User user = getUser(request.getSession());
        RetsVersion retsVersion = request.getRetsVersion();
        GetMetadataParameters parameters =
            new GetMetadataParameters(request.getParameterMap(), user);
        GetMetadataTransaction transaction =
            new GetMetadataTransaction(out, parameters, RetsServer.getMetadataManager(), retsVersion);
        if (retsVersion.equals(RetsVersion.RETS_1_5))
        {
            // For RETS 1.5, getMetadata needs a Content-ID header containing the
            // name of the first metadata item returned.
            String metadataType = parameters.getType();
            if (metadataType.length() > 0) {
                response.setHeader("Content-ID", "METADATA-" + metadataType);
            }
        }
        transaction.execute();
    }
}
