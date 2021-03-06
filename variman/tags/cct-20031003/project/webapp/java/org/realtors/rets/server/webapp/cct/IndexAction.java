package org.realtors.rets.server.webapp.cct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.cct.StatusEnum;
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
public class IndexAction extends CctAction
{

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception
    {
        TestRunner testRunner = getTestRunner(request.getSession());
        List displayBeans = new ArrayList();
        Iterator iter = testRunner.getTests(); 
        int i = 0;
        while (iter.hasNext())
        {
            CertificationTest test = (CertificationTest) iter.next();
            ValidationResult result = testRunner.getResult(test.getName());
            TestDisplayBean displayBean = new TestDisplayBean(test, result);
            displayBeans.add(displayBean);
            
            if (result.getStatus() == StatusEnum.RUNNING)
            {
                request.setAttribute("cctActiveTest", test.getName());
            }
            i++;
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
