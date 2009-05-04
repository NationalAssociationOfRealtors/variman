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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * @web.servlet name="log4j-servlet"
 * @web.servlet-mapping url-pattern="/log4j"
 */
public class Log4jServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        WebApp.loadLog4j();
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        out.println("Done");
        LOG.warn("Log4j reloaded");
    }

    private static final Logger LOG = Logger.getLogger(Log4jServlet.class);

}
