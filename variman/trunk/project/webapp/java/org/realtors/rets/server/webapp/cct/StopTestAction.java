package org.realtors.rets.server.webapp.cct;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

public class StopTestAction extends CctAction
{

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception
    {
        loadCctState(request.getSession());
        TestForm form = (TestForm) actionForm;
        CertificationTestSuite suite = getSuite(request.getSession());
        CertificationTest test = suite.getTest(form.getTestNumber());
        LOG.debug("Stopping test #" + form.getTestNumber() + ": " +
                  test.getDescription());
        test.stop();
        return mapping.findForward("home");
    }

    private static final Logger LOG =
        Logger.getLogger(StopTestAction.class);
}
