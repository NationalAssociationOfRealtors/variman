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
        mStatus = FAILED;
    }

    public String getMessage()
    {
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
