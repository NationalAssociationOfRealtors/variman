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
        String testNo = request.getParameter("testNo");
        if (testNo == null)
        {
            return mapping.findForward("userPage");
        }
        
        int iTestNo = Integer.parseInt(testNo);
        TestRunner testRunner = getTestRunner(request.getSession());
        CertificationTest test = testRunner.getTest(iTestNo);
        
        TestDisplayBean displayBean = new TestDisplayBean(test, null);
        request.setAttribute("cctDisplayBean", displayBean);

        return mapping.findForward("showProcedure");
    }
}
