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
import javax.servlet.http.HttpSession;

import org.realtors.rets.server.PasswordMethod;
import org.realtors.rets.server.User;
import org.apache.log4j.Logger;

/**
 * @web.filter name="authentication-filter"
 *   description="Performs digest authentication"
 * @web.filter-init-param name="user-map"
 *   value="org.realtors.rets.server.webapp.auth.HibernateUserMap"
 *
 *   valuex="org.realtors.rets.server.webapp.auth.AuthenticationFilter"
 */
public class AuthenticationFilter implements Filter, UserMap
{
    public void init(FilterConfig filterConfig) throws ServletException
    {
        instantiateUserMap(filterConfig.getInitParameter("user-map"));
        LOG.info("Using user map: " + mUserMap.getClass().getName());
    }

    private void instantiateUserMap(String userMapName)
    {
        mUserMap = new NullUserMap();
        if (userMapName == null)
        {
            return;
        }

        try
        {
            mUserMap = (UserMap)
                Class.forName(userMapName).newInstance();
        }
        catch (InstantiationException e)
        {
            LOG.warn("Could not instantiate: " + userMapName, e);
        }
        catch (IllegalAccessException e)
        {
            LOG.warn("Could not instantiate: " + userMapName, e);
        }
        catch (ClassNotFoundException e)
        {
            LOG.warn("Could not instantiate: " + userMapName, e);
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
        authorizationRequest.setMethod(request.getMethod());
        if (verifyResponse(authorizationRequest, request.getSession()))
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

    private boolean verifyResponse(DigestAuthorizationRequest request,
                                   HttpSession session)
    {
        User athorizedUser = findAuthorizedUser(request);
        if (athorizedUser != null)
        {
            session.setAttribute(AUTHORIZED_USER_KEY, athorizedUser);
            return true;
        }
        else
        {
            session.removeAttribute(AUTHORIZED_USER_KEY);
            return false;
        }
    }

    private User findAuthorizedUser(DigestAuthorizationRequest request)
    {
        User user = mUserMap.findUser(request.getUsername());
        if (user != null)
        {
            boolean passwordIsA1 =
                user.isPasswordMethod(PasswordMethod.DIGEST_A1);
            if (!request.verifyResponse(user.getPassword(), passwordIsA1))
            {
                user = null;
            }
        }
        return user;
    }

    public User findUser(String username)
    {
        User user = new User();
        user.setPasswordMethod(
            PasswordMethod.getInstance(PasswordMethod.PLAIN_TEXT));
        if (username.equals("Joe"))
        {
            user.setPassword("Schmoe");
        }
        else if (username.equals("test"))
        {
            user.setPassword("test1");
        }
        else
        {
            user = null;
        }
        return user;
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
        mUserMap = null;
    }

    private static final Logger LOG =
        Logger.getLogger(AuthenticationFilter.class);
    public static final String AUTHORIZED_USER_KEY = "authorized_user";
    private UserMap mUserMap;
}
