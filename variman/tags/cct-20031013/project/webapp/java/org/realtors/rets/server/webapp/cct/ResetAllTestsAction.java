/*
 * Created on Sep 11, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author kgarner
 */
public class ResetAllTestsAction extends CctAction
{
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception
    {
        TestRunner runner = getTestRunner(request.getSession());
        runner.resetAllResults();
        return mapping.findForward("userPage");
    }

}
