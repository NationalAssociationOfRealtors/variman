/*
 * Created on Sep 4, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import org.apache.commons.lang.StringUtils;

import org.realtors.rets.server.cct.ValidationResult;
import org.realtors.rets.server.cct.ValidationResults;

/**
 * @author kgarner
 */
public class TestRunner
{
    public TestRunner(String context)
    {
        this(context, new CertificationTestSuite(context),
             new ValidationResults());
    }
    
    public TestRunner(String context, CertificationTestSuite suite,
                      ValidationResults results)
    {
        mSuite = suite;
        mResults = results;
        mRunningTest = null;
        mTestContext = StringUtils.EMPTY;
    }
    
    public void startTestByName(String name)
    {
        CertificationTest test = mSuite.getTest(name);
        startTest(test);
    }
    
    public void startTestByNumber(int number)
    {
        CertificationTest test = mSuite.getTest(number);
        startTest(test);
    }
    
    private void startTest(CertificationTest test)
    {
        mRunningTest = test;
        test.init(mTestContext);
        test.start();
    }
    
    public void stopRunningTest()
    {
        if (mRunningTest != null)
        {
            ValidationResult result = 
                mResults.getResultByName(mRunningTest.getName());
            mRunningTest.stop();
            // todo: this won't compile
            // mRunningTest.validate(result);
            mRunningTest.validate();
            mRunningTest = null;
        }
    }

    private ValidationResults mResults;
    private CertificationTest mRunningTest;
    private CertificationTestSuite mSuite;
    private String mTestContext;
}
