/*
 */
package org.realtors.rets.server.webapp.cct;

import org.realtors.rets.server.cct.*;
import org.realtors.rets.server.cct.*;

/**
 * Subclasses must implement getDescription(), start(), and validate().
 */
public abstract class BaseCertificationTest implements CertificationTest
{
    public BaseCertificationTest()
    {
        mValidationResults = null;
        mMessage = "";
        mStatus = NOTRUN;
    }

    public String getMessage()
    {
        return mMessage;
    }

    public Status getStatus()
    {
        return mStatus;
    }

    public void start()
    {
        mMessage = "";
    }

    public void stop()
    {
        mValidationResults = validate();
        if (mValidationResults.wasSuccessful())
        {
            mStatus = PASSED;
        }
        else
        {
            mStatus = FAILED;
        }
        mMessage = mValidationResults.getMessage();
    }

    protected abstract ValidationResults validate();

    public void cancel()
    {
        mStatus = FAILED;
    }

    public void init(String testContext)
    {
        mTestContext = testContext;
    }

    protected Status mStatus;
    private ValidationResults mValidationResults;
    protected String mTestContext;
    private String mMessage;
}
