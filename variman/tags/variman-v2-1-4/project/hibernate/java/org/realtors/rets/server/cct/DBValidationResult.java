/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 * Created on Sep 9, 2003
 *
 */
package org.realtors.rets.server.cct;

/**
 * @hibernate.class table="rets_cct_validationresults"
 */
public class DBValidationResult extends ValidationResult
{
    /**
     * @hibernate.id generator-class="native"
     */
    public Long getId()
    {
        return mId;
    }

    /**
     * @hibernate.property 
     */
    public String getUsername()
    {
        return mUsername;
    }

    /**
     * @param id The identifier
     */
    public void setId(Long id)
    {
        mId = id;
    }

    /**
     * @param username The username
     */
    public void setUsername(String username)
    {
        mUsername = username;
    }

    private Long mId;
    private String mUsername;
}
