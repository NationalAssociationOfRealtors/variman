/*
 * Created on Aug 21, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.realtors.rets.server.User;
import org.realtors.rets.server.cct.UserInfo;
import org.realtors.rets.server.webapp.auth.HibernateUserMap;
import org.realtors.rets.server.webapp.auth.UserMap;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author kgarner
 */
public class RegistrationAction extends Action
{

    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception
    {
        if (isCancelled(request))
        {
            return new ActionForward(mapping.getInput());
        }
        
        ActionErrors errors = new ActionErrors();
        
        RegistrationForm form = (RegistrationForm) actionForm;
        
        UserMap userMap = new HibernateUserMap();
        String username = form.getUsername();
        User user = userMap.findUser(username);
        if (user != null)
        {
            errors.add(ActionErrors.GLOBAL_ERROR,
                       new ActionError("registration.user.exists"));
        }
        
        if (!form.isAcceptedLegalese())
        {
            errors.add(ActionErrors.GLOBAL_ERROR,
                       new ActionError("registration.accept.terms"));
        }

        if (!errors.isEmpty())
        {
            saveErrors(request, errors);
            return new ActionForward(mapping.getInput());
        }

        user = new User();
        user.setUsername(username);
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setPassword(form.getPassword());
        
        UserInfo userInfo = new UserInfo();
        userInfo.setUser(user);
        userInfo.setAgentID(form.getAgentID());
        userInfo.setCompany(form.getCompany());
        userInfo.setEmail(form.getEmail());
        userInfo.setProductName(form.getProductName());
        userInfo.setProductVersion(form.getProductVersion());
        userInfo.setUserAgent(form.getUserAgent());

//        UserUtils userUtils = new UserUtils();
//        userUtils.createUser(user, userInfo);
        
        HttpSession session = request.getSession();
        session.setAttribute("regUser", user);
        session.setAttribute("regUserInfo", userInfo);
        request.setAttribute("user", user);
        request.setAttribute("userInfo", userInfo);

        return mapping.findForward("confirmPage");
    }
}
