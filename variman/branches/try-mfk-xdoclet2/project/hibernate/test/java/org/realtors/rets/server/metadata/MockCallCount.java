/*
 */
package org.realtors.rets.server.metadata;

import junit.framework.Assert;

public class MockCallCount extends Assert
{
    public MockCallCount()
    {
        mShouldVerify = false;
    }

    public void setExpectedCallCount(int callCount)
    {
        mShouldVerify = true;
        mExpectedCallCount = callCount;
        mRealCallCount = 0;
    }

    public void increment()
    {
        mRealCallCount++;
    }

    public void verify()
    {
        if (mShouldVerify)
        {
            assertEquals(mExpectedCallCount, mRealCallCount);
        }
    }

    private boolean mShouldVerify;
    private int mExpectedCallCount;
    private int mRealCallCount;
}
