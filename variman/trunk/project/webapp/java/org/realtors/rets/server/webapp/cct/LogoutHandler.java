/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutHandler extends BaseServletHandler
{
    public static final String NAME = "/logout";

    public String getName()
    {
        return NAME;
    }

    public void reset()
    {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
    }

    public ValidationResults validate()
    {
        return null;
    }

    public void setGetInvokeCount(InvokeCount callCount)
    {
    }
}
