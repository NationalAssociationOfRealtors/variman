/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 * Created on Sep 4, 2003
 *
 */
package org.realtors.rets.server.cct;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author kgarner
 */
public class InMemoryValidationResults implements ValidationResults
{
    public InMemoryValidationResults()
    {
        mResults = new HashMap();
    }
    
    public ValidationResult getResultByName(String name)
    {
        ValidationResult result = (ValidationResult) mResults.get(name);
        if (result == null)
        {
            result = new ValidationResult();
            result.setTestName(name);
            addResult(result);
        }

        return result;
    }
    
    public void addResult(ValidationResult result)
    {
        mResults.put(result.getTestName(), result);
    }

    public Iterator iterator()
    {
        return mResults.values().iterator();    
    }
    
    private Map mResults;
}
