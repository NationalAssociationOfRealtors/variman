/*
 */
package org.realtors.rets.server.webapp.cct.tests;

import org.realtors.rets.server.cct.StatusEnum;
import org.realtors.rets.server.cct.ValidationResult;
import org.realtors.rets.server.webapp.cct.BaseCertificationTest;
import org.realtors.rets.server.webapp.cct.HandlerManager;
import org.realtors.rets.server.webapp.cct.LoginHandler;
import org.realtors.rets.server.webapp.cct.InvokeCount;
import org.realtors.rets.server.webapp.cct.ActionHandler;
import org.realtors.rets.server.webapp.cct.LogoutHandler;
import org.realtors.rets.server.webapp.cct.CapabilityUrlLevel;

public class MinimumUrlLogin extends BaseCertificationTest
{
    
    public String getName()
    {
        return MinimumUrlLogin.class.getName();
    }

    public String getProcedure()
    {
        return "Login, then logout.";
    }

    public ValidationResult validate()
    {
        ValidationResult results = new ValidationResult();
        mLogin.validate(results);
        mAction.validate(results);
        mLogout.validate(results);
        return results;
    }

    public String getDescription()
    {
        return "Tests a login with a minumum of capability URLs filled in";
    }

    public void start()
    {
        super.start();
        HandlerManager actionManager = HandlerManager.getInstance();
        mLogin = actionManager.getLoginHandler(mTestContext);
        mLogin.reset();
        mLogin.setCapabilityUrlLevel(CapabilityUrlLevel.MINIMAL);
        mLogin.setSessionId(SESSION_ID);
        mLogin.setGetInvokeCount(InvokeCount.ONE);
        mLogin.addStandardHeaders();

        mAction = actionManager.getActionHandler();
        mAction.reset();
        mAction.setGetInvokeCount(InvokeCount.ZERO);

        mLogout = actionManager.getLogoutHandler();
        mLogout.reset();
        mLogout.setGetInvokeCount(InvokeCount.ZERO);

        mStatus = StatusEnum.RUNNING;
    }

    private LoginHandler mLogin;
    private ActionHandler mAction;
    private LogoutHandler mLogout;
    public static final String SESSION_ID = "MinimalUrlLogin";
}
