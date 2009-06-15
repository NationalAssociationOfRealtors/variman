package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @web.servlet name="default-servlet"
 * @web.servlet-mapping url-pattern="/index.html"
 */
public class DefaultServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException
    {
        StringBuffer contextPath = ServletUtils.getContextPath(request);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>RETS Server</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<p>");
        out.println("This is a RETS server.  The login URL is:");
        out.println("</p>");
        out.println("<blockquote>");
        out.println(contextPath + Paths.LOGIN);
        out.println("</blockquote>");
        out.println("</body>");
        out.println("</html>");
    }

    protected void doPost(HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse)
        throws ServletException, IOException
    {
        doGet(httpServletRequest, httpServletResponse);
    }
}
