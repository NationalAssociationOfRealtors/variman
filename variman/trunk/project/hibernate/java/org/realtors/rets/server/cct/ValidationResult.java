/*
 */
package org.realtors.rets.server.cct;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @hibernate.class table="rets_cct_validationresults"
 */
public class ValidationResult
{
    public ValidationResult()
    {
        mStatus = StatusEnum.NOTRUN;
        mMessages = new ArrayList();
        mTestName = "";
        mUsername = null;
    }
    
    public void addMessage(String message)
    {
        mMessages.add(message);
    }
    
    /**
     * @hibernate.id generator-class="native"
     */
    public Long getId()
    {
        return mId;
    }

    public String getMessage()
    {
        return StringUtils.join(mMessages.iterator(), ",");
    }
    
    /**
     * @hibernate.list table="rets_cct_vr_messages"
     * @hibernate.collection-key column="result_id"
     * @hibernate.collection-index column="message_index"
     * @hibernate.collection-element column="message" type="string"
     */
    public List getMessages()
    {
        return mMessages;
    }

    /**
     * @hibernate.property
     */
    public StatusEnum getStatus()
    {
        return mStatus;
    }

    /**
     * @hibernate.property
     */
    public String getTestName()
    {
        return mTestName;
    }
    
    /**
     * @hibernate.property
     */
    public String getUsername()
    {
        return mUsername;
    }
    
    public boolean isFailure()
    {
        return !wasSuccessful();
    }

    public void reset()
    {
        mMessages = new ArrayList();
        mStatus = StatusEnum.NOTRUN;
    }

    public void setId(Long long1)
    {
        mId = long1;
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

    public void setUsername(String info)
    {
        mUsername = info;
    }
        
    public boolean wasSuccessful()
    {
        return mStatus == StatusEnum.PASSED;
    }

    private Long mId;
    private List mMessages;
    private StatusEnum mStatus;
    private String mTestName;
    private String mUsername;
}
