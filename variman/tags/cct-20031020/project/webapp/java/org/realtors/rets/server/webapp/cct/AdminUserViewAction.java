/*
 * Created on Oct 8, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.cct.UserInfo;
import org.realtors.rets.server.cct.ValidationResult;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author kgarner
 */
public class AdminUserViewAction extends CctAction
{
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception
    {
        String username = request.getParameter("username");
        if (username == null)
        {
            return mapping.findForward("adminPage");
        }
        
        TestRunner testRunner = TestRunnerFactory.getTestRunner(username);
        
        List displayBeans = new ArrayList();
        Iterator i = testRunner.getTests();
        while (i.hasNext())
        {
            CertificationTest test = (CertificationTest) i.next();
            ValidationResult result = testRunner.getResult(test.getName());
            TestDisplayBean displayBean = new TestDisplayBean(test, result);
            displayBeans.add(displayBean);
        }
        
        request.setAttribute("cctDisplayBeans", displayBeans);
        
        UserInfo userInfo = UTILS.getUserInfo(username);
        request.setAttribute("cctAdminUserInfo", userInfo);
        
        return mapping.findForward("adminUserView");
    }
}
