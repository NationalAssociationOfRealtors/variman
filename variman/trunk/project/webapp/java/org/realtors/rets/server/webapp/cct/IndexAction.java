package org.realtors.rets.server.webapp.cct;

import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: dbt
 * Date: Aug 26, 2003
 * Time: 3:08:59 PM
 * To change this template use Options | File Templates.
 */
public class IndexAction extends CctAction
{

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception
    {
        Iterator iter = ((CertificationTestSuite)
            request.getSession().getAttribute(TESTSUITE_KEY)).getTests();
        int i = 0;
        while (iter.hasNext())
        {
            CertificationTest test = (CertificationTest) iter.next();
            if (test.getStatus() == CertificationTest.RUNNING)
            {
                request.setAttribute("cctActiveTest", new Integer(i));
            }
            i++;
        }
        return mapping.findForward("home");
    }

    private static final Log LOG = LogFactory.getLog(IndexAction.class);
}
