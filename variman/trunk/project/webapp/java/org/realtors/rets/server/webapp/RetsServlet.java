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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.realtors.rets.server.AccountingStatistics;
import org.realtors.rets.server.ReplyCode;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.RetsUtils;
import org.realtors.rets.server.User;
import org.realtors.rets.server.webapp.auth.AuthenticationFilter;

public abstract class RetsServlet extends HttpServlet implements Constants
{

    protected String getContextInitParameter(String name, String defaultValue)
    {
        String value = getServletContext().getInitParameter(name);
        if (value == null)
        {
            value = defaultValue;
        }
        return value;
    }

    protected void doGet(HttpServletRequest request, 
                         HttpServletResponse response)
        throws ServletException, IOException
    {
        RetsServletRequest retsRequest = new RetsServletRequest(request);
        RetsServletResponse retsResponse = new RetsServletResponse(response);
        serviceRets(retsRequest, retsResponse);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }

    /**
     * Returns <code>true</code> if the response format is XML. The template
     * error handler uses this to determine how to handle errors. For XML
     * responses, a RETS reply is sent. For non-XML responses, a HTTP 500 error
     * is sent.
     *
     * @return <code>true</code> if the response format is XML
     */
    protected boolean isXmlResponse()
    {
        return true;
    }

    /**
     * Template method for RETS service handling. It calls doRets(), but
     * handles exceptions.
     *
     * @see #isXmlResponse
     */
    protected void serviceRets(RetsServletRequest request,
                               RetsServletResponse response)
        throws IOException
    {
        try
        {
            response.setRetsVersionHeader(request.getRetsVersion());
            response.setRetsRequestID(request.getRetsRequestID());
            preDoRets(request, response);
            doRets(request, response);
            postDoRets(request, response);
        }
        catch (RetsReplyException e)
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
                    RetsUtils.printEmptyRets(out, e.getReplyCode(),
                                             e.getMeaning());
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
        catch (Throwable e)
        {
            LOG.error("Caught", e);
            if (!response.isCommitted())
            {
                response.reset();
                if (isXmlResponse())
                {
                    PrintWriter out = response.getXmlWriter();
                    RetsUtils.printEmptyRets(out, ReplyCode.MISC_ERROR);
                }
                else
                {
                    response.setStatus(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            }
        }
    }

    protected void preDoRets(RetsServletRequest request,
                             RetsServletResponse response)
    {
        // Default implementation does nothing.
    }

    /**
     * Method to be overridden by subclasses to handle RETS requests. The
     * subclass should try to put the error checking and exception throwing
     * before the response is committed. This allows the error handler to send
     * back appropriate RETS error responses. Once the response is committed,
     * the only way to handle errors is to log them.
     */
    protected abstract void doRets(RetsServletRequest request,
                          RetsServletResponse response)
        throws RetsServerException, IOException;

    protected void postDoRets(RetsServletRequest request,
                                   RetsServletResponse response)
    {
        // Default implementation does nothing.
    }

    protected AccountingStatistics getStatistics(HttpSession session)
    {
        return (AccountingStatistics) session.getAttribute(ACCOUNTING_KEY);
    }

    protected User getUser(HttpSession session)
    {
        return (User) session.getAttribute(
            AuthenticationFilter.AUTHORIZED_USER_KEY);
    }

    protected void printOpenRets(PrintWriter out, int code, String message)
    {
        out.print("<RETS ReplyCode=\"");
        out.print(code);
        out.print("\" ReplyText=\"");
        out.print(message);
        out.println("\">");
        out.println("<RETS-RESPONSE>");
    }

    protected void printCloseRets(PrintWriter out)
    {
        out.println("</RETS-RESPONSE></RETS>");
    }

    private static final Logger LOG = Logger.getLogger(RetsServlet.class);
}
