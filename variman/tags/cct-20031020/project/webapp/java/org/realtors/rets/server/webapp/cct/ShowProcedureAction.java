/*
 * Created on Sep 17, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.realtors.rets.server.cct.StatusEnum;
import org.realtors.rets.server.cct.ValidationResult;

/**
 * 
 */
public class ShowProcedureAction extends CctAction
{
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception
    {
        String testName = request.getParameter("testName");
        if (testName == null)
        {
            return mapping.findForward("userPage");
        }
        
        TestRunner testRunner = getTestRunner(request.getSession());
        CertificationTest test = testRunner.getTest(testName);
        ValidationResult result = testRunner.getResult(testName);
        
        if (testRunner.getRunningTest() == null)
        {
            request.setAttribute("cctNoRunningTest", Boolean.TRUE);
        }
        
        if (result.getStatus() == StatusEnum.RUNNING)
        {
            request.setAttribute("cctActiveTest", Boolean.TRUE);
        }
        
        TestDisplayBean displayBean = new TestDisplayBean(test, result);
        request.setAttribute("cctDisplayBean", displayBean);

        return mapping.findForward("showProcedure");
    }
}
