/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

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
import org.realtors.rets.server.webapp.cct.ActionHandler;
import org.realtors.rets.server.webapp.cct.AlternateLoginHandler;

public class LoginUrlLogin extends BaseCertificationTest
{
    public String getName()
    {
        return NAME;
    }

    public String getDescription()
    {
        return "Tests client can handle changing Login URLs";
    }

    public static final String[] PROCEDURE = {
        "Login." ,
        "Logout.",
        "Login, again",
        "Logout",
    };

    public String[] getProcedure()
    {
        return PROCEDURE;
    }

    public void start()
    {
        RetsHandlers handlers = getRetsHandlers();
        handlers.resetAll();

        LoginHandler login = handlers.getLoginHandler();
        login.setAlternateLoginUrl(true);
        login.setSessionId(SESSION_ID);
        login.setGetInvokeCount(InvokeCount.ONE);
        login.addStandardHeaders();

        ActionHandler action = handlers.getActionHandler();
        action.setGetInvokeCount(InvokeCount.ONE);
        action.addStandardHeaders();
        action.addStandardCookies(SESSION_ID);

        AlternateLoginHandler altLogin = handlers.getAlternateLoginHandler();
        altLogin.setSessionId(SESSION_ID);
        altLogin.setGetInvokeCount(InvokeCount.ONE);
        altLogin.setAlternateActionUrl(true);
        altLogin.addStandardHeaders();

        AlternateActionHandler altAction = handlers.getAlternateActionHandler();
        altAction.setGetInvokeCount(InvokeCount.ONE);
        altAction.addStandardHeaders();
        altAction.addStandardCookies(SESSION_ID);

        GetMetadataHandler metadata = handlers.getGetMetadataHandler();
        metadata.setGetInvokeCount(InvokeCount.ANY);
        metadata.addStandardHeaders();
        metadata.addStandardCookies(SESSION_ID);

        LogoutHandler logout = handlers.getLogoutHandler();
        logout.setGetInvokeCount(InvokeCount.ZERO_OR_TWO);
        logout.addStandardHeaders();
        logout.addStandardCookies(SESSION_ID);
    }
    
    public void stop()
    {
    }
    
    public void cancel()
    {
    }

    public static final String NAME = "LoginUrlLogin";
    public static final String SESSION_ID = NAME;

}
