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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.tomcat.EmbeddedTomcat;

/**
 * web.servlet name="stop-servlet"
 * web.servlet-mapping url-pattern="/stop"
 */
public class StopServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>RETS Stop Servlet</title>");
        out.println("</head>");
        out.println();
        out.println("<body>");
        out.println("<h1 align='center'>RETS Stop Servlet</h1>");
        out.println("<p>This is the RETS stop servlet.</p>");
        out.println("</body>");
        out.println("</html>");
        out.flush();

        EmbeddedTomcat tomcat = EmbeddedTomcat.getInstance();
        try
        {
            tomcat.stopTomcat();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
