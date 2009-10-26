/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.realtors.rets.server.webapp.WebApp;

/**
 * A nonce table that tracks generated nonces and nonce counts to counter reply
 * attacks.
 */
public class NonceTable
{
    public NonceTable()
    {
        mNonceTable = new HashMap();
        mInitialTimeout = DEFAULT_INITIAL_TIMEOUT;
        mSuccessTimeout = DEFAULT_SUCCESS_TIMEOUT;
    }

    public synchronized String generateNonce()
    {
        String nonce = DigestUtils.md5Hex(
            "" + System.currentTimeMillis() +
            RandomStringUtils.randomAlphanumeric(5));
        mNonceTable.put(nonce, new Entry(nonce));
        LOG.debug("Created new nonce <" + nonce + ">, table size: " +
                  mNonceTable.size());
        return nonce;
    }

    public void addNonce(String nonce, String opaque, long nonceCount)
    {
        mNonceTable.put(nonce, new Entry(nonce, opaque, nonceCount));
    }

    public synchronized String getOpaque(String nonce)
    {
        Entry entry = (Entry) mNonceTable.get(nonce);
        return (entry == null ? null : entry.getOpaque());
    }

    /**
     * Validates a digest authorization request. If no qop is present, the
     * request is assumed to be in RFC 2609 format, otherwise the request is
     * assumed to be in RFC 2617 format. A successful validation updates the
     * expiration time.
     *
     * @param request Request to validate
     * @return <code>true</code> if the request is valide
     */
    public synchronized boolean validateRequest(
        DigestAuthorizationRequest request)
    {
        if (request.getQop() == null)
        {
            return validate2609Request(request.getNonce());
        }
        else
        {
            return validate2617Request(request.getNonce(), request.getOpaque(),
                                       request.getNonceCount());
        }
    }

    /**
     * Validate an RFC 2617 request, i.e. a request that contains a qop. A
     * valid request contains a nonce that is in the table, contains the
     * matching opaque value, and whose nonce count is alays increasing. A
     * successful validation updates the expiration time.
     *
     * @param nonce  The nonce from the request
     * @param opaque The opaque from the request
     * @param nonceCount The hex nonce count from the request
     * @return <code>true</code> if the request is valid
     */
    private boolean validate2617Request(String nonce, String opaque,
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
        // HPMA does not advance NC!
        if (!(currentCount > entry.getCurrentNonceCount() || 
                WebApp.getHPMAMode()))
        {
            LOG.warn("nonce count failed assertion: " + currentCount + " > " +
                     entry.getCurrentNonceCount());
            return false;
        }

        entry.setCurrentNonceCount(currentCount);
        entry.resetExpirationTime();
        return true;
    }

    /**
     * Validate an RFC 2069 request, i.e. no qop is specified. A valid request
     * contains a nonce that is in the table. A successful validation updates
     * the expiration time.
     *
     * @param nonce The nonce from the request
     * @return <code>true</code> if the request is valid
     */
    private boolean validate2609Request(String nonce)
    {
        Entry entry = (Entry) mNonceTable.get(nonce);
        if (entry == null)
        {
            LOG.debug("Unknown nonce: " + nonce);
            return false;
        }

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

        int numRemoved = 0;
        int numEntries = entries.size();
        for (int i = 0; i < numEntries; i++)
        {
            Entry entry = (Entry) entries.get(i);
            if (entry.getExpirationTime() <= currentTime)
            {
                synchronized (this)
                {
                    mNonceTable.remove(entry.getNonce());
                    LOG.debug("Removed old nonce <" + entry.getNonce() + ">");
                    numRemoved++;
                }
            }
        }
        if (numRemoved > 0)
        {
            LOG.debug("New table size: " + mNonceTable.size());
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

    public long getInitialTimeout()
    {
        return mInitialTimeout;
    }

    public void setInitialTimeout(long initialTimeout)
    {
        mInitialTimeout = initialTimeout;
    }

    public long getSuccessTimeout()
    {
        return mSuccessTimeout;
    }

    public void setSuccessTimeout(long successTimeout)
    {
        mSuccessTimeout = successTimeout;
    }

    private class Entry
    {
        public Entry(String nonce)
        {
            String opaque = "" + System.currentTimeMillis() +
                RandomStringUtils.randomAlphanumeric(5);
            init(nonce, DigestUtils.md5Hex(opaque), 0);
        }

        public Entry(String nonce, String opaque, long nonceCount)
        {
            init(nonce, opaque, nonceCount);
        }

        private void init(String nonce, String opaque, long nonceCount)
        {
            mNonce = nonce;
            mOpaque = opaque;
            mCurrentNonceCount = nonceCount;
            mExpirationTime =
                System.currentTimeMillis() + mInitialTimeout;
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
                System.currentTimeMillis() + mSuccessTimeout;
            LOG.debug("Nonce <" + mNonce + "> expirationTime: " +
                      mExpirationTime);
        }

        private String mOpaque;
        private long mCurrentNonceCount;
        private String mNonce;
        private long mExpirationTime;
    }

    private static final Logger LOG =
        Logger.getLogger(NonceTable.class);

    /** Initial timeout, before a succesful validation is 2 minutes.*/
    public static final long DEFAULT_INITIAL_TIMEOUT =
        2 * DateUtils.MILLIS_PER_MINUTE;

    /** Timeout after each successful validation is 5 minutes.*/
    public static final long DEFAULT_SUCCESS_TIMEOUT =
        5 * DateUtils.MILLIS_PER_MINUTE;

    private Map /* Entry */ mNonceTable;
    private long mInitialTimeout;
    private long mSuccessTimeout;
}
