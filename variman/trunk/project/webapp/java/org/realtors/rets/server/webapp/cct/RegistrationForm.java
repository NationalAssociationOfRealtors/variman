/*
 * Created on Aug 21, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author kgarner
 */
public class RegistrationForm extends ActionForm
{
    /**
     * 
     * @return
     */
    public String getAgentID()
    {
        return mAgentID;
    }

    /**
     * 
     * @return
     */
    public String getCompany()
    {
        return mCompany;
    }

    /**
     * 
     * @return
     */
    public String getEmail()
    {
        return mEmail;
    }

    /**
     * 
     * @return
     */
    public String getFullName()
    {
        return mFullName;
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
    public String getProductName()
    {
        return mProductName;
    }

    /**
     * 
     * @return
     */
    public String getProductVersion()
    {
        return mProductVersion;
    }

    /**
     * 
     * @return
     */
    public String getUserAgent()
    {
        return mUserAgent;
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
     * @return
     */
    public String getVerifyPassword()
    {
        return mVerifyPassword;
    }

    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        mAgentID = "";
        mCompany = "";
        mEmail = "";
        mFullName = "";
        mPassword = "";
        mProductName = "";
        mProductVersion = "";
        mUserAgent = "";
        mUsername = "";
        mVerifyPassword = "";
    }

    /**
     * 
     * @param string
     */
    public void setAgentID(String string)
    {
        mAgentID = string;
    }

    /**
     * 
     * @param string
     */
    public void setCompany(String string)
    {
        mCompany = string;
    }

    /**
     * 
     * @param string
     */
    public void setEmail(String string)
    {
        mEmail = string;
    }

    /**
     * 
     * @param string
     */
    public void setFullName(String string)
    {
        mFullName = string;
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
    public void setProductName(String string)
    {
        mProductName = string;
    }

    /**
     * 
     * @param string
     */
    public void setProductVersion(String string)
    {
        mProductVersion = string;
    }

    /**
     * 
     * @param string
     */
    public void setUserAgent(String string)
    {
        mUserAgent = string;
    }

    /**
     * 
     * @param string
     */
    public void setUsername(String string)
    {
        mUsername = string;
    }

    /**
     * 
     * @param string
     */
    public void setVerifyPassword(String string)
    {
        mVerifyPassword = string;
    }

    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        if (StringUtils.isEmpty(mUsername))
        {
            errors.add("username",
                       new ActionError("registration.username.empty"));
        }
        if (StringUtils.isEmpty(mPassword))
        {
            errors.add("password",
                       new ActionError("registration.password.empty"));
        }
        else
        {
            if (!mPassword.equals(mVerifyPassword))
            {
                errors.add("password",
                new ActionError("registration.password.unequal"));
            }
        }
        if (StringUtils.isEmpty(mProductName))
        {
            errors.add("productname",
                       new ActionError("registration.productname.empty"));
        }
        if (StringUtils.isEmpty(mProductVersion))
        {
            errors.add("productversion",
                       new ActionError("registration.productversion.empty"));
        }
        if (StringUtils.isEmpty(mCompany))
        {
            errors.add("company",
                       new ActionError("registration.company.empty"));
        }
        if(StringUtils.isEmpty("email"))
        {
            errors.add("agentid",
                       new ActionError("registration.email.empty"));
        }
        
        return errors;
    }
    
    private String mAgentID;
    private String mCompany;
    private String mEmail;
    private String mFullName;
    private String mPassword;
    private String mProductName;
    private String mProductVersion;
    private String mUserAgent;
    private String mUsername;
    private String mVerifyPassword;
}
