package org.realtors.rets.server.webapp.cct;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StartTestAction extends CctAction
{

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception
    {
        TestForm form = (TestForm) actionForm;
        TestRunner testRunner = getTestRunner(request.getSession());
        String testName = form.getTestName();

        LOG.debug("Starting test #" + testName + ": " +
                  testRunner.getDescription(testName));

        testRunner.startTestByName(testName);
        
        ActionForward forward;
        String done = form.getDone();
        if (done == null || done.equals("home"))
        {
            forward = mapping.findForward("home");
        }
        else        
        {
            forward = mapping.findForward("showProcedure");
            ActionForward newForward =
                new ActionForward(forward.getName(), forward.getPath(),
                                  forward.getRedirect(),
                                  forward.getContextRelative());
            StringBuffer path = new StringBuffer(newForward.getPath());
            path.append("?testName=").append(testName);
            newForward.setPath(path.toString());
            forward = newForward;
        }

        return forward;
    }

    private static final Logger LOG =
        Logger.getLogger(StartTestAction.class);
}
