/*
 * Created on Sep 4, 2003
 *
 */
package org.realtors.rets.server.cct;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kgarner
 */
public class ValidationResults
{
    public ValidationResults()
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
            mResults.put(name, result);
        }

        return result;
    }
    
    private Map mResults;
}
