/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 * Created on Aug 21, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.realtors.rets.server.User;
import org.realtors.rets.server.webapp.auth.HibernateUserMap;
import org.realtors.rets.server.webapp.auth.UserMap;

/**
 * @author kgarner
 */
public class RegistrationAction extends Action
{
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

        return mapping.findForward("confirmPage");
    }
}
