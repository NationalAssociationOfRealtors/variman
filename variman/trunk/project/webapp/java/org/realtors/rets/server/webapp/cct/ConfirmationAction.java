/*
 * Created on Aug 25, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.realtors.rets.server.User;
import org.realtors.rets.server.cct.UserInfo;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author kgarner
 */
public class ConfirmationAction extends Action
{
    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception
    {
        if (isCancelled(request))
        {
            return new ActionForward(mapping.getInput());
        }
        
        HttpSession session = request.getSession();
        RegistrationForm form =
            (RegistrationForm) session.getAttribute("registrationForm");
            
        if (form == null)
        {
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("registration.session.timeout"));
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        }

        User user = new User();
        user.setUsername(form.getUsername());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.changePassword(form.getPassword());
        
        UserInfo userInfo = new UserInfo();
        userInfo.setUser(user);
        userInfo.setAgentId(form.getAgentId());
        userInfo.setCompany(form.getCompany());
        userInfo.setEmail(form.getEmail());
        userInfo.setProductName(form.getProductName());
        userInfo.setProductVersion(form.getProductVersion());
        userInfo.setUserAgent(form.getUserAgent());      

        UserUtils utils = new UserUtils();
        utils.createUser(user, userInfo);
        
        session.setAttribute("registrationForm", null);
        
        return mapping.findForward("userPage");
    }
}
