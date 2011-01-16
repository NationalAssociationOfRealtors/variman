/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.SortedSet;
import java.util.Iterator;

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
import org.realtors.rets.server.ReplyCode;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.RetsUtils;
import org.realtors.rets.server.User;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.UserUtils;
import org.realtors.rets.server.Group;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.config.SecurityConstraints;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.config.TimeRestriction;
import org.realtors.rets.server.webapp.WebApp;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import org.hibernate.HibernateException;

/**
 * @web.filter name="authentication-filter"
 *   description="Performs digest authentication"
 * @web.filter-init-param name="user-map"
 *   value="org.realtors.rets.server.webapp.auth.HibernateUserMap"
 */
public class AuthenticationFilter implements Filter, UserMap
{
    public void init(FilterConfig filterConfig) throws ServletException
    {
        instantiateUserMap(filterConfig.getInitParameter("user-map"));
        LOG.debug("Using user map: " + mUserMap.getClass().getName());
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
        HttpServletResponse response =
            (HttpServletResponse) servletResponse;
        try
        {
            doAuthentication(filterChain, request, response);
        }
        catch (RetsReplyException e)
        {
            if (response.isCommitted())
            {
                // Nothing we can report to the client, so make sure to log
                // this
                LOG.error("Caught", e);
            }
            else
            {
                response.reset();
                LOG.debug("Caught", e);
                response.setContentType("text/xml");
                PrintWriter out = response.getWriter();
                RetsUtils.printEmptyRets(out, e.getReplyCode(), e.getMeaning());
            }
        }
        catch (ServletException e)
        {
            LOG.error("Caught", e);
            throw e;
        }
        catch (Throwable e)
        {
            LOG.error("Caught", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        finally
        {
            MDC.remove("addr");
            MDC.remove("user");
            MDC.remove("user-agent");
        }
    }

    private void doAuthentication(FilterChain filterChain,
                                  HttpServletRequest request,
                                  HttpServletResponse response)
        throws IOException, ServletException, RetsServerException
    {
        MDC.put("addr", request.getRemoteAddr());
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.length() == 0) {
            throw new RetsReplyException(ReplyCode.MISC_ERROR, "Missing required User-Agent request header. See the 'Required Client Request Header Fields' section in the RETS specification.");
        }
        MDC.put("user-agent", userAgent);
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String method = request.getMethod();
        if (LOG.isDebugEnabled())
        {
            LOG.debug(WebApp.SERVER_NAME + " version " + WebApp.getVersion());
            LOG.debug("Authorizing URI: " + method + " " + uri + " " + query);
            LOG.debug("User-Agent: " + userAgent);
        }
        String retsPrefix = request.getContextPath() + "/rets";
        String cctPrefix = request.getContextPath() + "/cct";
        String hpmaPrefix  = request.getContextPath() + "/server";
        if (!uri.startsWith(retsPrefix) && !uri.startsWith(cctPrefix) && !uri.startsWith(hpmaPrefix))
        {
            filterChain.doFilter(request, response);
            return;
        }

        try
        {
            String authorizationHeader = request.getHeader("Authorization");
            DigestAuthorizationRequest authorizationRequest =
                new DigestAuthorizationRequest(authorizationHeader, method,
                                               uri, query);
            HttpSession session = request.getSession();
            if (!verifyResponse(authorizationRequest, session))
            {
                send401(NOT_STALE, response);
                return;
            }

            if (!WebApp.getNonceTable().validateRequest(authorizationRequest))
            {
                // A valid digest response but an invalid nonce means the user
                // name and password validate, but we don't know about the
                // nonce. Sending a 401 creates a new nonce, forcing the client
                // to reauthenticate. This stops reply attacks. Setting stale
                // tells the client not to re-prompt the user for credentials.
                // If the nonce was truly just out of date, the client should be
                // able to generate a valid response.
                send401(STALE, response);
                return;
            }

            User user = (User) session.getAttribute(AUTHORIZED_USER_KEY);
            MDC.put("user", user.getUsername());
            LOG.info("Digest auth succeeded for URI: " + method + " " + uri);

            filterChain.doFilter(request, response);
        }
        catch (IllegalArgumentException e)
        {
            LOG.error("Caught", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                               e.getMessage());
        }
    }

    private boolean verifyResponse(DigestAuthorizationRequest request,
                                   HttpSession session)
        throws RetsServerException
    {
        User authorizedUser = findAuthorizedUser(request);
        if (authorizedUser == null)
        {
            session.removeAttribute(AUTHORIZED_USER_KEY);
            return false;
        }

        if (!verifyTimeRestriction(authorizedUser))
        {
            LOG.info("User <" + authorizedUser.getUsername() +
                     "> does not have access during this time period");
            session.removeAttribute(AUTHORIZED_USER_KEY);
            return false;
        }

        session.setAttribute(AUTHORIZED_USER_KEY, authorizedUser);
        return true;
    }

    private User findAuthorizedUser(DigestAuthorizationRequest request)
        throws RetsServerException
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
        if (username == null)
        {
            return null;
        }

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

    private boolean verifyTimeRestriction(User user)
        throws RetsServerException
    {
        try
        {
            boolean allowed = true;
            SortedSet groups = UserUtils.getGroups(user);
            SecurityConstraints securityConstraints =
                RetsServer.getSecurityConstraints();
            for (Iterator iterator = groups.iterator(); iterator.hasNext();)
            {
                Group group = (Group) iterator.next();
                GroupRules rules =
                    securityConstraints.getRulesForGroup(group.getName());
                TimeRestriction timeRestriction = rules.getTimeRestriction();
                if ((timeRestriction != null) &&
                    !timeRestriction.isAllowedNow())
                {
                    LOG.info("Group <" + group.getName() + "> does not " +
                             "allow access at this time.");
                    allowed = false;
                    break;
                }
            }
            return allowed;
        }
        catch (HibernateException e)
        {
            throw new RetsServerException(e);
        }
    }

    private void send401(boolean stale, HttpServletResponse response)
        throws IOException
    {
        NonceTable nonceTable = WebApp.getNonceTable();
        String nonce = nonceTable.generateNonce();
        String opaque = nonceTable.getOpaque(nonce);
        DigestAuthenticateResponse authenticateResponse =
            new DigestAuthenticateResponse("RETS Server");
        authenticateResponse.setNonce(nonce);
        authenticateResponse.setOpaque(opaque);
        authenticateResponse.setStale(stale);
        String header = authenticateResponse.getHeader();
        response.setHeader("WWW-Authenticate", header);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        LOG.debug("Sending 401 with header: " + header);
    }

    public void destroy()
    {
        mUserMap = null;
    }

    private static final boolean STALE = true;
    private static final boolean NOT_STALE = false;
    private static final Logger LOG =
        Logger.getLogger(AuthenticationFilter.class);
    public static final String AUTHORIZED_USER_KEY = "authorized_user";
    private UserMap mUserMap;
}
