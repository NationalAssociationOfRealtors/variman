/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 * Created on Sep 5, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import org.realtors.rets.server.cct.ValidationResult;

/**
 *
 */
public class TestDisplayBean
{
    public TestDisplayBean(CertificationTest test, ValidationResult result)
    {
        mTest = test;
        mResult = result;
    }
    
    /**
     * 
     * @return
     */
    public ValidationResult getResult()
    {
        return mResult;
    }

    /**
     * 
     * @return
     */
    public CertificationTest getTest()
    {
        return mTest;
    }

    /**
     * 
     * @param result
     */
    public void setResult(ValidationResult result)
    {
        mResult = result;
    }

    /**
     * 
     * @param test
     */
    public void setTest(CertificationTest test)
    {
        mTest = test;
    }

    private ValidationResult mResult;
    private CertificationTest mTest;
}
