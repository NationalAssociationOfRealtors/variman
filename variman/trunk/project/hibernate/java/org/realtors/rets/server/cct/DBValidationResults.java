/*
 * Rex RETS Server
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author kgarner
 */
public class DBValidationResults implements ValidationResults
{
    public DBValidationResults()
    {
        mResults = new HashMap();
    }

    public ValidationResult getResultByName(String name)
    {
        DBValidationResult result = (DBValidationResult) mResults.get(name);
        if (result == null)
        {
            result = new DBValidationResult();
            result.setUsername(mContext);
            result.setTestName(name);
            mResults.put(name, result);
        }

        return result;
    }
    
    public void addResult(ValidationResult result)
    {
        mResults.put(result.getTestName(), result);
    }

    /**
     * 
     * @return
     */
    public String getContext()
    {
        return mContext;
    }

    /**
     * 
     * @param string
     */
    public void setContext(String context)
    {
        mContext = context;
    }

    public Iterator iterator()
    {
        return mResults.values().iterator();
    }

    private Map mResults;
    private String mContext;
}
