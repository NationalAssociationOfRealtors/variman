package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Performs all necessary one-time initializations for the web
 * application.
 *
 * @web:servlet name="login-servlet"
 * @web:servlet-mapping url-pattern="/login"
 */
public class LoginServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(
                "<RETS ReplyCode=\"0\" ReplyText=\"Operation Successful\">");
        out.println("<RETS-RESPONSE>");
        out.println("Broker = B123, BO987");
        out.println("MemberName = Joe T. Schmoe");
        out.println("MetadataVersion = 1.0.000");
        out.println("MinMetadataVersion = 1.00.000");
        out.println("User = A123,5678,1,A123");
        out.println("Login = http://rets.test:6103/login");
        out.println("Logout = http://rets.test:6103/logout");
        out.println("Search = http://rets.test:6103/search");
        out.println("GetMetadata = http://rets.test:6103/getMetadata");
        out.println("ChangePassword = http://rets.test:6103/changePassword");
        out.println("GetObject = http://rets.test:6103/get");
        out.println("Balance = 44.21");
        out.println("TimeoutSeconds = 60");
        out.println("</RETS-RESPONSE></RETS>");
    }
}
