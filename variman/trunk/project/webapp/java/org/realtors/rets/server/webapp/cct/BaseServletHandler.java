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
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
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
    }

    public void reset()
    {
        mExpectedHeaders.clear();
        mHeaders.clear();
        mDoGetInvokeCount = 0;
        mInvokeCount = InvokeCount.ANY;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        mDoGetInvokeCount++;
        mHeaders.clear();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements())
        {
            String headerName = (String) headerNames.nextElement();
            mHeaders.put(headerName.toLowerCase(),
                         request.getHeader(headerName));
        }
        LOG.info(getName() + " doGet invoked: " + mDoGetInvokeCount);
    }

    public void setGetInvokeCount(InvokeCount invokeCount)
    {
        mInvokeCount = invokeCount;
    }

    public void validate(ValidationResults results)
    {
        if (mInvokeCount.equals(InvokeCount.ONE) && !(mDoGetInvokeCount == 1))
        {
            String message = getName() + " get invoke count was " +
                mDoGetInvokeCount + ", expected " + mInvokeCount.getName();
            results.addFailure(message);
            LOG.debug("Failed: " + message);
        }

        if (mInvokeCount.equals(InvokeCount.ZERO_OR_ONE) &&
            !((mDoGetInvokeCount == 0) || (mDoGetInvokeCount == 1)))
        {
            // Todo: LoginHandler.validate: log error
        }

        validateHeaders(results);
    }

    public ValidationResults validate()
    {
        ValidationResults results = new ValidationResults();
        validate(results);
        return results;
    }

    private void validateHeaders(ValidationResults result)
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
                result.addFailure(message);
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

    private static final Logger LOG =
        Logger.getLogger(BaseServletHandler.class);
    private Map mExpectedHeaders;
    private int mDoGetInvokeCount;
    private InvokeCount mInvokeCount;
    private Map mHeaders;
}
