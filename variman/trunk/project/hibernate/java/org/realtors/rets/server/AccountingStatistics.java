/*
 */
package org.realtors.rets.server;

/**
 * @hibernate.class table="rets_accounting"
 */
public class AccountingStatistics
{
    public AccountingStatistics()
    {
        startSession();
    }

    /**
     *
     * @return
     *
     * @hibernate.id generator-class="native"
     */
    public Long getId()
    {
        return mId;
    }

    public void setId(Long id)
    {
        mId = id;
    }

    /**
     * Starts accounting for a session. Stores the current time and resets the
     * accumalted time.
     */
    public void startSession()
    {
        mSessionStartTime = System.currentTimeMillis();
        mSessionAccumalatedTime = 0;
    }

    /**
     * Adds time to this session.
     *
     * @param time
     */
    public void addSessionTime(long time)
    {
        mSessionAccumalatedTime += time;
        mTotalTime += time;
    }

    public long getSessionAccumalatedTime()
    {
        return mSessionAccumalatedTime;
    }

    public long getSessionDuration()
    {
        return System.currentTimeMillis() - mSessionStartTime;
    }

    /**
     *
     * @return
     *
     * @hibernate.property
     */
    public long getTotalTime()
    {
        return mTotalTime;
    }

    public void setTotalTime(long totalTime)
    {
        mTotalTime = totalTime;
    }

    /**
     *
     * @return
     *
     * @hibernate.many-to-one column="userId"
     */
    public User getUser()
    {
        return mUser;
    }

    public void setUser(User user)
    {
        mUser = user;
    }

    private Long mId;
    private long mSessionStartTime;
    private long mSessionAccumalatedTime;
    private long mTotalTime;
    private User mUser;
}
