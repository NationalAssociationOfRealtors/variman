/*
 * Variman RETS Server
 *
 * Author: RealGo
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.realtors.rets.server.RequestLogEntry;
import org.realtors.rets.server.RequestLogLogger;
import org.realtors.rets.server.User;
import org.realtors.rets.server.io.ByteCounter;
import org.realtors.rets.server.protocol.SearchTransactionStatistics;
import org.realtors.rets.server.webapp.auth.AuthenticationFilter;


/**
 * A Servlet {@link Filter} which creates a new {@link RequestLogEntry} and
 * makes it available down the filter chain.
 * <p>
 * NOTE: This filter should be configured to execute after the
 * {@link AuthenticationFilter}. This filter attempts to find user information
 * supplied by the {@link AuthenticationFilter}.
 * 
 * @author Danny
 */
public class RequestLogFilter implements Filter {

    /**
     * The request attribute key where the {@link RequestLogEntry} for the
     * current request is stored.
     */
    public static final String REQUESTLOGENTRY_KEY = "org.realtors.rets.server.webapp.RequestLogFilter.RequestLogEntry";

    private static final Logger LOG = Logger.getLogger(RequestLogFilter.class);

    // Configuration Variables -----------------------------------------------
    private RequestLogLogger requestLogLogger;

    // State Variables -------------------------------------------------------
    private String localHostName;

    /**
     * Returns the requestLogLogger.
     * 
     * @return The requestLogLogger.
     */
    public RequestLogLogger getRequestLogLogger() {
        return this.requestLogLogger;
    }

    /**
     * Sets the requestLogLogger to the specified value.
     * 
     * @param requestLogLogger The requestLogLogger to set.
     */
    public void setRequestLogLogger(RequestLogLogger requestLogLogger) {
        this.requestLogLogger = requestLogLogger;
    }

    /*- (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing to initialize.
    }

    /*- (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
        // Nothing to destroy.
    }

    /*- (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        RequestLogEntry requestLogEntry = new RequestLogEntry();
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        HttpSession session = request.getSession(false);
        
        /*
         * Make the request log available throughout the current request.
         */
        request.setAttribute(REQUESTLOGENTRY_KEY, requestLogEntry);
        
        long startTime = System.currentTimeMillis();
        Date now = new Date(startTime);
        
        ServletResponse countingServletResponse = ServletUtils.makeCountable(response);
        try {
            chain.doFilter(servletRequest, countingServletResponse);
        } finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            ByteCounter byteCounter = (ByteCounter)countingServletResponse;
            
            String localHostName = getLocalHostName();
            String remoteHostName = request.getRemoteHost();
            
            // TODO: Get HTTP Response Status.
            Long userId = null;
            String username = null;
            User user = (User)getAttribute(session, AuthenticationFilter.AUTHORIZED_USER_KEY);
            if (user != null) {
                userId = user.getId();
                username = user.getUsername();
            }

            String retsSessionId = null;
            Cookie retsSessionIdCookie = getCookieByName(request, "RETS-Session-ID");
            if (retsSessionIdCookie != null) {
                retsSessionId = retsSessionIdCookie.getValue();
            }
            String userAgent = request.getHeader("User-Agent");
            String retsVersion = request.getHeader("RETS-Version");
            String requestUri = request.getRequestURI();
            Map<?, ?> requestParameterMap = request.getParameterMap();
            long byteCount = byteCounter.getByteCount();
            
            SearchTransactionStatistics searchTransactionStatistics = (SearchTransactionStatistics)request.getAttribute(SearchServlet.SEARCH_TRANSACTION_STATISTICS_KEY);

            requestLogEntry.put("localHostName", localHostName);
            requestLogEntry.put("request", request);
            requestLogEntry.put("remoteHostName", remoteHostName);
            requestLogEntry.put("userId", userId);
            requestLogEntry.put("username", username);
            requestLogEntry.put("retsSessionId", retsSessionId);
            requestLogEntry.put("userAgent", userAgent);
            requestLogEntry.put("retsVersion", retsVersion);
            requestLogEntry.put("requestUri", requestUri);
            requestLogEntry.put("requestParameterMap", requestParameterMap);
            if (searchTransactionStatistics != null) {
                requestLogEntry.put("searchTransactionStatistics", searchTransactionStatistics);
            }
            requestLogEntry.put("byteCount", Long.valueOf(byteCount));
            requestLogEntry.put("duration", Long.valueOf(duration));
            requestLogEntry.put("now", now);
            RequestLogLogger requestLogLogger = getRequestLogLogger();
            requestLogLogger.logEntry(requestLogEntry);
        }
    }

    /*
     * Lazily resolves the local host name and returns the cached value on
     * subsequent calls.
     */
    private String getLocalHostName() {
        if (this.localHostName == null) {
            this.localHostName = "?";
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                this.localHostName = localHost.getHostName();
            } catch (UnknownHostException e) {
                LOG.warn(e);
            }
        }
        return this.localHostName;
    }

    private static Cookie getCookieByName(HttpServletRequest request, String cookieName) {
        Cookie namedCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (name != null) {
                    name = name.trim();
                } else {
                    name = "";
                }
                if (name.equals(cookieName)) {
                    namedCookie = cookie;
                    break;
                }
            }
        }
        return namedCookie;
    }

    private static Object getAttribute(HttpSession session, String attributeKey) {
        if (session == null) {
            return null;
        }
        Object obj = null;
        try {
            obj = session.getAttribute(attributeKey);
        } catch (IllegalStateException e) {
            // Ignore. Session was invalidated.
        }
        return obj;
    }

}
