package org.realtors.rets.server.webapp.cct;

import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;

import org.realtors.rets.server.User;
import org.realtors.rets.server.webapp.auth.AuthenticationFilter;
import org.realtors.rets.server.cct.UserInfo;

/**
 * Created by IntelliJ IDEA.
 * User: dbt
 * Date: Aug 26, 2003
 * Time: 2:05:42 PM
 * To change this template use Options | File Templates.
 */
public abstract class CctAction extends Action
{
    void loadCctState(HttpSession session)
    {
        User user = (User) session.getAttribute(USER_KEY);
        UserInfo info = (UserInfo) session.getAttribute(USERINFO_KEY);
        CertificationTestSuite suite =
            (CertificationTestSuite) session.getAttribute(TESTSUITE_KEY);
        if (user == null)
        {
            user = getUser(session);
            session.setAttribute(USER_KEY, user);
        }
        if (info == null)
        {
            info = getUserInfo(session);
            session.setAttribute(USERINFO_KEY, info);
        }
        if (suite == null)
        {
            suite = new CertificationTestSuite(user.getUsername());
            session.setAttribute(TESTSUITE_KEY, suite);
        }
    }

    protected User getUser(HttpSession session)
    {
        return (User) session.getAttribute(
            AuthenticationFilter.AUTHORIZED_USER_KEY);
    }

    protected UserInfo getUserInfo(HttpSession session)
    {
        User user = getUser(session);
        return UTILS.getUserInfo(user.getUsername());
    }

    public static final String USER_KEY = "user";
    public static final String USERINFO_KEY = "userInfo";
    public static final String TESTSUITE_KEY = "suite";
    private static final UserUtils UTILS = new UserUtils();
}
