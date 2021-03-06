/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.webapp.RetsServlet;

/**
 * @web.servlet name="reset-servlet"
 * @web.servlet-mapping url-pattern="/reset"
 */
public class ResetServlet extends RetsServlet
{
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        String header =
            new DigestAuthenticateResponse("RETS Server").getHeader();
        resp.setHeader("WWW-Authenticate", header);
        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
