/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.cct.*;

public class ActionHandler extends BaseServletHandler
{
    public static final String NAME = "/action";

    public String getName()
    {
        return NAME;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println("Welcome to the RETS Compliance Tester from the Center " +
                    "for REALTOR(R) Technology!");
    }

    public ValidationResults validate()
    {
        return null;
    }

    public void reset()
    {
    }

    public void setGetInvokeCount(InvokeCount callCount)
    {
    }

    public void addCookie(String name, String value)
    {
    }
}
