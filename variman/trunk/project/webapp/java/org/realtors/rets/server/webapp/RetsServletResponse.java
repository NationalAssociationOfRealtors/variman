/*
 */
package org.realtors.rets.server.webapp;

import java.io.PrintWriter;
import java.io.IOException;

import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.RetsVersion;

public class RetsServletResponse extends HttpServletResponseWrapper
{
    public RetsServletResponse(HttpServletResponse response)
    {
        super(response);
        mXmlWriter = null;
    }

    public PrintWriter getXmlWriter() throws IOException
    {
        if (mXmlWriter == null)
        {
            setContentType("text/xml");
            mXmlWriter = getWriter();
        }
        return mXmlWriter;
    }

    public boolean isXmlResponse()
    {
        return (mXmlWriter != null);
    }

    public void setRetsVersionHeader(RetsVersion retsVersion)
    {
        if (retsVersion == RetsVersion.RETS_1_0)
        {
            setHeader("RETS-Version", "RETS/1.0");
        }
        else if (retsVersion == RetsVersion.RETS_1_5)
        {
            setHeader("RETS-Version", "RETS/1.5");
        }
    }

    private PrintWriter mXmlWriter;
}
