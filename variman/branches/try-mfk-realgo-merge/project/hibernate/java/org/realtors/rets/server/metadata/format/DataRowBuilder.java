/*
 * Variman RETS Server
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
 * method appends a value plus the delimiter character.
 */
public class DataRowBuilder
{

    protected static final String CRLF = "\r\n";

    public DataRowBuilder(PrintWriter writer)
    {
        this(writer, "\t");
    }

    public DataRowBuilder(PrintWriter writer, String delimiter)
    {
        mWriter = writer;
        mDelimiter = delimiter;
    }

    public void begin()
    {
        mWriter.print("<DATA>");
        mWriter.print(mDelimiter);
    }

    public void end()
    {
        mWriter.print("</DATA>");
        mWriter.print(CRLF);
    }
    public void append(Object object)
    {
        if (object != null)
        {
            mWriter.print(StringEscapeUtils.escapeXml(object.toString()));
        }
        mWriter.print(mDelimiter);
    }

    public void append(Date date)
    {
        if (date != null) {
            DateFormat formatter =
                new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            mWriter.print(formatter.format(date));
        }
        mWriter.print(mDelimiter);
    }

    public void appendDate(Date date)
    {
        if (date != null) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            mWriter.print(formatter.format(date));
        }
        mWriter.print(mDelimiter);
    }

    public void appendTime(Date time)
    {
        if (time != null) {
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            mWriter.print(formatter.format(time));
        }
        mWriter.print(mDelimiter);
    }

    public void appendDateTime(Date dateTime)
    {
        if (dateTime != null) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            mWriter.print(formatter.format(dateTime));
        }
        mWriter.print(mDelimiter);
    }

    public void append(int number)
    {
        mWriter.print(number);
        mWriter.print(mDelimiter);
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
        mWriter.print(mDelimiter);
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
            mWriter.print(mDelimiter);
        }
    }

    private PrintWriter mWriter;
    private String mDelimiter;
}
