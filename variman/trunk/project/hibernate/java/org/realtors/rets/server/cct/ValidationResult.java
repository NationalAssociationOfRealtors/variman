/*
 */
package org.realtors.rets.server.cct;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 
 */
public class ValidationResult
{
    public ValidationResult()
    {
        mStatus = StatusEnum.NOT_RUN;
        mMessages = new ArrayList();
        mTestName = "";
    }
    
    public void addMessage(String message)
    {
        mMessages.add(message);
    }
    
    public String getMessage()
    {
        return StringUtils.join(mMessages.iterator(), ",");
    }
    
    public List getMessages()
    {
        return mMessages;
    }

    public StatusEnum getStatus()
    {
        return mStatus;
    }

    public String getTestName()
    {
        return mTestName;
    }
    
    public boolean isFailure()
    {
        return !wasSuccessful();
    }

    public void reset()
    {
        mMessages = new ArrayList();
        mStatus = StatusEnum.PASSED;
    }

    public void setMessage(String message)
    {
        mMessages.add(message);
    }

    public void setMessages(List list)
    {
        mMessages = list;
    }

    public void setStatus(StatusEnum enum)
    {
        mStatus = enum;
    }

    public void setTestName(String string)
    {
        mTestName = string;
    }

    public boolean wasSuccessful()
    {
        return mStatus == StatusEnum.PASSED;
    }

    private List mMessages;
    private StatusEnum mStatus;
    private String mTestName;
}
