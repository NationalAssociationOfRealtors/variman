/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

import org.realtors.rets.server.LogoutTransaction;
import org.realtors.rets.server.AccountingStatistics;
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

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        super.doGet(request, response);
        addRetsVersion(response);

        SessionFilter.invalidateSession(request.getSession());
        // Delete session cookie
        Cookie cookie = new Cookie("RETS-Session-ID", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        mTransaction.execute(out, new AccountingStatistics());
    }

    private LogoutTransaction mTransaction;
}
