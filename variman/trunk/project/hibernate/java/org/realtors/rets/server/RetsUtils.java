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

import java.io.PrintWriter;

public class RetsUtils
{
    public static void printOpenRetsSuccess(PrintWriter out)
    {
        printOpenRets(out, ReplyCode.SUCCESSFUL);
    }

    public static void printOpenRets(PrintWriter out, int replyCode,
                                     String replyText)
    {
        out.println("<RETS ReplyCode=\"" + replyCode + "\" " +
                    "ReplyText=\"" + replyText + "\">");
    }

    public static void printOpenRets(PrintWriter out, ReplyCode replyCode)
    {
        printOpenRets(out, replyCode.getValue(), replyCode.getName());
    }

    public static void printEmptyRets(PrintWriter out, int replyCode,
                                      String replyText)
    {
        out.print("<RETS ReplyCode=\"" + replyCode + "\" " +
                  "ReplyText=\"" + replyText + "\"/>\n");
    }

    public static void printEmptyRets(PrintWriter out, ReplyCode replyCode)
    {
        printEmptyRets(out, replyCode.getValue(), replyCode.getName());
    }

    public static void printRetsStatus(PrintWriter out, ReplyCode replyCode)
    {
        printRetsStatus(out, replyCode.getValue(), replyCode.getName());
    }

    public static void printRetsStatus(PrintWriter out, int replyCode,
                                       String replyText)
    {
        out.println("<RETS-STATUS ReplyCode=\"" + replyCode + "\" " +
                  "ReplyText=\"" + replyText + "\"/>");
    }

    public static void printCloseRets(PrintWriter out)
    {
        out.println("</RETS>");
    }

    public static void printOpenRetsResponse(PrintWriter out)
    {
        out.println("<RETS-RESPONSE>");
    }

    public static void printCloseRetsResponse(PrintWriter out)
    {
        out.println("</RETS-RESPONSE>");
    }

    public static void printXmlHeader(PrintWriter out)
    {
        out.println("<?xml version=\"1.0\"?>");
    }
}
