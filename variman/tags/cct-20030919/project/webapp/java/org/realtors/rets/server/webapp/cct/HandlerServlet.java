/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.User;
import org.realtors.rets.server.webapp.RetsServlet;

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

    private void logRequest(HttpServletRequest request)
    {
        WireLog.LOG.debug("\n--------------------\n");
        WireLog.LOG.debug(request.getMethod() + " " + request.getRequestURI()
                          + "\n");
        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements())
        {
            String name = (String) parameterNames.nextElement();
            String[] values = request.getParameterValues(name);
            WireLog.LOG.debug("Parameter " + name + ": " +
                              Arrays.asList(values) + "\n");
        }
        WireLog.LOG.debug("\n");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doGet(req, resp);
    }

    private static final Logger LOG =
        Logger.getLogger(HandlerServlet.class);
}
