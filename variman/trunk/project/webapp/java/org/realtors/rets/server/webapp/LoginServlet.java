package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.realtors.rets.server.User;
import org.realtors.rets.server.AccountingStatistics;
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
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException
    {
        StringBuffer contextPath = new StringBuffer();
        contextPath.append(request.getScheme()).append("://");
        contextPath.append(request.getServerName());
        contextPath.append(":").append(request.getServerPort());
        contextPath.append(request.getContextPath());
        LOG.debug("context=" + contextPath);

        HttpSession session = request.getSession();
        SessionFilter.validateSession(session);
        AccountingStatistics statitics = getStatistics(session);
        statitics.startSession();
        User user = getUser(session);

        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        printOpenRets(out, 0, "Operation Successful");
        out.println("Broker = B123, BO987");
        out.println("MemberName = " + user.getName());
        out.println("MetadataVersion = 1.0.000");
        out.println("MinMetadataVersion = 1.00.000");
        out.println("User = A123,5678,1,A123");
        out.println("Login = " + contextPath + Paths.LOGIN);
        out.println("Logout = " + contextPath + Paths.LOGOUT);
        out.println("Search = " + contextPath + Paths.SEARCH);
        out.println("GetMetadata = " + contextPath + Paths.GET_METADATA);
        out.println("ChangePassword = " + contextPath + Paths.CHANGE_PASSWORD);
        out.println("GetObject = " + contextPath + Paths.GET);
        out.println("Balance = " + statitics.getTotalBalanceFormatted());
        out.println("TimeoutSeconds = " + session.getMaxInactiveInterval());
        printCloseRets(out);
    }

    private static final Logger LOG =
        Logger.getLogger(LoginServlet.class);
}
