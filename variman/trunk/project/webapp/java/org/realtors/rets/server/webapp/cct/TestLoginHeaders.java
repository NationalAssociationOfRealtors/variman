/*
 */
package org.realtors.rets.server.webapp.cct;



public class TestLoginHeaders
{
    public void runSimpleLogin()
    {
        HandlerManager actionManager = HandlerManager.getInstance();
        LoginHandler login = actionManager.getLoginHandler();
        login.setSessionId("login-headers");
        login.setGetInvokeCount(InvokeCount.ONE);
        login.addRequiredHeader("Accept", "^\\*/\\*$");
        login.addRequiredHeader("User-Agent", "^[^/]+(/[^/])?$");
        login.addRequiredHeader("RETS-Version", ".*");

        ActionHandler action = actionManager.getActionHandler();
        action.setGetInvokeCount(InvokeCount.ONE);
        action.addCookie("RETS-Session-ID", "login-headers");

        LogoutHandler logout = actionManager.getLogoutHandler();
        logout.setGetInvokeCount(InvokeCount.ZERO_OR_ONE);
        action.addCookie("RETS-Session-ID", "login-headers");
    }

    public void runMinimalUrls()
    {
        HandlerManager actionManager = HandlerManager.getInstance();
        LoginHandler login = actionManager.getLoginHandler();
        login.setSessionId("login.minimalUrls");
        login.setGetInvokeCount(InvokeCount.ONE);
        login.addRequiredHeader("Accept", "^\\*/\\*$");
        login.addRequiredHeader("User-Agent", "^[^/]+(/[^/])?$");
        login.addRequiredHeader("RETS-Version", ".*");
        login.setCapabilityUrlLevel(CapabilityUrlLevel.MINIMAL);
//        login.setRelativeUrls(true);
//        login.setAlternateLoginUrl(true);

        ActionHandler action = actionManager.getActionHandler();
        action.setGetInvokeCount(InvokeCount.ONE);
        action.addCookie("RETS-Session-ID", "login.minimalUrls");

        LogoutHandler logout = actionManager.getLogoutHandler();
        logout.setGetInvokeCount(InvokeCount.ZERO_OR_ONE);
        action.addCookie("RETS-Session-ID", "login.minimalUrls");
    }
}
