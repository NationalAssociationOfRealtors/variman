/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.LogoutTransaction;
import org.realtors.rets.server.AccountingStatistics;

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
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        mTransaction.execute(out, new AccountingStatistics());
    }

    private LogoutTransaction mTransaction;
}
