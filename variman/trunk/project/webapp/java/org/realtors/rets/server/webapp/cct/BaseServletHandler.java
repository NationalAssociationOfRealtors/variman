/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import org.realtors.rets.server.cct.*;

import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.commons.lang.exception.NestableRuntimeException;

public abstract class BaseServletHandler implements ServletHandler
{
    public BaseServletHandler()
    {
        mExpectedHeaders = new HashMap();
        mHeaders = new HashMap();
        mExpectedCookies = new HashMap();
        mCookies = new HashMap();
    }

    public void reset()
    {
        mExpectedHeaders.clear();
        mHeaders.clear();
        mExpectedCookies.clear();
        mCookies.clear();
        mDoGetInvokeCount = 0;
        mInvokeCount = InvokeCount.ZERO;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        mDoGetInvokeCount++;
        copyHeaders(request);
        copyCookies(request);
        LOG.info(getName() + " doGet invoked: " + mDoGetInvokeCount);
    }

    private void copyHeaders(HttpServletRequest request)
    {
        mHeaders.clear();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements())
        {
            String headerName = (String) headerNames.nextElement();
            mHeaders.put(headerName.toLowerCase(),
                         request.getHeader(headerName));
        }
    }

    private void copyCookies(HttpServletRequest request)
    {
        mCookies.clear();
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
        {
            return;
        }
        for (int i = 0; i < cookies.length; i++)
        {
            Cookie cookie = cookies[i];
            mCookies.put(cookie.getName(), cookie.getValue());
        }
    }

    public void setGetInvokeCount(InvokeCount invokeCount)
    {
        mInvokeCount = invokeCount;
    }

    public ValidationResult validate()
    {
        ValidationResult result = new ValidationResult();
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
        Set names = mExpectedHeaders.keySet();
        for (Iterator iterator = names.iterator(); iterator.hasNext();)
        {
            String name = (String) iterator.next();
            String header = (String) mHeaders.get(name.toLowerCase());
            String regexp = (String) mExpectedHeaders.get(name);
            if ((header == null) || !matches(header, regexp))
            {
                String message = getName() + " HTTP header [" + name +
                    "] was " + header + ", expected " + regexp;
                result.setStatus(StatusEnum.FAILED);
                result.addMessage(message);
                LOG.debug("Failed: " + message);
            }
        }
    }

    private void validateCookies(ValidationResult result)
    {
        Set names = mExpectedCookies.keySet();
        for (Iterator iterator = names.iterator(); iterator.hasNext();)
        {
            String name = (String) iterator.next();
            String value = (String) mCookies.get(name);
            String regexp = (String) mExpectedCookies.get(name);
            if ((value == null) || !matches(value, regexp))
            {
                String message = getName() + " HTTP cookie [" + name +
                    "] was " + value + ", expected " + regexp;
                result.setStatus(StatusEnum.FAILED);
                result.addMessage(message);
                LOG.debug("Failed: " + message);
            }
        }
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

    private static final Logger LOG =
        Logger.getLogger(BaseServletHandler.class);
    private Map mExpectedHeaders;
    private int mDoGetInvokeCount;
    private InvokeCount mInvokeCount;
    private Map mHeaders;
    private Map mExpectedCookies;
    private Map mCookies;
}
