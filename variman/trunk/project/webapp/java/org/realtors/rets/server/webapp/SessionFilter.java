/*
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * @web.filter name="session-filter"
 *   description="Handles session management after the user is authenticated"
 */
public class SessionFilter implements Filter
{
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
        throws IOException, ServletException
    {
        if (!(servletRequest instanceof HttpServletRequest))
        {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String contextUrl = getContextUrl(request);
        HttpSession session = request.getSession();
        boolean isSessionValid =
            (session.getAttribute(SESSION_VALID_KEY) != null);
        LOG.debug("contextUrl <" + contextUrl + ">, isSessionValid: " +
                  isSessionValid);
        // Can only goto login if the session is not valid, and can only go
        // to other pages if session valid. Hence the following cases are
        // errors:
        //
        // Someone tries to login with an already valid session
        //
        // Someone tries to hit one of the other URLs without a valid session
        //
        // Anything else is fine, and we let it go.
        if (contextUrl.equals("/rets/login") && isSessionValid)
        {
            LOG.debug("Logging in while session is valid, sending response");
            sendAdditionalLoginNotPermitted(response);
        }
        else if (!contextUrl.equals("/rets/login") && !isSessionValid)
        {
            LOG.debug("Invalid session, sending response");
            sendInvalidSession(response);
        }
        else
        {
            LOG.debug("Session state is expected, continuing");
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private void sendAdditionalLoginNotPermitted(HttpServletResponse response)
        throws IOException
    {
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        out.println("<RETS ReplyCode=\"20022\" " +
                    "ReplyText=\"Additional login not permitted\"/>");
    }

    private void sendInvalidSession(HttpServletResponse response)
        throws IOException
    {
        response.setContentType("text/xml");
//        PrintWriter out = response.getWriter();
//        out.println("<?xml version=\"1.0\"?>");
//        out.println("<!-- Session is invalid -->");
    }

    private String getContextUrl(HttpServletRequest request)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(request.getServletPath());
        String pathInfo = request.getPathInfo();
        if (pathInfo != null)
        {
            buffer.append(pathInfo);
        }
        return buffer.toString();
    }

    public void destroy()
    {
    }

    private static final Logger LOG =
        Logger.getLogger(SessionFilter.class);
    public static final String SESSION_VALID_KEY = "is_session_valid";
}
