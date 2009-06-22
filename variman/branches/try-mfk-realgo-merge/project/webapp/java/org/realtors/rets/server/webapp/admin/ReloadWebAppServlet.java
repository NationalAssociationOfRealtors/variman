/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2007, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.webapp.admin;

import java.io.IOException;
import java.io.PrintWriter;

import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.webapp.RetsServlet;
import org.realtors.rets.server.webapp.RetsServletRequest;
import org.realtors.rets.server.webapp.RetsServletResponse;
import org.realtors.rets.server.webapp.WebApp;

/**
 * @web.servlet name="reload-web-app-servlet"
 * @web.servlet-mapping url-pattern="/admin/rets/reloadWebApp"
 */
public class ReloadWebAppServlet extends RetsServlet {

    protected void doRets(RetsServletRequest request, RetsServletResponse response) throws RetsServerException, IOException {
        
        WebApp.invalidate();
        WebApp.load(request.getSession().getServletContext());
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println("Successfully reloaded.");
    }

}
