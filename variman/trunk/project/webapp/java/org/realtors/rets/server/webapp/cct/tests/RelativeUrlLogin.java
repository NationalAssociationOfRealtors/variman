/*
 */
package org.realtors.rets.server.webapp.cct.tests;

import org.realtors.rets.server.cct.ValidationResults;
import org.realtors.rets.server.webapp.cct.BaseCertificationTest;
import org.realtors.rets.server.webapp.cct.HandlerManager;
import org.realtors.rets.server.webapp.cct.InvokeCount;
import org.realtors.rets.server.webapp.cct.LoginHandler;
import org.realtors.rets.server.webapp.cct.ActionHandler;
import org.realtors.rets.server.webapp.cct.LogoutHandler;

public class RelativeUrlLogin extends BaseCertificationTest
{
    protected ValidationResults validate()
    {
        ValidationResults results = new ValidationResults();
        login.validate(results);
        action.validate(results);
        logout.validate(results);
        return results;
    }

    public String getDescription()
    {
        return "Tests client can handle relative URLs";
    }

    public String getProcedure()
    {
        return "Login, then logout.";
    }

    public void start()
    {
        HandlerManager actionManager = HandlerManager.getInstance();
        login = actionManager.getLoginHandler(mTestContext);
        login.setSessionId("login-headers");
        login.setGetInvokeCount(InvokeCount.ONE);
        login.addRequiredHeader("Accept", "^\\*/\\*$");
        login.addRequiredHeader("User-Agent", "^[^/]+(/[^/])?$");
        login.addRequiredHeader("RETS-Version", ".*");

        action = actionManager.getActionHandler();
        action.setGetInvokeCount(InvokeCount.ONE);
        action.addCookie("RETS-Session-ID", "login-headers");

        logout = actionManager.getLogoutHandler();
        logout.setGetInvokeCount(InvokeCount.ZERO_OR_ONE);
//        logout.addCookie("RETS-Session-ID", "login-headers");
    }

    private LoginHandler login;
    private ActionHandler action;
    private LogoutHandler logout;
}