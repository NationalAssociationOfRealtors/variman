package org.realtors.rets.server.webapp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: Jun 18, 2003
 * Time: 1:01:58 PM
 * To change this template use Options | File Templates.
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
        Boolean loggedIn = (Boolean) session.getAttribute(LOGGED_IN_KEY);
        if (!loggedIn.booleanValue())
        {
            System.out.println("Not logged in");
        }
        else
        {
            System.out.println("Blah!");
        }
        sleep(73L);

        response.setContentType("text/xml");
        copyIOStream(getResource("logout_response.xml"),
                     response.getOutputStream());

        AccountingStatistics stats = getStatistics(request.getSession());
        System.out.println("Duration: " + stats.getDuration());
    }
}
