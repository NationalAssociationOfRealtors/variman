/*
 */
package org.realtors.rets.server.webapp.cct;

public class ValidationResults
{
    public ValidationResults()
    {
        mWasSuccessful = true;
        mMessage = "Success";
    }

    public boolean wasSuccessful()
    {
        return mWasSuccessful;
    }

    public String getMessage()
    {
        return mMessage;
    }

    public void setMessage(String message)
    {
        mMessage = message;
    }

    public void addFailure(String message)
    {
        if (mWasSuccessful)
        {
            mWasSuccessful = false;
            mMessage = message;
        }
    }

    private boolean mWasSuccessful;
    private String mMessage;
}
