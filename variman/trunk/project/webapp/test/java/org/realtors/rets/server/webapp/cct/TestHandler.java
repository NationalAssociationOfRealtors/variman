/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import java.io.PrintWriter;

import org.realtors.rets.server.webapp.RetsServletRequest;
import org.realtors.rets.server.webapp.RetsServletResponse;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.ReplyCode;

import org.apache.commons.lang.enum.Enum;

class TestHandler extends BaseServletHandler
{
    public static final String NAME = "test";

    public TestHandler()
    {
        mResponseMode = NO_RESPONSE;
        mExceptionMode = NO_EXCEPTION;
    }

    public String getName()
    {
        return NAME;
    }

    public void setResponseMode(ResponseMode responseMode)
    {
        mResponseMode = responseMode;
    }

    public void setExceptionMode(ExceptionMode exceptionMode)
    {
        mExceptionMode = exceptionMode;
    }

    protected void serviceRets(RetsServletRequest request,
                               RetsServletResponse response)
        throws RetsReplyException, IOException
    {
        if (mResponseMode == WRITER)
        {
            response.setContentType("text/xml");
            PrintWriter out = response.getWriter();
            out.println("<xml></xml>");
        }
        else if (mResponseMode == XML_WRITER)
        {
            PrintWriter out = response.getXmlWriter();
            out.println("<xml></xml>");
        }

        if (mExceptionMode == RETS_EXCEPTION)
        {
            throw new RetsReplyException(ReplyCode.MISC_ERROR);
        }
        else if (mExceptionMode == RUNTIME_EXCEPTION)
        {
            throw new RuntimeException("Error");
        }
    }

    private ResponseMode mResponseMode;
    private ExceptionMode mExceptionMode;

    public static final ResponseMode NO_RESPONSE = new ResponseMode("none");
    public static final ResponseMode WRITER = new ResponseMode("writer");
    public static final ResponseMode XML_WRITER =
        new ResponseMode("xml-writer");

    public static final ExceptionMode NO_EXCEPTION = new ExceptionMode("none");
    public static final ExceptionMode RETS_EXCEPTION =
        new ExceptionMode("rets");
    public static final ExceptionMode RUNTIME_EXCEPTION =
        new ExceptionMode("runtime");

    static class ResponseMode extends Enum
    {
        private ResponseMode(String name)
        {
            super(name);
        }
    }

    static class ExceptionMode extends Enum
    {
        private ExceptionMode(String name)
        {
            super(name);
        }
    }
}
