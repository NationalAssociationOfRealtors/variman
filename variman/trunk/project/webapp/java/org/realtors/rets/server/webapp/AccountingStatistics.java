/*
 */
package org.realtors.rets.server.webapp;

public class AccountingStatistics
{
    public AccountingStatistics()
    {
        mCreateTime = System.currentTimeMillis();
        mAccumaltedTime = 0;
    }

    public void addTime(long time)
    {
        mAccumaltedTime += time;
    }

    public long getAccumaltedTime()
    {
        return mAccumaltedTime;
    }

    public long getDuration()
    {
        return System.currentTimeMillis() - mCreateTime;
    }

    public void resetStartTime()
    {
        mCreateTime = System.currentTimeMillis();
    }

    private long mCreateTime;
    private long mAccumaltedTime;
}
