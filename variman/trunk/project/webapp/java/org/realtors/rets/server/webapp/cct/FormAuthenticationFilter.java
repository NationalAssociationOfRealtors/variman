package org.realtors.rets.server.webapp.cct;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.realtors.rets.server.User;

import org.apache.log4j.MDC;

/**
 * This filter makes sure that people are authenticated before they
 * enter into the private area.  It redirects to "/login.jsp" with
 * the argument of "done=<URL ORIGINALLY SPECIFIED>".
 *
 * @web:filter name="Form Authentication Filter"
 * @web:filter-mapping url-pattern="/cct/*"
 */
public class FormAuthenticationFilter implements Filter, CctConstants
{
    /**
     * initializes the authentication filter.
     *
     * @param config the FilterConfig object that this Filter should use
     *
     * @exception ServletException if an error occurs
     */
    public void init(FilterConfig config)
        throws ServletException
    {
        mConfig = config;
    }

    /**
     * Cleans up any resources the filter may have taken.
     */
    public void destroy()
    {
        mConfig = null;
    }

    /**
     * Checks to see if AUTHENTICATION_KEY is set in the session. If
     * not it redirects to "/login.jsp?done=<ORIGINAL URL CALL>".  If
     * AUTHENTICATION_KEY is present it passes the request and
     * response to the next level of the FilterChain.
     *
     * @param servletRequest the request we're filtering.
     * @param servletResponse the response of the servlet transaction
     * @param chain the filter chain
     *
     * @exception IOException if an error occurs
     * @exception ServletException if an error occurs
     */
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain chain)
        throws IOException, ServletException
    {
        if (!(servletRequest instanceof HttpServletRequest))
        {
            chain.doFilter(servletRequest, servletResponse);
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response =
            (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        if ((session == null) ||
            (session.getAttribute(AUTHENTICATION_KEY) == null))
        {
            StringBuffer done = new StringBuffer(request.getServletPath());
            String query = request.getQueryString();
            if (query != null)
            {
                done.append("?").append(query);
            }
            response.sendRedirect(request.getContextPath() +
                                  "/login.jsp?done=" + done.toString());
            return;
        }

        // User is authenticated
        User user = (User) session.getAttribute(USER_KEY);
        MDC.put("user", user.getUsername());
        MDC.put("addr", request.getRemoteAddr());
        chain.doFilter(servletRequest, servletResponse);
        MDC.remove("user");
        MDC.remove("addr");
    }

    /** The configuration for the filter. */
    private FilterConfig mConfig;
}
