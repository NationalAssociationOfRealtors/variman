/*
 */

package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class LoginHandler extends BaseServletHandler
{
    public static final String NAME = "/login";

    public LoginHandler()
    {
        super();
        reset();
    }

    public String getName()
    {
        return NAME;
    }

    public void reset()
    {
        super.reset();
        mSessionId = "";
        mCapabilityUrlLevel = CapabilityUrlLevel.NORMAL;
        mRelativeUrls = false;
        mAlternateActionUrl = false;
        mAlternateLoginUrl = false;
    }

    public void setSessionId(String sessionId)
    {
        mSessionId = sessionId;
    }

    public void setRelativeUrls(boolean relativeUrls)
    {
        mRelativeUrls = relativeUrls;
    }

    public void setAlternateActionUrl(boolean alternateActionUrl)
    {
        mAlternateActionUrl = alternateActionUrl;
    }

    public void setAlternateLoginUrl(boolean alternateLoginUrl)
    {
        mAlternateLoginUrl = alternateLoginUrl;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        super.doGet(request, response);

        mContextPath = new StringBuffer();
        mContextPath.append(request.getScheme()).append("://");
        mContextPath.append(request.getServerName());
        mContextPath.append(":").append(request.getServerPort());
        mContextPath.append(request.getContextPath());

        response.addCookie(new Cookie("RETS-Session-ID", mSessionId));
        response.setContentType("text/xml");
        mOut = response.getWriter();
        println(mOut, "<RETS ReplyCode=\"0\" " +
                    "ReplyText=\"Operation Successful\">");
        println(mOut, "<RETS-RESPONSE>");
        println(mOut, "MemberName = Joe Schmoe");
        println(mOut, "User = A123,5678,1,A123");
        println(mOut, "Broker = B123");
        println(mOut, "MetadataVersion = 1.00.000");
        println(mOut, "MinMetadataVersion = 1.00.000");
        printNormalUrl(mOut, "Action", getActionUrl());
        printMaximalUrl(mOut, "ChangePassword", "changePassword");
        printMaximalUrl(mOut, "GetObject", "getObject");
        printAbsoluteCapabilityUrl(mOut, "Login", getLoginUrl());
        printMaximalUrl(mOut, "LoginComplete", "loginComplete");
        printNormalUrl(mOut, "Logout", "logout");
        printMinimalUrl(mOut, "Search", "search");
        printMinimalUrl(mOut, "GetMetadata", "getMetadata");
        printMaximalUrl(mOut, "Update", "update");
        println(mOut, "</RETS-RESPONSE>");
        println(mOut, "</RETS>");
    }

    private String getActionUrl()
    {
        return mAlternateActionUrl ? "actionAlt" : "action";
    }

    private String getLoginUrl()
    {
        return mAlternateLoginUrl ? "loginAlt" : "login";
    }

    private void printCapabilityUrl(PrintWriter out, String capability,
                                    String url)
    {
        if (mRelativeUrls)
        {
            printRelativeCapabilityUrl(out, capability, url);
        }
        else
        {
            printAbsoluteCapabilityUrl(out, capability, url);
        }
    }

    private void printAbsoluteCapabilityUrl(PrintWriter out, String capability,
                                            String url)
    {
        println(out, capability + " = " + mContextPath + "/rets/" + url);
    }

    private void printRelativeCapabilityUrl(PrintWriter out, String capability,
                                            String url)
    {
        println(out, capability + " = " + "/" + url);
    }

    private void printMinimalUrl(PrintWriter out, String capability, String url)
    {
        printCapabilityUrl(out, capability, url);
    }

    private void printNormalUrl(PrintWriter out, String capability, String url)
    {
        if (mCapabilityUrlLevel != CapabilityUrlLevel.MINIMAL)
        {
            printCapabilityUrl(out, capability, url);
        }
    }

    private void printMaximalUrl(PrintWriter out, String capability, String url)
    {
        if (mCapabilityUrlLevel == CapabilityUrlLevel.MAXIMMAL)
        {
            printCapabilityUrl(out, capability, url);
        }
    }

    /**
     * Prints a line always using CRLF rather than using the system property
     * line.separator.
     *
     * @param out
     * @param data
     */
    private void println(PrintWriter out, String data)
    {
        out.print(data);
        out.print("\r\n");
    }

    private void println(String data)
    {
        println(mOut, data);
    }

    public void setCapabilityUrlLevel(CapabilityUrlLevel capabilityUrlLevel)
    {
        mCapabilityUrlLevel = capabilityUrlLevel;
    }

    private static final Logger LOG =
        Logger.getLogger(LoginHandler.class);
    private String mSessionId;
    private CapabilityUrlLevel mCapabilityUrlLevel;
    private boolean mRelativeUrls;
    private boolean mAlternateActionUrl;
    private boolean mAlternateLoginUrl;
    private StringBuffer mContextPath;
    private PrintWriter mOut;
}
