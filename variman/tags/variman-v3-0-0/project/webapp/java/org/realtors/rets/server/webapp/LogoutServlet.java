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

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.realtors.rets.server.AccountingStatistics;
import org.realtors.rets.server.LogoutTransaction;
import org.realtors.rets.server.RetsServerException;

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: Jun 18, 2003
 * Time: 1:01:58 PM
 * To change this template use Options | File Templates.
 *
 * @web.servlet name="logout-servlet"
 * @web.servlet-mapping url-pattern="/rets/logout"
 */
public class LogoutServlet extends RetsServlet
{
    public void init() throws ServletException
    {
        super.init();
        mTransaction = new LogoutTransaction();
    }

    /**
     * Logs out of a RETS server.
     *
     * @param request
     * @param response
     * @throws RetsServerException
     * @throws IOException
     */
    protected void doRets(RetsServletRequest request,
                          RetsServletResponse response)
        throws RetsServerException, IOException
    {
        HttpSession session = request.getSession();
        AccountingStatistics stats = getStatistics(request.getSession());
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        mTransaction.execute(out, stats);
        SessionFilter.invalidateSession(session);
    }

    private LogoutTransaction mTransaction;
}
