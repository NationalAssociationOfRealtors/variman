/*
 */
package org.realtors.rets.server.webapp.cct;

import org.apache.struts.action.ActionForm;

public class TestForm extends ActionForm
{
    public int getTestNumber()
    {
        return mTestNumber;
    }

    public void setTestNumber(int testNumber)
    {
        mTestNumber = testNumber;
    }

    private int mTestNumber;
}
