/*
 */
package org.realtors.rets.server;

import java.io.PrintWriter;

import org.apache.log4j.Logger;

public class LogoutTransaction extends RetsTransaction
{
    public void execute(PrintWriter out, AccountingStatistics stats)
    {
        LOG.debug("Duration: " + stats.getSessionDuration());

        printOpenRetsSuccess(out);
        printOpenRetsResponse(out);
        out.println("ConnectTime = " + stats.getSessionTime());
        out.println("Billing = " + stats.getSessionBalanceFormatted());
        out.println("SignOffMessage = Goodbye");
        printCloseRetsResponse(out);
        printCloseRets(out);
    }

    private static final Logger LOG =
        Logger.getLogger(LogoutTransaction.class);
}
