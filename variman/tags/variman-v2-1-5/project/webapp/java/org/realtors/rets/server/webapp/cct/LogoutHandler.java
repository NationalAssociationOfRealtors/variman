/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;

import javax.servlet.http.Cookie;

import org.realtors.rets.server.AccountingStatistics;
import org.realtors.rets.server.LogoutTransaction;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.webapp.RetsServletRequest;
import org.realtors.rets.server.webapp.RetsServletResponse;
import org.realtors.rets.server.webapp.SessionFilter;

public class LogoutHandler extends BaseServletHandler
{
    public static final String NAME = "/logout";

    public LogoutHandler()
    {
        mTransaction = new LogoutTransaction();
    }

    public String getName()
    {
        return NAME;
    }

    protected void serviceRets(RetsServletRequest request,
                               RetsServletResponse response)
        throws RetsReplyException, IOException
    {
        SessionFilter.invalidateSession(request.getSession());
        // Delete session cookie
        Cookie cookie = new Cookie("RETS-Session-ID", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        mTransaction.execute(response.getXmlWriter(),
                             new AccountingStatistics());
    }

    private LogoutTransaction mTransaction;
}
