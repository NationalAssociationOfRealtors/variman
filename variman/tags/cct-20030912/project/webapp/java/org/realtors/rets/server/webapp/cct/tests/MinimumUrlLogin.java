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

    public String getDescription()
    {
        return "Tests a login with a minumum of capability URLs filled in";
    }

    public void start()
    {
        RetsHandlers handlers = getRetsHandlers();
        handlers.resetAll();

        LoginHandler login = handlers.getLoginHandler();
        login.reset();
        login.setCapabilityUrlLevel(CapabilityUrlLevel.MINIMAL);
        login.setSessionId(SESSION_ID);
        login.setGetInvokeCount(InvokeCount.ONE);
        login.addStandardHeaders();

        ActionHandler action = handlers.getActionHandler();
        action.reset();
        action.setGetInvokeCount(InvokeCount.ZERO);

        GetMetadataHandler metadata = handlers.getGetMetadataHandler();
        metadata.setGetInvokeCount(InvokeCount.ANY);
        metadata.addStandardHeaders();
        metadata.addStandardCookies(SESSION_ID);

        LogoutHandler logout = handlers.getLogoutHandler();
        logout.reset();
        logout.setGetInvokeCount(InvokeCount.ZERO);
    }
    
    public void stop()
    {
    }
    
    public void cancel()
    {
    }

    public static final String SESSION_ID = "MinimalUrlLogin";
}
