/*
 */
package org.realtors.rets.server.webapp.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.DigestUtils;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

/**
 * A nonce table that tracks generated nonces and nonce counts to counter reply
 * attacks.
 */
public class NonceTable
{
    public NonceTable()
    {
        mNonceTable = new HashMap();
    }

    public synchronized String generateNonce()
    {
        String nonce = DigestUtils.md5Hex(
            "" + System.currentTimeMillis() +
            RandomStringUtils.randomAlphanumeric(5));
        mNonceTable.put(nonce, new Entry(nonce));
        LOG.debug("Created new nonce <" + nonce + ">");
        return nonce;
    }

    public synchronized String getOpaque(String nonce)
    {
        Entry entry = (Entry) mNonceTable.get(nonce);
        return (entry == null ? null : entry.getOpaque());
    }

    public synchronized boolean validateRequest(String nonce, String opaque,
                                                String nonceCount)
    {
        Entry entry = (Entry) mNonceTable.get(nonce);
        if (entry == null)
        {
            LOG.debug("Unknown nonce: " + nonce);
            return false;
        }

        if (!entry.getOpaque().equals(opaque))
        {
            LOG.warn("opaque from client <" + opaque + "> does not match <" +
                     entry.getOpaque() + ">");
            return false;
        }

        long currentCount = Long.parseLong(nonceCount, 16);
        if (!(currentCount > entry.getCurrentNonceCount()))
        {
            LOG.warn("nonce count failed assertion: " + currentCount + " > " +
                     entry.getCurrentNonceCount());
            return false;
        }

        entry.setCurrentNonceCount(currentCount);
        entry.resetExpirationTime();
        return true;
    }

    public synchronized boolean isValidNonce(String nonce)
    {
        return mNonceTable.containsKey(nonce);
    }

    public void expireNonces()
    {
        long currentTime = System.currentTimeMillis();
        // Make an atomic copy of all entries so the lock is released while
        // iterating the entries
        List entries;
        synchronized (this)
        {
            entries = new ArrayList(mNonceTable.values());
        }

        int numEntries = entries.size();
        LOG.debug("Checking " + numEntries + " nonce entries");
        for (int i = 0; i < numEntries; i++)
        {
            Entry entry = (Entry) entries.get(i);
            if (entry.getExpirationTime() <= currentTime)
            {
                synchronized (this)
                {
                    mNonceTable.remove(entry.getNonce());
                    LOG.debug("Removed old nonce <" + entry.getNonce() + ">");
                }
            }
        }
    }

    public synchronized void setExpirationTime(String nonce, long time)
    {
        Entry entry = (Entry) mNonceTable.get(nonce);
        entry.setExpirationTime(time);
    }

    public synchronized long getExpirationTime(String nonce)
    {
        Entry entry = (Entry) mNonceTable.get(nonce);
        return entry.getExpirationTime();
    }

    private static class Entry
    {
        public Entry(String nonce)
        {
            String opaque = "" + System.currentTimeMillis() +
                RandomStringUtils.randomAlphanumeric(5);
            mNonce = nonce;
            mOpaque = DigestUtils.md5Hex(opaque);
            mCurrentNonceCount = 0;
            mExpirationTime =
                System.currentTimeMillis() + DEFAULT_INITIAL_TIMEOUT;
        }

        public String getNonce()
        {
            return mNonce;
        }

        public long getCurrentNonceCount()
        {
            return mCurrentNonceCount;
        }

        public void setCurrentNonceCount(long currentNonceCount)
        {
            mCurrentNonceCount = currentNonceCount;
        }

        public String getOpaque()
        {
            return mOpaque;
        }

        public long getExpirationTime()
        {
            return mExpirationTime;
        }

        public void setExpirationTime(long expirationTime)
        {
            mExpirationTime = expirationTime;
        }

        public void resetExpirationTime()
        {
            mExpirationTime =
                System.currentTimeMillis() + DEFAULT_SUCCESS_TIMEOUT;
        }

        private String mOpaque;
        private long mCurrentNonceCount;
        private String mNonce;
        private long mExpirationTime;
    }

    private static final Logger LOG =
        Logger.getLogger(NonceTable.class);

    /** Initial timeout, before a succesful validation is 10 minutes.*/
    public static final long DEFAULT_INITIAL_TIMEOUT =
        10 * DateUtils.MILLIS_IN_MINUTE;

    /** Timeout after each successful validation is 2 hours.*/
    public static final long DEFAULT_SUCCESS_TIMEOUT =
        2 * DateUtils.MILLIS_IN_HOUR;
//        DateUtils.MILLIS_IN_MINUTE;

    private Map /* Entry */ mNonceTable;
}
