/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.realtors.rets.client.RetsVersion;

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
        if (retsVersion.equals(RetsVersion.RETS_1_0))
        {
            mRetsVersionheader = "RETS/1.0";
        }
        else if (retsVersion.equals(RetsVersion.RETS_1_5))
        {
            mRetsVersionheader = "RETS/1.5";
        }
        else if (retsVersion.equals(RetsVersion.RETS_1_7))
        {
            mRetsVersionheader = "RETS/1.7";
        }
        else if (retsVersion.equals(RetsVersion.RETS_1_7_2))
        {
            mRetsVersionheader = "RETS/1.7.2";
        }
        setRetsHttpHeaders();
    }
    
    public void setRetsRequestID(String retsRequestID)
    {
        mRetsRequestID = retsRequestID;
        setRetsHttpHeaders();
    }

    private void setRetsHttpHeaders()
    {
        if (mRetsVersionheader != null)
        {
            setHeader("RETS-Version", mRetsVersionheader);
        }
        setHeader("RETS-Server", sRetServerHeader);
        setHeader("Cache-Control", "private");
        if (mRetsRequestID != null)
            setHeader("RETS-Request-ID", mRetsRequestID);
    }

    public void reset()
    {
        super.reset();
        setRetsHttpHeaders();
    }

    private String mRetsVersionheader;
    private String mRetsRequestID;
    public static final String sRetServerHeader =
        WebApp.SERVER_NAME + "/" + WebApp.getVersion();
}
