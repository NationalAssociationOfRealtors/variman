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
        super.start();
        HandlerManager actionManager = HandlerManager.getInstance();
        login = actionManager.getLoginHandler(mTestContext);
        login.reset();
        login.setRelativeUrls(true);
        login.setSessionId(SESSION_ID);
        login.setGetInvokeCount(InvokeCount.ONE);
        login.addRequiredHeader("Accept", "^\\*/\\*$");
        login.addRequiredHeader("User-Agent", ".*");
        login.addRequiredHeader("RETS-Version", ".*");

        action = actionManager.getActionHandler();
        action.reset();
        action.setGetInvokeCount(InvokeCount.ONE);
        login.addRequiredHeader("Accept", "^\\*/\\*$");
        login.addRequiredHeader("User-Agent", ".*");
        login.addRequiredHeader("RETS-Version", ".*");
        action.addCookie("RETS-Session-ID", "^" + SESSION_ID + "$");

        logout = actionManager.getLogoutHandler();
        logout.reset();
        logout.setGetInvokeCount(InvokeCount.ZERO_OR_ONE);
        login.addRequiredHeader("Accept", "^\\*/\\*$");
        login.addRequiredHeader("User-Agent", ".*");
        login.addRequiredHeader("RETS-Version", ".*");
        logout.addCookie("RETS-Session-ID", "^" + SESSION_ID + "$");
        mStatus = RUNNING;
    }

    private LoginHandler login;
    private ActionHandler action;
    private LogoutHandler logout;
    public static final String SESSION_ID = "RelativeUrlLogin";
}
