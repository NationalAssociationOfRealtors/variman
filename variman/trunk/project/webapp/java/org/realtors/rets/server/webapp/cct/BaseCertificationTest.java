/*
 */
package org.realtors.rets.server.webapp.cct;

import org.realtors.rets.server.cct.StatusEnum;
import org.realtors.rets.server.cct.ValidationResult;

/**
 * Subclasses must implement getDescription(), start(), and validate().
 */
public abstract class BaseCertificationTest implements CertificationTest
{
    public BaseCertificationTest()
    {
        mValidationResult = null;
        mMessage = "";
        mStatus = StatusEnum.NOT_RUN;
    }

    public String getMessage()
    {
        return mMessage;
    }

    public StatusEnum getStatus()
    {
        return mStatus;
    }

    public void start()
    {
        mMessage = "";
    }

    public void stop()
    {
        mValidationResult = validate();
        if (mValidationResult.wasSuccessful())
        {
            mStatus = StatusEnum.PASSED;
        }
        else
        {
            mStatus = StatusEnum.FAILED;
        }
        mMessage = mValidationResult.getMessage();
    }

    public ValidationResult validate()
    {
        ValidationResult result = new ValidationResult();
        RetsHandlers handlers = getRetsHandlers();
        handlers.validateAll(result);
        return result;
    }

    public void cancel()
    {
        mStatus = StatusEnum.FAILED;
    }

    public void init(String testContext)
    {
        mTestContext = testContext;
    }

    protected RetsHandlers getRetsHandlers()
    {
        RetsHandlers handlers =
            HandlerManager.getInstance().getHandlers(mTestContext);
        return handlers;
    }

    protected StatusEnum mStatus;
    private ValidationResult mValidationResult;
    protected String mTestContext;
    private String mMessage;
}
