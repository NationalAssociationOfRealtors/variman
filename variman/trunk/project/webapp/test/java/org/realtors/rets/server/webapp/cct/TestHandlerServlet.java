/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

public class TestHandlerServlet extends HttpServlet
{
    public void setHandler(ServletHandler handler)
    {
        mHandler = handler;
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        mHandler.doGet(req, resp);
        mHandler.validate();
    }

    private ServletHandler mHandler;
}
