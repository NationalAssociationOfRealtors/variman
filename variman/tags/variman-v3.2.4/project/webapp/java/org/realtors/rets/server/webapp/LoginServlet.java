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
import org.realtors.rets.client.RetsVersion;

import org.realtors.rets.server.AccountingStatistics;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.User;
import org.realtors.rets.server.RetsUtils;
import org.realtors.rets.server.metadata.MetadataManager;

import org.apache.commons.lang.StringEscapeUtils;
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
        MetadataManager metadataManager = RetsServer.getMetadataManager();
        String version = metadataManager.getSystemVersion();
        Date date = metadataManager.getSystemDate();
        RetsVersion retsVersion = request.getRetsVersion();
        
        PrintWriter out = response.getXmlWriter();
        RetsUtils.printOpenRetsSuccess(out);
        if (retsVersion.notEquals(RetsVersion.RETS_1_0) || WebApp.getHPMAMode())
        {
            RetsUtils.printOpenRetsResponse(out);
        }

        out.println("Broker = " + StringEscapeUtils.escapeXml(user.getBrokerCode()));
        out.println("MemberName = " + StringEscapeUtils.escapeXml(user.getName()));
        out.println("MetadataVersion = " + version);
        out.println("MinMetadataVersion = " + version);
        if (!retsVersion.equals(RetsVersion.RETS_1_0) && 
            !retsVersion.equals(RetsVersion.RETS_1_5))
        {
            SimpleDateFormat formatter =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            
            if (retsVersion.equals(RetsVersion.RETS_1_7)) {
                formatter.applyPattern("E, d MMM yyyy HH:mm:ss z");
            }
            
            out.println("MetadataTimestamp = " + formatter.format(date));
            out.println("MinMetadataTimestamp = " + formatter.format(date));
        }
        out.println("User = " + StringEscapeUtils.escapeXml(user.getUsername()) + ",NULL,NULL,NULL");
        String loginPath = request.getServletPath();
        if (WebApp.isHPMALoginPath(loginPath)) {
            // HPMA needs relative URLs
            contextPath.replace(0, contextPath.lastIndexOf("/"), "");
            out.println("Login = " + contextPath + loginPath);
            // now construct the remaining URLs from the incoming path
            contextPath.append(loginPath.substring(0, loginPath.indexOf("login")));
            out.println("Logout = " + contextPath + "logout");
            out.println("Search = " + contextPath + "search");
            out.println("GetMetadata = " + contextPath + "getMetadata");
            out.println("GetObject = " + contextPath + "getObject");
        } else {
            out.println("Login = " + contextPath + Paths.LOGIN);
            out.println("Logout = " + contextPath + Paths.LOGOUT);
            out.println("Search = " + contextPath + Paths.SEARCH);
            out.println("GetMetadata = " + contextPath + Paths.GET_METADATA);
            out.println("GetObject = " + contextPath + Paths.GET_OBJECT);
        }
        out.println("Balance = " + statitics.getTotalBalanceFormatted());
        out.println("TimeoutSeconds = " + session.getMaxInactiveInterval());
        if (request.getRetsVersion().notEquals(RetsVersion.RETS_1_0) || WebApp.getHPMAMode())
        {
            RetsUtils.printCloseRetsResponse(out);
        }
        RetsUtils.printCloseRets(out);
    }

    private static final Logger LOG = Logger.getLogger(LoginServlet.class);
}
