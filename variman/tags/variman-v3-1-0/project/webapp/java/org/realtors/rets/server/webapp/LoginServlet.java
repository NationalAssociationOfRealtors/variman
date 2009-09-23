/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;

import org.realtors.rets.client.RetsVersion;

import org.realtors.rets.server.AccountingStatistics;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.User;
import org.realtors.rets.server.RetsUtils;
import org.realtors.rets.server.MetadataFetcher;

import org.apache.log4j.Logger;

/**
 * Performs all necessary one-time initializations for the web
 * application.
 *
 * @web.servlet name="login-servlet"
 * @web.servlet-mapping url-pattern="/rets/login"
 */
public class LoginServlet extends RetsServlet
{
    public void init() throws ServletException
    {
        mMetadataFetcher = getMetadataFetcher();
    }

    protected void doRets(RetsServletRequest request,
                          RetsServletResponse response)
        throws RetsServerException, IOException
    {
        StringBuffer contextPath = ServletUtils.getContextPath(request);
        LOG.debug("context=" + contextPath);

        HttpSession session = request.getSession();
        SessionFilter.validateSession(session);
        AccountingStatistics statitics = getStatistics(session);
        statitics.startSession();
        User user = getUser(session);
        String version = mMetadataFetcher.getSystemVersion();
        Date date = mMetadataFetcher.getSystemDate();
        RetsVersion retsVersion = request.getRetsVersion();
        
        PrintWriter out = response.getXmlWriter();
        RetsUtils.printOpenRetsSuccess(out);
        if (retsVersion.notEquals(RetsVersion.RETS_1_0) || WebApp.getHPMAMode())
        {
            RetsUtils.printOpenRetsResponse(out);
        }

        // HPMA needs relative URLs
        if (WebApp.getHPMAMode()) {
            contextPath.replace(0, contextPath.length(), "/downloads");
        }
        
        out.println("Broker = " + user.getBrokerCode());
        out.println("MemberName = " + user.getName());
        out.println("MetadataVersion = " + version);
        out.println("MinMetadataVersion = " + version);
        if (!retsVersion.equals(RetsVersion.RETS_1_0) && 
            !retsVersion.equals(RetsVersion.RETS_1_5))
        {
            SimpleDateFormat formatter =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            
            if (retsVersion.equals(RetsVersion.RETS_1_7))
                formatter.applyPattern("E, d MMM yyyy HH:mm:ss z");
            
            out.println("MetadataTimestamp = " + formatter.format(date));
            out.println("MinMetadataTimestamp = " + formatter.format(date));
        }
        out.println("User = " + user.getUsername() + ",NULL,NULL,NULL");
        out.println("Login = " + contextPath + Paths.LOGIN);
        out.println("Logout = " + contextPath + Paths.LOGOUT);
        out.println("Search = " + contextPath + Paths.SEARCH);
        out.println("GetMetadata = " + contextPath + Paths.GET_METADATA);
        out.println("GetObject = " + contextPath + Paths.GET_OBJECT);
        out.println("Balance = " + statitics.getTotalBalanceFormatted());
        out.println("TimeoutSeconds = " + session.getMaxInactiveInterval());
        if (request.getRetsVersion().notEquals(RetsVersion.RETS_1_0) || WebApp.getHPMAMode())
        {
            RetsUtils.printCloseRetsResponse(out);
        }
        RetsUtils.printCloseRets(out);
    }

    private static final Logger LOG =
        Logger.getLogger(LoginServlet.class);
    private MetadataFetcher mMetadataFetcher;
}
