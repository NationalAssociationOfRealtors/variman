/*
 */
package org.realtors.rets.server.webapp.cct.tests;

import org.realtors.rets.server.webapp.cct.ActionHandler;
import org.realtors.rets.server.webapp.cct.BaseCertificationTest;
import org.realtors.rets.server.webapp.cct.CapabilityUrlLevel;
import org.realtors.rets.server.webapp.cct.GetMetadataHandler;
import org.realtors.rets.server.webapp.cct.InvokeCount;
import org.realtors.rets.server.webapp.cct.LoginHandler;
import org.realtors.rets.server.webapp.cct.LogoutHandler;
import org.realtors.rets.server.webapp.cct.RetsHandlers;

public class MaximumUrlLogin extends BaseCertificationTest
{

    public String getName()
    {
        return MaximumUrlLogin.class.getName();
    }

    public String getProcedure()
    {
        return "Login, then logout.";
    }

    public String getDescription()
    {
        return "Tests a login with all capability URLs filled in";
    }

    public void start()
    {
        RetsHandlers handlers = getRetsHandlers();
        handlers.resetAll();

        LoginHandler login = handlers.getLoginHandler();
        login.setCapabilityUrlLevel(CapabilityUrlLevel.MAXIMMAL);
        login.setSessionId(SESSION_ID);
        login.setGetInvokeCount(InvokeCount.ONE);
        login.addStandardHeaders();

        ActionHandler action = handlers.getActionHandler();
        action.setGetInvokeCount(InvokeCount.ONE);
        action.addStandardHeaders();
        action.addStandardCookies(SESSION_ID);

        GetMetadataHandler metadata = handlers.getGetMetadataHandler();
        metadata.setGetInvokeCount(InvokeCount.ANY);
        metadata.addStandardHeaders();
        metadata.addStandardCookies(SESSION_ID);

        LogoutHandler logout = handlers.getLogoutHandler();
        logout.setGetInvokeCount(InvokeCount.ZERO_OR_ONE);
        logout.addStandardHeaders();
        logout.addStandardCookies(SESSION_ID);
    }
    
    public void stop()
    {
    }
    
    public void cancel()
    {
    }

    public static final String SESSION_ID = "MaximumUrlLogin";
}
