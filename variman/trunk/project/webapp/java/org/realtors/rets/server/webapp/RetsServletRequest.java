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

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.server.config.RetsConfig;

public class RetsServletRequest extends HttpServletRequestWrapper
{
    public RetsServletRequest(HttpServletRequest request)
    {
        super(request);
        mVersion = RetsVersion.RETS_1_0;
        mRetsVersionHeader = request.getHeader("RETS-Version");
        if (mRetsVersionHeader != null)
        {
            if (mRetsVersionHeader.equals("RETS/1.0"))
            {
                mVersion = RetsVersion.RETS_1_0;
            }
            else if (mRetsVersionHeader.equals("RETS/1.5"))
            {
                mVersion = RetsVersion.RETS_1_5;
            }
            else if (mRetsVersionHeader.equals("RETS/1.7"))
            {
                mVersion = RetsVersion.RETS_1_7;
            }
            else if (mRetsVersionHeader.equals("RETS/1.7.2"))
            {
                mVersion = RetsVersion.RETS_1_7_2;
            }
        }
        mRetsRequestID = request.getHeader("RETS-Request-ID");
    }

    /**
     * Returns the RETS version. If the client does not specify a version,
     * 1.0 is implied.
     *
     * @return the RETS version.
     */
    public RetsVersion getRetsVersion()
    {
        return mVersion;
    }

    /**
     * Returns the actual value of the RETS version header.
     *
     * @return the RETS version header, or <code>null</code>
     */
    public String getRetsVersionheader()
    {
        return mRetsVersionHeader;
    }
    
    /**
     *  Returns the RETS-Request-ID header value.
     *  @return the RETS-Request-ID header value.
     */
    public String getRetsRequestID()
    {
        return mRetsRequestID;
    }
 
    
    private RetsVersion mVersion;
    private String mRetsVersionHeader;
    private String mRetsRequestID;
}
