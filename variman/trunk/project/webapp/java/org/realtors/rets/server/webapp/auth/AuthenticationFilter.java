/*
 */
package org.realtors.rets.server.webapp.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class AuthenticationFilter implements Filter
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
        String uri = request.getRequestURI();
        LOG.debug("Authorizing URI: " + uri);
        if (!uri.startsWith("/rets"))
        {
            filterChain.doFilter(servletRequest, servletResponse);
        }

        String authorizationHeader = request.getHeader("Authorization");
        LOG.debug("Authorizition header: " + authorizationHeader);
        if (authorizationHeader == null)
        {
            send401(response);
            return;
        }

        DigestAuthorizationRequest authorizationRequest =
            new DigestAuthorizationRequest(authorizationHeader);
        authorizationRequest.setUsername("Joe");
        authorizationRequest.setPassword("Schmoe");
        authorizationRequest.setMethod(request.getMethod());
        if (authorizationRequest.verifyResponse())
        {
            LOG.debug("Digest auth succeeded");
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else
        {
            LOG.debug("Invalid digest auth");
            send401(response);
        }
    }

    private void send401(HttpServletResponse response) throws IOException
    {
        String header =
            new DigestAuthenticateResponse("RETS Server").getHeader();
        response.setHeader("WWW-Authenticate", header);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        LOG.debug("Sending 401 with header: " + header);
    }

    public void destroy()
    {
    }

    private static final Logger LOG =
        Logger.getLogger(AuthenticationFilter.class);
}
