/*
 * Created on Aug 27, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.RedirectingActionForward;

import org.realtors.rets.server.User;
import org.realtors.rets.server.cct.UserInfo;

/**
 * @author kgarner
 */
public class LoginAction extends CctAction
{
    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm actionForm,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        LoginForm form = (LoginForm) actionForm;

        if (isCancelled(request))
        {
            return new RedirectingActionForward(form.getDone());        
        }

        String username = form.getUsername();

        UserInfo userInfo = UTILS.getUserInfo(username); 

        if ((userInfo != null) &&
            !UTILS.authenticateUser(userInfo.getUser(), form.getPassword()))
        {
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("login.auth.failure"));
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        }
        
        User user = userInfo.getUser();        

        HttpSession session = request.getSession();

        session.setAttribute(FormAuthenticationFilter.AUTHENTICATION_KEY,
                             "true");
        session.setAttribute(USERINFO_KEY, userInfo);
        session.setAttribute(USER_KEY, user);

        TestRunner testRunner = new TestRunner(user.getUsername());
        session.setAttribute(TESTRUNNER_KEY, testRunner);
        
        return new RedirectingActionForward(form.getDone());        
    }
}
