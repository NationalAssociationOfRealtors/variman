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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.User;
import org.realtors.rets.server.webapp.RetsServlet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Services all client validation handlers
 *
 * @web.servlet name="cct-handler-servlet"
 * @web.servlet-mapping url-pattern="/rets/cct/*"
 */
public class HandlerServlet extends RetsServlet
{
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException
    {
        if (WireLog.LOG.isDebugEnabled())
        {
            logRequest(request);
            response = new LoggingHttpServletResponse(response, WireLog.LOG);
        }
        String name = request.getPathInfo();
        User user = getUser(request.getSession());
        RetsHandlers handlers =
            HandlerManager.getInstance().getHandlers(user.getUsername());
        ServletHandler handler = handlers.getByName(name);
        if (handler != null)
        {
            LOG.debug("Dispatching " + name + " to " +
                      handler.getClass().getName());
            handler.doGet(request, response);
        }
        else
        {
            LOG.warn("No handler for " + name);
            response.sendError(404, request.getRequestURI());
        }
    }

    private static void wireLog(String message)
    {
        WireLog.LOG.debug(message + "\n");
    }

    private void logRequest(HttpServletRequest request)
    {
        wireLog("\n--------------------");
        SimpleDateFormat format =
            new SimpleDateFormat("dd MMM yyyy HH:mm:ss.SSS");
        wireLog(format.format(new Date()));
        wireLog(request.getMethod() + " " + request.getRequestURI());

        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements())
        {
            String name = (String) parameterNames.nextElement();
            String[] values = request.getParameterValues(name);
            wireLog("Parameter " + name + ": " + StringUtils.join(values, ","));
        }

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements())
        {
            String name = (String) headerNames.nextElement();
            Enumeration values = request.getHeaders(name);
            while (values.hasMoreElements())
            {
                String value = (String) values.nextElement();
                wireLog(name + ": " + value);
            }
        }
        wireLog("");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doGet(req, resp);
    }

    private static final Logger LOG =
        Logger.getLogger(HandlerServlet.class);
}
