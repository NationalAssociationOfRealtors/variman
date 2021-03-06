/*
 */
package org.realtors.rets.server;

import java.io.PrintWriter;

public class RetsTransaction
{
    protected void printOpenRetsSuccess(PrintWriter out)
    {
        printOpenRets(out, 0, "Operation Successful");
    }

    protected void printOpenRets(PrintWriter out, int replyCode,
                                 String replyText)
    {
        out.println("<RETS ReplyCode=\"" + replyCode + "\" " +
                    "ReplyText=\"" + replyText + "\">");
    }

    protected void printCloseRets(PrintWriter out)
    {
        out.println("</RETS>");
    }

    protected void printOpenRetsResponse(PrintWriter out)
    {
        out.println("<RETS-RESPONSE>");
    }

    protected void printCloseRetsResponse(PrintWriter out)
    {
        out.println("</RETS-RESPONSE>");
    }
}
