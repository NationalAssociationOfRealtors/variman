/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.realtors.rets.server.cct.StatusEnum;
import org.apache.struts.util.RequestUtils;

/**
 * @jsp:tag name="teststatustd"
 */
public class CctStatusTd extends TagSupport
{
    public CctStatusTd()
    {
        super();
        mScope = null;
        mProperty = null;
    }

    /**
     * gets the bean name
     * @return the name of the bean
     * @jsp:attribute required="true" rtexprvalue="false"
     *  description="name of which to show status"
     */
    public String getName()
    {
        return mName;
    }

    /**
     * sets the bean name
     * @param name the name of the test bean
     */
    public void setName(String name)
    {
        mName = name;
    }

    /**
     * @return the test bean scope
     * @jsp:attribute required="false" rtexprvalue="false"
     *  description="scope in which to locate bean"
     */
    public String getScope()
    {
        return mScope;
    }

    /**
     *
     * @param scope the scope of the test bean
     */
    public void setScope(String scope)
    {
        mScope = scope;
    }

    /**
     * @jsp:attribute required="false" rtexprvalue="false"
     *  description="property of which to show status"
     */
    public String getProperty()
    {
        return mProperty;
    }

    /**
     * 
     * @param string
     */
    public void setProperty(String string)
    {
        mProperty = string;
    }

    public int doStartTag() throws JspException
    {
        JspWriter out = pageContext.getOut();
            
        StatusEnum status =
            (StatusEnum) RequestUtils.lookup(pageContext, mName, mProperty,
                                             mScope);
        try
        {
            String message;
            String tdclass;
            if (status == StatusEnum.PASSED)
            {
                tdclass = "pass";
                message = "Test Passed";
            }
            else if (status == StatusEnum.FAILED)
            {
                tdclass = "fail";
                message = "Test Failed";
            }
            else if (status == StatusEnum.RUNNING)
            {
                tdclass = "active";
                message = "Test Running";
            }
            else if (status == StatusEnum.NOT_RUN)
            {
                tdclass = "unknown";
                message = "Test not yet run";
            }
            else
            {
                tdclass = "unknown";
                message = status.getName();
            }
            out.write("<td class=\"");
            out.write(tdclass);
            out.write("\">");
            out.write(message);
            out.write("</td>");
        }
        catch (IOException e)
        {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }

    private String mName;
    private String mProperty;
    private String mScope;
   
}
