/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.webapp.cct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.realtors.rets.server.cct.StatusEnum;
import org.realtors.rets.server.cct.UserInfo;
import org.realtors.rets.server.cct.UserRoles;
import org.realtors.rets.server.cct.ValidationResult;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Created by IntelliJ IDEA.
 * User: dbt
 * Date: Aug 26, 2003
 * Time: 3:08:59 PM
 * To change this template use Options | File Templates.
 */
public class IndexAction
    extends CctAction
    implements UserRoles
{

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception
    {
        HttpSession session = request.getSession();
        TestRunner testRunner = getTestRunner(session);
        List displayBeans = new ArrayList();
        Iterator i = testRunner.getTests(); 
        while (i.hasNext())
        {
            CertificationTest test = (CertificationTest) i.next();
            ValidationResult result = testRunner.getResult(test.getName());
            TestDisplayBean displayBean = new TestDisplayBean(test, result);
            displayBeans.add(displayBean);
            
            if (result.getStatus() == StatusEnum.RUNNING)
            {
                request.setAttribute("cctActiveTest", test.getName());
            }
        }
        
        UserInfo userInfo = (UserInfo) session.getAttribute(USERINFO_KEY);
        if (userInfo.isUserInRole(ADMIN_VIEW))
        {
            request.setAttribute("cctHasAdminView", Boolean.TRUE);
        }
        
        request.setAttribute("cctDisplayBeans", displayBeans);
        
        StringBuffer loginUrl = new StringBuffer();
        loginUrl.append(request.getScheme()).append("://");
        loginUrl.append(request.getServerName());
        loginUrl.append(":").append(request.getServerPort());
        loginUrl.append(request.getContextPath());
        loginUrl.append("/rets/cct/login");
        request.setAttribute("cctLoginUrl", loginUrl.toString());

        return mapping.findForward("home");
    }
}
