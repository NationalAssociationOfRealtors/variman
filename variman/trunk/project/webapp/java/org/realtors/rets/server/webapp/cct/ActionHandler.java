/*
 * Variman RETS Server
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

import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.webapp.RetsServletRequest;
import org.realtors.rets.server.webapp.RetsServletResponse;

public class ActionHandler extends BaseServletHandler
{
    public static final String NAME = "/action";

    public String getName()
    {
        return NAME;
    }

    protected void serviceRets(RetsServletRequest request,
                               RetsServletResponse response)
        throws RetsReplyException, IOException
    {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println("Welcome to the RETS Compliance Tester from the Center " +
                    "for REALTOR(R) Technology!");
    }
}
