package org.realtors.rets.server.webapp.cct;

import java.util.Map;
import java.util.HashMap;

public class HandlerManager
{
    private static HandlerManager mInstance;

    public synchronized static HandlerManager getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new HandlerManager();
        }
        return mInstance;
    }

    private HandlerManager()
    {
        mHandlers = new HashMap();
        mHandlers.put(LoginHandler.NAME, new LoginHandler());
        mHandlers.put(ActionHandler.NAME, new ActionHandler());
        mHandlers.put(LogoutHandler.NAME, new LogoutHandler());
    }

    public LoginHandler getLoginHandler()
    {
        return (LoginHandler) mHandlers.get(LoginHandler.NAME);
    }

    public ActionHandler getActionHandler()
    {
        return (ActionHandler) mHandlers.get(ActionHandler.NAME);
    }

    public LogoutHandler getLogoutHandler()
    {
        return (LogoutHandler) mHandlers.get(LogoutHandler.NAME);
    }

    public void addServletHandler(String name, Class aClass)
    {
        mHandlers.put(name, aClass);
    }

    public void addServletHandler(String name, ServletHandler handler)
    {
        mHandlers.put(name, handler);
    }

    public ServletHandler getServletHandler(String name)
    {
        return (ServletHandler) mHandlers.get(name);
    }

    public LoginHandler getLoginHandler(String testContext)
    {
        return getLoginHandler();
    }

    private Map mHandlers;
}

