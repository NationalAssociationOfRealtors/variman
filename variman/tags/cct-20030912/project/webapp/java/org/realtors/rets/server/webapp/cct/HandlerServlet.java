/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;

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
                         HttpServletResponse respsponse)
        throws ServletException, IOException
    {
        String name = request.getPathInfo();
        User user = getUser(request.getSession());
        RetsHandlers handlers =
            HandlerManager.getInstance().getHandlers(user.getUsername());
        ServletHandler handler = handlers.getByName(name);
        if (handler != null)
        {
            LOG.debug("Dispatching " + name + " to " +
                      handler.getClass().getName());
            handler.doGet(request, respsponse);
        }
        else
        {
            LOG.warn("No handler for " + name);
            respsponse.sendError(404, request.getRequestURI());
        }
    }

    private static final Logger LOG =
        Logger.getLogger(HandlerServlet.class);
}
