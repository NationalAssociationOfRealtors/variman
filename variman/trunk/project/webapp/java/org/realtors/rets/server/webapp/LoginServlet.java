package org.realtors.rets.server.webapp;

import java.io.PrintWriter;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

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
        HttpSession session = request.getSession();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>RETS Login Servlet</title>");
        out.println("</head>");
        out.println();
        out.println("<body>");
        out.println("<h1 align='center'>RETS Login Servlet</h1>");
        out.println("<p>This is the RETS login servlet.</p>");
        out.println("</body>");
        out.println("</html>");
    }
}
