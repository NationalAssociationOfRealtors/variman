/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

import java.io.Serializable;
import java.text.NumberFormat;

/**
 * @hibernate.class table="rets_accounting"
 */
public class AccountingStatistics implements Serializable
{
    public AccountingStatistics()
    {
        mTotalTime = 0;
        startSession();
        mFormatter = NumberFormat.getInstance();
        mFormatter.setMaximumFractionDigits(2);
        mFormatter.setMinimumFractionDigits(2);
    }

    /**
     *
     * @return a Long object
     *
     * @hibernate.id generator-class="native"
     *   unsaved-value="null"
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
        mSessionTime = 0;
    }

    /**
     * Adds time to this session.
     *
     * @param time
     */
    public void addSessionTime(long time)
    {
        mSessionTime += time;
        mTotalTime += time;
        // Detect Overflow (number becomes negative) and reset to zero 
        // if it happens.
        if (mSessionTime < 0) mSessionTime = 0;
        if (mTotalTime < 0) mTotalTime = 0;
    }

    public long getSessionTime()
    {
        return mSessionTime;
    }

    public long getSessionDuration()
    {
        return System.currentTimeMillis() - mSessionStartTime;
    }

    public double getSessionBalance()
    {
        return mSessionTime * CHARGE_PER_SECOND;
    }

    public String getSessionBalanceFormatted()
    {
        return mFormatter.format(getSessionBalance());
    }

    /**
     *
     * @return a long
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
     * @return a User object
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

    public String getTotalBalanceFormatted()
    {
        return mFormatter.format(getTotalBalance());
    }

    private double getTotalBalance()
    {
        return mTotalTime * CHARGE_PER_SECOND;
    }

    public static final double CHARGE_PER_SECOND = 0.007;
    private Long mId;
    private long mSessionStartTime;
    private long mSessionTime;
    private long mTotalTime;
    private User mUser;
    private NumberFormat mFormatter;
}
