/*
 */
package org.realtors.rets.server.webapp.auth;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

public class NonceReaper implements Runnable
{
    public NonceReaper(NonceTable nonceTable)
    {
        mNonceTable = nonceTable;
    }

    public void start()
    {
        LOG.debug("Starting nonce reaper");
        mReaper = new Thread(this, "Nonce reaper");
        mReaper.start();
    }

    public void stop()
    {
        LOG.debug("Stopping nonce reaper");
        Thread thread = mReaper;
        mReaper = null;
        thread.interrupt();
    }

    public void run()
    {
        Thread thisThread = Thread.currentThread();
        while (mReaper == thisThread)
        {
            try
            {
                thisThread.sleep(DateUtils.MILLIS_IN_MINUTE);
            }
            catch (InterruptedException e)
            {
                // Empty
            }
            mNonceTable.expireNonces();
        }
        LOG.debug("Stopped nonce reaper");
    }

    private static final Logger LOG =
        Logger.getLogger(NonceReaper.class);
    private volatile Thread mReaper;
    private NonceTable mNonceTable;
}
