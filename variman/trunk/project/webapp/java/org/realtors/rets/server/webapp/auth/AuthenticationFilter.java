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

/**
 * @web.filter name="authentication-filter"
 *   description="Performs digest authentication"
 * @web.filter-init-param name="password-map"
 *   value="org.realtors.rets.server.webapp.auth.AuthenticationFilter"
 */
public class AuthenticationFilter implements Filter, PasswordMap
{
    public void init(FilterConfig filterConfig) throws ServletException
    {
        instantiatePasswordMap(filterConfig.getInitParameter("password-map"));
        LOG.info("Using password map: " + mPasswordMap.getClass().getName());
    }

    private void instantiatePasswordMap(String passwordMapName)
    {
        mPasswordMap = new NullPasswordMap();
        if (passwordMapName == null)
        {
            return;
        }

        try
        {
            mPasswordMap = (PasswordMap)
                Class.forName(passwordMapName).newInstance();
        }
        catch (InstantiationException e)
        {
            LOG.warn("Could not instantiate: " + passwordMapName, e);
        }
        catch (IllegalAccessException e)
        {
            LOG.warn("Could not instantiate: " + passwordMapName, e);
        }
        catch (ClassNotFoundException e)
        {
            LOG.warn("Could not instantiate: " + passwordMapName, e);
        }
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
        LOG.debug("Authorization header: " + authorizationHeader);
        if (authorizationHeader == null)
        {
            send401(response);
            return;
        }

        DigestAuthorizationRequest authorizationRequest =
            new DigestAuthorizationRequest(authorizationHeader);
        authorizationRequest.setPassword(getPassword(authorizationRequest));
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

    private String getPassword(DigestAuthorizationRequest authorizationRequest)
    {
        return mPasswordMap.getPassword(authorizationRequest.getUsername());
    }

    public String getPassword(String username)
    {
        if (username.equals("Joe"))
            return "Schmoe";
        else if (username.equals("test"))
            return "test1";
        else
            return null;
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
        mPasswordMap = null;
    }

    private static final Logger LOG =
        Logger.getLogger(AuthenticationFilter.class);
    private PasswordMap mPasswordMap;
}
