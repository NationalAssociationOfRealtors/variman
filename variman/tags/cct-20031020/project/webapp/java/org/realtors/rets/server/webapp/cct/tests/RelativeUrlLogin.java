/*
 */
package org.realtors.rets.server.webapp.cct.tests;

import org.realtors.rets.server.webapp.cct.AlternateActionHandler;
import org.realtors.rets.server.webapp.cct.BaseCertificationTest;
import org.realtors.rets.server.webapp.cct.GetMetadataHandler;
import org.realtors.rets.server.webapp.cct.InvokeCount;
import org.realtors.rets.server.webapp.cct.LoginHandler;
import org.realtors.rets.server.webapp.cct.LogoutHandler;
import org.realtors.rets.server.webapp.cct.RetsHandlers;

public class RelativeUrlLogin extends BaseCertificationTest
{
    public String getName()
    {
        return "RelativeUrlLogin";
    }

    public String getDescription()
    {
        return "Tests client can handle relative URLs";
    }

    public static final String[] PROCEDURE = { "Login." , "Logout." };
    public String[] getProcedure()
    {
        return PROCEDURE;
    }

    public void start()
    {
        RetsHandlers handlers = getRetsHandlers();
        handlers.resetAll();

        LoginHandler login = handlers.getLoginHandler();
        login.setRelativeUrls(true);
        login.setSessionId(SESSION_ID);
        login.setGetInvokeCount(InvokeCount.ONE);
        login.setAlternateActionUrl(true);
        login.addStandardHeaders();

        AlternateActionHandler action = handlers.getAlternateActionHandler();
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

    public static final String SESSION_ID = "RelativeUrlLogin";

}
