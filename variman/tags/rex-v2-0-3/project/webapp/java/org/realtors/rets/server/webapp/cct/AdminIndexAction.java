/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 * Created on Oct 1, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.cct.StatusEnum;
import org.realtors.rets.server.cct.UserInfo;
import org.realtors.rets.server.cct.UserRoles;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author kgarner
 */
public class AdminIndexAction
    extends CctAction
    implements UserRoles
{
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception
    {
        List userInfos = UTILS.getAllUserInfos();

        List adminInfos = new ArrayList();
        Iterator i = userInfos.iterator();
        while (i.hasNext())
        {
            UserInfo userInfo = (UserInfo) i.next();
            if (userInfo.isUserInRole(CLIENT_TESTER))
            {
                String username = userInfo.getUser().getUsername();
                TestRunner testRunner =
                    TestRunnerFactory.getTestRunner(username);
                                        
                AdminIndexInfo info = new AdminIndexInfo();
                info.setUserInfo(userInfo);
                
                if (testRunner.passedAllTests())
                {
                    info.setOverallStatus(StatusEnum.PASSED);
                }
                else
                {
                    info.setOverallStatus(StatusEnum.FAILED);
                }
                                
                adminInfos.add(info);
            }
        }
        
        request.setAttribute("cctAdminIndexInfos", adminInfos);
        
        return mapping.findForward("adminIndex");
    }
}
