/*
 * Created on Aug 27, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import org.apache.struts.validator.ValidatorForm;

/**
 * @author kgarner
 */
public class LoginForm extends ValidatorForm
{
    /**
     * 
     * @return
     */
    public String getDone()
    {
        return mDone;
    }

    /**
     * 
     * @return
     */
    public String getPassword()
    {
        return mPassword;
    }

    /**
     * 
     * @return
     */
    public String getUsername()
    {
        return mUsername;
    }

    /**
     * 
     * @param string
     */
    public void setDone(String string)
    {
        mDone = string;
    }

    /**
     * 
     * @param string
     */
    public void setPassword(String string)
    {
        mPassword = string;
    }

    /**
     * 
     * @param string
     */
    public void setUsername(String string)
    {
        mUsername = string;
    }

    private String mDone;
    private String mPassword;
    private String mUsername;

}
