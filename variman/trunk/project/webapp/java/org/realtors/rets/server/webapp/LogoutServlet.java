package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.realtors.rets.server.AccountingStatistics;
import org.apache.log4j.Logger;

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
    /**
     * Logs out of RETS server.
     *
     * @param request
     * @param response
     * @throws ServletException
     */
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        AccountingStatistics stats = getStatistics(request.getSession());
        LOG.debug("Duration: " + stats.getSessionDuration());

        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        printOpenRets(out, 0, "Operation Successful");
        out.println("ConnectTime = " + stats.getSessionTime());
        out.println("Billing = " + stats.getSessionBalanceFormatted());
        out.println("SignOffMessage = Goodbye");
        printCloseRets(out);

        SessionFilter.invalidateSession(session);
    }

    private static final Logger LOG =
        Logger.getLogger(LogoutServlet.class);
}
