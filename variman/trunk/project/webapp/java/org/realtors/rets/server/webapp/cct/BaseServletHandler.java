/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.cct.StatusEnum;
import org.realtors.rets.server.cct.ValidationResult;
import org.realtors.rets.server.webapp.RetsServletRequest;
import org.realtors.rets.server.webapp.RetsServletResponse;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

public abstract class BaseServletHandler implements ServletHandler
{
    public BaseServletHandler()
    {
        mExpectedHeaders = new HashMap();
        mActualHeaders = new HashMap();
        mExpectedCookies = new HashMap();
        mActualCookies = new HashMap();
        mActualParameters = new HashMap();
    }

    public void reset()
    {
        mExpectedHeaders.clear();
        mActualHeaders.clear();
        mExpectedCookies.clear();
        mActualCookies.clear();
        mActualParameters.clear();
        mDoGetInvokeCount = 0;
        mInvokeCount = InvokeCount.ZERO;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        RetsServletRequest retsRequest = new RetsServletRequest(request);
        RetsServletResponse retsResponse =
            new RetsServletResponse(response);
        serviceRetsTemplate(retsRequest, retsResponse);
    }

    /**
     * Returns <code>true</code> if the response format is XML. The error
     * handler uses this to determine how to handle errors. For XML responses,
     * a RETS reply is sent. For non-XML responses, a HTTP 500 error is sent.
     *
     * @return <code>true</code> if the response format is XML
     */
    protected boolean isXmlResponse()
    {
        return true;
    }

    /**
     * Template method for RETS service handling. It takes care of any
     * housekeeping needed for tests, as well as handles exceptions.
     *
     * @see #isXmlResponse
     */
    private void serviceRetsTemplate(RetsServletRequest request,
                                     RetsServletResponse response)
        throws IOException
    {
        try
        {
            // Make copies of information for tests
            copyHeaders(request);
            copyCookies(request);
            copyParameters(request);

            response.setRetsVersionHeader(request.getRetsVersion());
            serviceRets(request, response);

            // Only increment invoke count after calling transaction. This
            // ensures that any exceptions thrown cause the invoke count not
            // to get incremented.
            mDoGetInvokeCount++;
            LOG.info(getName() + " invoked: " + mDoGetInvokeCount);
        }
        catch(RetsReplyException e)
        {
            if (response.isCommitted())
            {
                // Nothing we can report to the client, so make sure to log
                // this
                LOG.error("Caught", e);
            }
            else
            {
                response.reset();
                if (isXmlResponse())
                {
                    // These are not necessarily errors, as bad input from the
                    // client could cause an exception
                    LOG.debug("Caught", e);
                    PrintWriter out = response.getXmlWriter();
                    out.print("<RETS ReplyCode=\"" + e.getReplyCode() +
                              "\" ReplyText=\"" + e.getMeaning() + "\"/>\n");
                }
                else
                {
                    // This is probably an error
                    LOG.error("Caught", e);
                    response.setStatus(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            }
        }
        catch(Exception e)
        {
            LOG.error("Caught", e);
            if (!response.isCommitted())
            {
                response.reset();
                if (isXmlResponse())
                {
                    PrintWriter out = response.getXmlWriter();
                    out.print("<RETS ReplyCode=\"20513\" " +
                              "ReplyText=\"Miscellaneous error\"/>\n");
                }
                else
                {
                    response.setStatus(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            }
        }
    }

    /**
     * Method to be overridden by subclasses to handle RETS requests. The
     * subclass should try to put the error checking and exception throwing
     * before the response is committed. This allows the error handler to send
     * back appropriate RETS error responses. Once the response is committed,
     * the only way to handle errors is to log them.
     */
    protected void serviceRets(RetsServletRequest request,
                               RetsServletResponse response)
        throws RetsReplyException, IOException
    {
        // Should be overridden
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }

    private void copyHeaders(HttpServletRequest request)
    {
        mActualHeaders.clear();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements())
        {
            String headerName = (String) headerNames.nextElement();
            mActualHeaders.put(headerName.toLowerCase(),
                         request.getHeader(headerName));
        }
    }

    private void copyCookies(HttpServletRequest request)
    {
        mActualCookies.clear();
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
        {
            return;
        }
        for (int i = 0; i < cookies.length; i++)
        {
            Cookie cookie = cookies[i];
            mActualCookies.put(cookie.getName().toLowerCase(),
                               cookie.getValue());
        }
    }

    private void copyParameters(HttpServletRequest request)
    {
        mActualParameters.clear();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements())
        {
            String paramName = (String) paramNames.nextElement();
            mActualParameters.put(paramName, request.getParameter(paramName));
        }
    }

    public void setGetInvokeCount(InvokeCount invokeCount)
    {
        mInvokeCount = invokeCount;
    }

    public ValidationResult validate()
    {
        ValidationResult result = new ValidationResult();
        result.setStatus(StatusEnum.PASSED);
        validate(result);
        return result;
    }

    public void validate(ValidationResult result)
    {
        validateInvokeCount(result);
        if (mDoGetInvokeCount > 0)
        {
            validateHeaders(result);
            validateCookies(result);
            validateParameters(result);
        }
    }

    private void validateInvokeCount(ValidationResult result)
    {
        boolean failed = false;
        if (mInvokeCount.equals(InvokeCount.ZERO) && !(mDoGetInvokeCount == 0))
        {
            failed = true;
        }
        else if (mInvokeCount.equals(InvokeCount.ONE) &&
                 !(mDoGetInvokeCount == 1))
        {
            failed = true;
        }
        else if (mInvokeCount.equals(InvokeCount.ZERO_OR_ONE) &&
                 !((mDoGetInvokeCount == 0) || (mDoGetInvokeCount == 1)))
        {
            failed = true;
        }

        if (failed)
        {
            String message = getName() + " get invoke count was " +
                mDoGetInvokeCount + ", expected " + mInvokeCount.getName();
            result.setStatus(StatusEnum.FAILED);
            result.addMessage(message);
            LOG.debug("Failed: " + message);
        }
    }

    private void validateHeaders(ValidationResult result)
    {
        validateRegexpMaps(result, mExpectedHeaders, mActualHeaders, "header");
    }

    private void validateCookies(ValidationResult result)
    {
        validateRegexpMaps(result, mExpectedCookies, mActualCookies, "cookie");
    }

    private void validateRegexpMaps(ValidationResult result, Map expected,
                                    Map actual, String content)
    {
        Set names = expected.keySet();
        for (Iterator iterator = names.iterator(); iterator.hasNext();)
        {
            String name = (String) iterator.next();
            String value = (String) actual.get(name.toLowerCase());
            String regexp = (String) expected.get(name);
            if ((value == null) || !matches(value, regexp))
            {
                String message = getName() + " HTTP " + content +" [" + name +
                    "] was " + value + ", expected " + regexp;
                result.setStatus(StatusEnum.FAILED);
                result.addMessage(message);
                LOG.debug("Failed: " + message);
            }
        }
    }

    protected void validateParameters(ValidationResult result)
    {
    }

    public void addRequiredHeader(String header, String pattern)
    {
        mExpectedHeaders.put(header, pattern);
    }

    public static boolean matches(String string, String regexp)
    {
        try
        {
            Perl5Compiler compiler = new Perl5Compiler();
            Pattern pattern = compiler.compile(regexp);
            Perl5Matcher matcher = new Perl5Matcher();
            return matcher.contains(string, pattern);
        }
        catch (MalformedPatternException e)
        {
            // Convert to runtime exception
            throw new NestableRuntimeException(e);
        }
    }

    public void addCookie(String name, String value)
    {
        mExpectedCookies.put(name, value);
    }

    public void addStandardHeaders()
    {
        addRequiredHeader("Accept", "^\\*/\\*$");
        addRequiredHeader("User-Agent", ".*");
        addRequiredHeader("RETS-Version", ".*");
    }

    public void addStandardCookies(String sessionId)
    {
        addCookie("JSESSIONID", ".*");
        addCookie("RETS-Session-ID", "^" + sessionId + "$");
    }

    protected void addRetsVersion(HttpServletResponse response)
    {
        response.addHeader("RETS-Version", "RETS/1.5");
    }

    private static final Logger LOG =
        Logger.getLogger(BaseServletHandler.class);
    private int mDoGetInvokeCount;
    private InvokeCount mInvokeCount;
    private Map mExpectedHeaders;
    private Map mActualHeaders;
    private Map mExpectedCookies;
    private Map mActualCookies;
    protected Map mActualParameters;
}
