/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.log4j.Logger;

public class LoggingHttpServletResponse extends HttpServletResponseWrapper
{
    public LoggingHttpServletResponse(HttpServletResponse response, Logger log)
    {
        super(response);
        mLog = log;
    }

    public void setContentType(String type)
    {
        mLog.debug("Content-Type: " + type + "\n");
        super.setContentType(type);
    }

    public void addHeader(String name, String value)
    {
        mLog.debug(name + ":+ " + value + "\n");
        super.addHeader(name, value);
    }

    public void setHeader(String name, String value)
    {
        mLog.debug(name + ": " + value + "\n");
        super.setHeader(name, value);
    }

    public ServletOutputStream getOutputStream() throws IOException
    {
        return new LoggingServletOutputStream(super.getOutputStream(), mLog);
    }

    public PrintWriter getWriter() throws IOException
    {
        return new PrintWriter(new LoggingWriter(super.getWriter(), mLog));
    }

    public void reset()
    {
        mLog.debug("[Reset]\n");
        super.reset();
    }

    private Logger mLog;
}
