/*
 * Created on Sep 4, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import java.util.Iterator;

import org.apache.log4j.Logger;

import org.realtors.rets.server.cct.InMemoryValidationResults;
import org.realtors.rets.server.cct.StatusEnum;
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
             new InMemoryValidationResults());
    }

    public TestRunner(String context, CertificationTestSuite suite,
                      ValidationResults results)
    {
        mSuite = suite;
        mResults = results;
        mRunningTest = null;
        mTestContext = context;
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
        ValidationResult result = mResults.getResultByName(test.getName());
        result.reset();
        result.setStatus(StatusEnum.RUNNING);
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
            result.setStatus(StatusEnum.PASSED);
            mRunningTest.validate(result);
            mRunningTest = null;
        }
        else
        {
            LOG.warn("No running test to stop");
        }
    }

    public void stopRunningTest(int i)
    {
        CertificationTest test = mSuite.getTest(i);
        if (mRunningTest == test)
        {
            stopRunningTest();
        }
        else
        {
            LOG.warn("Trying to stop test that isn't actually running:" +
                       test.getName());
        }
    }

    /**
     *
     * @param i
     */
    public String getDescription(int i)
    {
        return mSuite.getTest(i).getDescription();
    }

    /**
     *
     * @return
     */
    public Iterator getTests()
    {
        return mSuite.getTests();
    }

    public StatusEnum getStatus(String name)
    {
        ValidationResult result = mResults.getResultByName(name);
        return result.getStatus();
    }

    public StatusEnum getStatus(int i)
    {
        CertificationTest test = mSuite.getTest(i);
        ValidationResult result = mResults.getResultByName(test.getName());
        return result.getStatus();
    }

    public ValidationResult getResult(String name)
    {
        return mResults.getResultByName(name);
    }

    private ValidationResults mResults;
    private CertificationTest mRunningTest;
    private CertificationTestSuite mSuite;
    private String mTestContext;
    private static final Logger LOG = Logger.getLogger(TestRunner.class);
}
