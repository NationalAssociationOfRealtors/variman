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
    }

    public void validate(ValidationResult result)
    {
        RetsHandlers handlers = getRetsHandlers();
        handlers.validateAll(result);
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
