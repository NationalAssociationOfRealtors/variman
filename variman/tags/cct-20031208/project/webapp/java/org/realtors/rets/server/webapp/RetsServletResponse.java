/*
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.realtors.rets.server.RetsVersion;

public class RetsServletResponse extends HttpServletResponseWrapper
{
    public RetsServletResponse(HttpServletResponse response)
    {
        super(response);
        mRetsVersionheader = null;
    }

    /**
     * Helper method that sets the content type to <code>"text/xml"</code>
     * and returns the writer.  Equivalent to:
     *
     * <code>
     *   response.setContentType("text/xml");
     *   PrintWriter out = response.getWriter();
     * </code>
     *
     * @return
     * @throws IOException
     */
    public PrintWriter getXmlWriter() throws IOException
    {
        setContentType("text/xml");
        return getWriter();
    }

    public void setRetsVersionHeader(RetsVersion retsVersion)
    {
        if (retsVersion == RetsVersion.RETS_1_0)
        {
            mRetsVersionheader = "RETS/1.0";
        }
        else if (retsVersion == RetsVersion.RETS_1_5)
        {
            mRetsVersionheader = "RETS/1.5";
        }
        setRetsVersionHeader();
    }

    private void setRetsVersionHeader()
    {
        if (mRetsVersionheader != null)
        {
            setHeader("RETS-Version", mRetsVersionheader);
        }
    }

    public void reset()
    {
        super.reset();
        setRetsVersionHeader();
    }

    private String mRetsVersionheader;
}
