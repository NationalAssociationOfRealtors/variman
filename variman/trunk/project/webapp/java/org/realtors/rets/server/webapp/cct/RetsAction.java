/*
 * Created on Aug 25, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import javax.servlet.http.HttpSession;

import org.realtors.rets.server.User;
import org.realtors.rets.server.webapp.auth.AuthenticationFilter;

import org.apache.struts.action.Action;

/**
 * @author kgarner
 */
public abstract class RetsAction extends Action
{
    protected User getUser(HttpSession session)
    {
        return (User) session.getAttribute(
            AuthenticationFilter.AUTHORIZED_USER_KEY);
    }
}
