/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp.cct;

import org.apache.struts.action.ActionForm;

public class TestForm extends ActionForm
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
    public String getTestName()
    {
        return mTestName;
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
    public void setTestName(String string)
    {
        mTestName = string;
    }

    private String mDone;
    private String mTestName;
}
