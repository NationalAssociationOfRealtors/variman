/*
 * Created on Sep 9, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

/**
 * @author kgarner
 */
public class CctConfigPlugIn implements PlugIn
{
    public CctConfigPlugIn()
    {
    }
    
    /**
     * @see org.apache.struts.action.PlugIn#destroy()
     */
    public void destroy()
    {
    }

    /**
     * 
     * @return
     */
    public String getTestRunnerClass()
    {
        return mTestRunnerClass;
    }

    public void init(ActionServlet servlet, ModuleConfig config)
        throws ServletException
    {
        LOG.info("Setting TestRunnerClass to " + mTestRunnerClass);
        TestRunnerFactory.setTestRunnerClass(mTestRunnerClass);
    }

    /**
     * 
     * @param string
     */
    public void setTestRunnerClass(String string)
    {
        mTestRunnerClass = string;
    }

    private String mTestRunnerClass;
    private static final Logger LOG =
        Logger.getLogger(CctConfigPlugIn.class);
}
