/*
 * Created on Oct 1, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.realtors.rets.server.cct.UserInfo;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.RequestProcessor;

/**
 * @author kgarner
 */
public class CctRequestProcessor
    extends RequestProcessor
    implements CctConstants
{
    protected boolean processRoles(HttpServletRequest request,
                                   HttpServletResponse response,
                                   ActionMapping mapping)
        throws IOException, ServletException
    {
        // Get roles.  If roles aren't defined, we assume its good for
        // all.
        String roles[] = mapping.getRoleNames();
        if ((roles == null) || (roles.length < 1))
        {
            return true;
        }

        HttpSession session = request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute(USERINFO_KEY);
        for (int i = 0; i < roles.length; i++)
        {
            if (userInfo.isUserInRole(roles[i]))
            {
                return true;
            }
        }

        response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                           getInternal().getMessage("notAuthorized",
                                                    mapping.getPath()));

        return false;
    }
}
