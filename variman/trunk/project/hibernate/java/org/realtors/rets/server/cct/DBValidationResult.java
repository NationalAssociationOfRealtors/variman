/*
 * Created on Sep 5, 2003
 *
 */
package org.realtors.rets.server.cct;

import java.util.List;

/**
 * @hibernate.class table="rets_cct_validationresults"
 */
public class DBValidationResult extends ValidationResult
{
    /**
     * 
     */
    public DBValidationResult()
    {
        super();
        mUsername = null;
    }

    /**
     * @hibernate.id generator-class="native"
     */
    public Long getId()
    {
        return mId;
    }

    /**
     * @hibernate.list table="rets_cct_vr_messages"
     * @hibernate.collection-key column="result_id"
     * @hibernate.collection-index column="message_index"
     * @hibernate.collection-element column="message" type="string"
     */
    public List getMessages()
    {
        return super.getMessages();
    }

    /**
     * @hibernate.property
     */
    public StatusEnum getStatus()
    {
        return super.getStatus();
    }

    /**
     * @hibernate.property
     */
    public String getTestName()
    {
        return super.getTestName();
    }
    
    /**
     * @hibernate.property
     */
    public String getUsername()
    {
        return mUsername;
    }

    public void setId(Long long1)
    {
        mId = long1;
    }

    public void setUsername(String info)
    {
        mUsername = info;
    }

    private Long mId;
    private String mUsername;
}
