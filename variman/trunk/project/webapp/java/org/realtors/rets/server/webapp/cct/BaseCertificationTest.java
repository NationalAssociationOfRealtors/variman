/*
 */
package org.realtors.rets.server.webapp.cct;

/**
 * Subclasses must implement getDescription(), start(), and validate().
 */
public abstract class BaseCertificationTest implements CertificationTest
{
    public BaseCertificationTest()
    {
        mValidationResults = null;
        mStatus = FAILED;
    }

    public String getMessage()
    {
        if (mValidationResults == null)
        {
            return "Test never run";
        }
        return mValidationResults.getMessage();
    }

    public Status getStatus()
    {
        return mStatus;
    }

    public void stop()
    {
        mValidationResults = validate();
        if (mValidationResults.wasSuccessful())
        {
            mStatus = PASSED;
        }
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
}
