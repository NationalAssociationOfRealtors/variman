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
        mLogin.validate(results);
        mAction.validate(results);
        mLogout.validate(results);
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
        mLogin = actionManager.getLoginHandler(mTestContext);
        mLogin.reset();
        mLogin.setRelativeUrls(true);
        mLogin.setSessionId(SESSION_ID);
        mLogin.setGetInvokeCount(InvokeCount.ONE);
        mLogin.addRequiredHeader("Accept", "^\\*/\\*$");
        mLogin.addRequiredHeader("User-Agent", ".*");
        mLogin.addRequiredHeader("RETS-Version", ".*");

        mAction = actionManager.getActionHandler();
        mAction.reset();
        mAction.setGetInvokeCount(InvokeCount.ONE);
        mAction.addRequiredHeader("Accept", "^\\*/\\*$");
        mAction.addRequiredHeader("User-Agent", ".*");
        mAction.addRequiredHeader("RETS-Version", ".*");
        mAction.addCookie("RETS-Session-ID", "^" + SESSION_ID + "$");

        mLogout = actionManager.getLogoutHandler();
        mLogout.reset();
        mLogout.setGetInvokeCount(InvokeCount.ZERO_OR_ONE);
        mLogout.addRequiredHeader("Accept", "^\\*/\\*$");
        mLogout.addRequiredHeader("User-Agent", ".*");
        mLogout.addRequiredHeader("RETS-Version", ".*");
        mLogout.addCookie("RETS-Session-ID", "^" + SESSION_ID + "$");
        mStatus = RUNNING;
    }

    private LoginHandler mLogin;
    private ActionHandler mAction;
    private LogoutHandler mLogout;
    public static final String SESSION_ID = "RelativeUrlLogin";
}
