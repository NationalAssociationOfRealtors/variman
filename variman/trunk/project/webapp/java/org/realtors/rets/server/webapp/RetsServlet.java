/*
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import org.realtors.rets.server.AccountingStatistics;
import org.realtors.rets.server.User;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.webapp.auth.AuthenticationFilter;

public class RetsServlet extends HttpServlet implements Constants
{
    protected void sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
        }
    }

    protected AccountingStatistics getStatistics(HttpSession session)
    {
        return (AccountingStatistics) session.getAttribute(ACCOUNTING_KEY);
    }

    protected User getUser(HttpSession session)
    {
        return (User) session.getAttribute(
            AuthenticationFilter.AUTHORIZED_USER_KEY);
    }

    protected InputStream getResource(String name)
    {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return cl.getResourceAsStream(getPackageName() + "/" + name);
    }

    /**
     * Returns the package name with dots replaced with slashes.  So
     * if the package name is "org.wxwindows.demo",
     * "org/wxwindows/demo" is returned.
     */
    public String getPackageName()
    {
        // getPackage returns null in gcj
        // String pkg = this.getClass().getPackage().getName();
        String pkg = this.getClass().getName();
        pkg = pkg.substring(0, pkg.lastIndexOf('.'));
        return pkg.replace('.', '/');
    }

    protected void copyIOStream(InputStream inStream, OutputStream outStream)
        throws IOException
    {
        byte[] buffer = new byte[1024];
        while (true)
        {
            int bytesRead = inStream.read(buffer);
            if (bytesRead == -1)
            {
                break;
            }
            outStream.write(buffer, 0, bytesRead);
        }
    }

    protected void printOpenRets(PrintWriter out, int code, String message)
    {
        out.print("<RETS ReplyCode=\"");
        out.print(code);
        out.print("\" ReplyText=\"");
        out.print(message);
        out.println("\">");
        out.println("<RETS-RESPONSE>");
    }

    protected void printCloseRets(PrintWriter out)
    {
        out.println("</RETS-RESPONSE></RETS>");
    }

    /**
     * Returns the metadata manager from the servlet context.
     *
     * @return the metadata manager.
     */
    protected MetadataManager getMetadataManager()
    {
        return WebApp.getMetadataManager();
    }
}
