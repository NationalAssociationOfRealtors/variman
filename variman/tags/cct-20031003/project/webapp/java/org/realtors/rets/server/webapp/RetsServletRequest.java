/*
 */
package org.realtors.rets.server.webapp;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;

import org.realtors.rets.server.RetsVersion;

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
        }
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

    private RetsVersion mVersion;
    private String mRetsVersionHeader;
}
