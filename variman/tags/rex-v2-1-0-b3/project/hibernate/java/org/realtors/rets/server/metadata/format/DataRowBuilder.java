/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

/***
 * Helper class to facilitate building rows in compact format. Each append()
 * method appends a value plus a tab character.
 */
public class DataRowBuilder
{

    public DataRowBuilder(PrintWriter writer)
    {
        mWriter = writer;
    }

    public void begin()
    {
        mWriter.print("<DATA>\t");
    }

    public void end()
    {
        mWriter.print("</DATA>\n");
    }
    public void append(Object object)
    {
        if (object != null)
        {
            mWriter.print(StringEscapeUtils.escapeXml(object.toString()));
        }
        mWriter.print("\t");
    }

    public void append(Date date)
    {
        DateFormat formatter =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        mWriter.print(formatter.format(date));
        mWriter.print("\t");
    }

    public void append(int number)
    {
        mWriter.print(number);
        mWriter.print("\t");
    }

    /**
     * Appends a "1" if <code>true</code> and "0" if false.
     *
     * @param b boolean value
     */
    public void append(boolean b)
    {
        if (b)
        {
            mWriter.print("1");
        }
        else
        {
            mWriter.print("0");
        }
        mWriter.print("\t");
    }

    public void append(Collection collection)
    {
        if (collection != null)
        {
            List strings = FormatUtil.toSortedStringList(collection);
            append(StringUtils.join(strings.iterator(), ","));
        }
        else
        {
            mWriter.print("\t");
        }
    }

    private PrintWriter mWriter;
}
