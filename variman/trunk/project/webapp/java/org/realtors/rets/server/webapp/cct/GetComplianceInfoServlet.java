/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 * Created on Sep 25, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import org.realtors.rets.server.User;
import org.realtors.rets.server.cct.UserInfo;
import org.realtors.rets.server.webapp.RetsServlet;

/**
 * @web.servlet name="get-compliance-info-servlet"
 * @web.servlet-mapping  url-pattern="/getComplianceInfo"
 */
public class GetComplianceInfoServlet extends RetsServlet
{
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException
    {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null)
        {
            response.sendError(404, "Username or Password incorrect.");
            return;
        }

        UserInfo userInfo = UTILS.getUserInfo(username); 

        boolean authFailed;
        if (userInfo == null)
        {
            authFailed = true;
        }
        else
        {
            authFailed = !UTILS.authenticateUser(userInfo.getUser(),
                                                 password);
        }
        if (authFailed)
        {
            response.sendError(404, "Username or Password incorrect.");
            return;
        }

        response.setContentType("text/xml");
        String xmlResponse = buildXML(userInfo);
        ServletOutputStream out = response.getOutputStream();
        out.print(xmlResponse);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
        throws ServletException, IOException
    {
        doGet(request, response);
    }
    
    private boolean getTestStatus(UserInfo userInfo)
    {
        TestRunner testRunner =
            TestRunnerFactory.getTestRunner(userInfo.getUser().getUsername());
            
        return testRunner.passedAllTests();
    }
    
    private String buildXML(UserInfo userInfo)
    {
        User user = userInfo.getUser();

        Element rootElement = new Element("COMPLIANCE_REPORT");
        Document doc = new Document(rootElement, null);
        
        List content = new ArrayList();
        
        Element element = new Element("FIRST_NAME");
        element.setText(user.getFirstName());
        content.add(element);
        
        element = new Element("LAST_NAME");
        element.setText(user.getLastName());
        content.add(element);
        
        element = new Element("COMPANY");
        element.setText(userInfo.getCompany());
        content.add(element);
        
        element = new Element("EMAIL");
        element.setText(userInfo.getEmail());
        content.add(element);
        
        element = new Element("PRODUCT_NAME");
        element.setText(userInfo.getProductName());
        content.add(element);
        
        element = new Element("PRODUCT_VERSION");
        element.setText(userInfo.getProductVersion());
        content.add(element);
        
        element = new Element("USER_AGENT");
        element.setText(userInfo.getUserAgent());
        content.add(element);
        
        element = new Element("PASSED_ALL_TESTS");
        if (getTestStatus(userInfo))
        {
            element.setText("YES");
        }
        else
        {
            element.setText("NO");
        }
        content.add(element);
        
        rootElement.setContent(content);
        
        XMLOutputter out = new XMLOutputter("  ", true, "ISO-8859-1");
        
        StringWriter writer = new StringWriter();
        try
        {
            out.output(doc, writer);
        }
        catch (IOException e)
        {
            LOG.error(e);
        }
        
        return writer.toString();
    }

    protected static final UserUtils UTILS = new UserUtils();
    private static final Logger LOG =
        Logger.getLogger(GetComplianceInfoServlet.class);
}
