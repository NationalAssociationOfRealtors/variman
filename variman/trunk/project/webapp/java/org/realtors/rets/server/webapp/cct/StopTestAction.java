package org.realtors.rets.server.webapp.cct;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StopTestAction extends CctAction
{

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception
    {
        TestForm form = (TestForm) actionForm;
        TestRunner testRunner = getTestRunner(request.getSession());
        int testNo = form.getTestNumber();
        LOG.debug("Stopping test #" + testNo + ": " +
                  testRunner.getDescription(testNo));
        testRunner.stopRunningTest();

        return mapping.findForward("home");
    }

    private static final Logger LOG =
        Logger.getLogger(StopTestAction.class);
}
