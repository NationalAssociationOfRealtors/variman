package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
        session.setAttribute(LOGGED_IN_KEY, Boolean.TRUE);
        AccountingStatistics statitics =
            (AccountingStatistics) session.getAttribute(ACCOUNTING_KEY);
        statitics.resetStartTime();

        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        printOpenRets(out, 0, "Operation Successful");

        out.println("<RETS-RESPONSE>");
        out.println("Broker = B123, BO987");
        out.println("MemberName = Joe T. Schmoe");
        out.println("MetadataVersion = 1.0.000");
        out.println("MinMetadataVersion = 1.00.000");
        out.println("User = A123,5678,1,A123");
        out.println("Login = " + contextPath + "/rets/login");
        out.println("Logout = " + contextPath + "/rets/logout");
        out.println("Search = " + contextPath + "/rets/search");
        out.println("GetMetadata = " + contextPath + "/rets/getMetadata");
        out.println("ChangePassword = " + contextPath + "/rets/changePassword");
        out.println("GetObject = " + contextPath + "/rets/get");
        out.println("Balance = 44.21");
        out.println("TimeoutSeconds = 60");
        out.println("</RETS-RESPONSE></RETS>");
        sleep(250L);
    }

    private void printOpenRets(PrintWriter out, int code, String message)
    {
        out.println("<RETS ReplyCode=\"");
        out.println(code);
        out.println("0\" ReplyText=\"");
        out.println(message);
        out.println("\">");
    }

    private static final Logger LOG =
        Logger.getLogger(LoginServlet.class);
}
