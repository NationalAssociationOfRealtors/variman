/*
 * Copyright (c) 2004-2010, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.util;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.realtors.rets.client.RetsVersion;


public class TagBuilder
{

    public TagBuilder(PrintWriter writer)
    {
        mWriter = writer;
        mContentIsEmpty = true;
    }

    public TagBuilder(PrintWriter writer, String tagName)
    {
        this(writer);
        begin(tagName);
    }

    public void begin(String tagName)
    {
        mTagName = tagName;
        mWriter.print("<");
        mWriter.print(mTagName);
    }

    public void end()
    {
        mWriter.print("</");
        mWriter.print(mTagName);
        mWriter.print(">\n");
    }

    public void close()
    {
        if (mContentIsEmpty)
        {
            mWriter.print("/>\n");
        }
        else
        {
            mWriter.print("</");
            mWriter.print(mTagName);
            mWriter.print(">\n");
        }
    }

    public TagBuilder appendAttribute(String attribute, String value)
    {
        mWriter.print(" ");
        mWriter.print(attribute);
        mWriter.print("=\"");
        if (value != null)
            mWriter.print(value);
        mWriter.print("\"");
        return this;
    }

    public TagBuilder appendAttribute(String attribute, Date date, RetsVersion retsVersion)
    {
        SimpleDateFormat formatter =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        
        String retsDate;
        
        if (retsVersion.equals(RetsVersion.RETS_1_0) || retsVersion.equals(RetsVersion.RETS_1_5) ||
            retsVersion.equals(RetsVersion.RETS_1_7))
        {
            // Done this way to allow for all future versions that use the 1.7.2 format.
            retsDate = formatter.format(date);
        }
        else
        {
            formatter.applyPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            retsDate = formatter.format(date);
        }
        appendAttribute(attribute, retsDate);
        return this;
    }

    public void endAttributes()
    {
        mWriter.print(">\n");
    }

    public TagBuilder beginContent()
    {
        mWriter.print(">");
        mContentIsEmpty = false;
        return this;
    }

    public TagBuilder beginContentOnNewLine()
    {
        mWriter.print(">");
        mWriter.print("\n");
        mContentIsEmpty = false;
        return this;
    }

    public TagBuilder appendColumns(String[] columns)
    {
        mWriter.print("<COLUMNS>\t");
        mWriter.print(StringUtils.join(columns, "\t"));
        mWriter.print("\t</COLUMNS>\n");
        return this;
    }

    public TagBuilder print(Object message)
    {
        if (message != null)
        {
            mWriter.print(StringEscapeUtils.escapeXml(message.toString()));
        }
        return this;
    }

    public TagBuilder print(int i)
    {
        mWriter.print(i);
        return this;
    }

    public TagBuilder print(Date date, RetsVersion retsVersion)
    {
        SimpleDateFormat formatter =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        
        String retsDate;
        
        if (retsVersion.equals(RetsVersion.RETS_1_0) || retsVersion.equals(RetsVersion.RETS_1_5) ||
            retsVersion.equals(RetsVersion.RETS_1_7))
        {
            // Done this way to allow for all future versions that use the 1.7.2 format.
            retsDate = formatter.format(date);
        }
        else
        {
            formatter.applyPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            retsDate = formatter.format(date);
        }
        return print(retsDate);
    }

    public TagBuilder print(boolean b)
    {
        if (b)
        {
            mWriter.print("1");
        }
        else
        {
            mWriter.print("0");
        }
        return this;
    }

    public TagBuilder print(Boolean b)
    {
        if (b == null) {
            mWriter.print("");
        }
        if (b.booleanValue())
        {
            mWriter.print("1");
        }
        else
        {
            mWriter.print("0");
        }
        return this;
    }

    public TagBuilder print(Collection<String> collection)
    {
        if (collection != null)
        {
            List<String> strings = new ArrayList<String>(collection.size());
            for (Iterator<?> iterator = collection.iterator(); iterator.hasNext();)
            {
                Object o = iterator.next();
                strings.add(o.toString());
            }
            Collections.sort(strings);

            print(StringUtils.join(strings.iterator(), ","));
        }
        return this;
    }

    public static void emptyTag(PrintWriter writer, String tagName)
    {
        new TagBuilder(writer, tagName).close();
    }

    public TagBuilder emptyTag(String tagName)
    {
        emptyTag(mWriter, tagName);
        return this;
    }

    public static void simpleTag(PrintWriter writer, String tagName, int value)
    {
        new TagBuilder(writer, tagName).beginContent().print(value).close();
    }

    public static void simpleTag(PrintWriter writer, String tagName,
                                 String value)
    {
        new TagBuilder(writer, tagName).beginContent().print(value).close();
    }

    public static void simpleTag(PrintWriter writer, String tagName,
                                 Object value)
    {
        new TagBuilder(writer, tagName).beginContent().print(value).close();
    }

    public TagBuilder simpleTag(String tagName, Object value)
    {
        simpleTag(mWriter, tagName, value);
        return this;
    }

    public static void simpleTag(PrintWriter writer, String tagName,
                                 boolean value)
    {
        new TagBuilder(writer, tagName).beginContent().print(value).close();
    }

    public static void simpleTag(PrintWriter writer, String tagName,
                                 Boolean value)
    {
        new TagBuilder(writer, tagName).beginContent().print(value).close();
    }

    public static void simpleTag(PrintWriter writer, String tagName,
                                 Collection<?> value)
    {
        if (value != null)
        {
            List<String> strings = new ArrayList<String>(value.size());
            for (Iterator<?> iterator = value.iterator(); iterator.hasNext();)
            {
                Object o = iterator.next();
                strings.add(o.toString());
            }
            Collections.sort(strings);
            
            new TagBuilder(writer, tagName).beginContent().print(StringUtils.join(strings.iterator(), ",")).close();
        }
        else
            emptyTag(writer, tagName);
    }
    
    public static void simpleTag(PrintWriter writer, String tagName,
                                Date date, RetsVersion retsVersion)
    {
        SimpleDateFormat formatter =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        
        String retsDate;
        
        if (retsVersion.equals(RetsVersion.RETS_1_0) || retsVersion.equals(RetsVersion.RETS_1_5) ||
            retsVersion.equals(RetsVersion.RETS_1_7))
        {
            // Done this way to allow for all future versions that use the 1.7.2 format.
            retsDate = formatter.format(date);
        }
        else
        {
            formatter.applyPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            retsDate = formatter.format(date);
        }
        new TagBuilder(writer, tagName).beginContent().print(retsDate).close();
    }
    
    public PrintWriter getWriter()
    {
        return mWriter;
    }
    
    private String mTagName;
    private PrintWriter mWriter;
    private boolean mContentIsEmpty;
}
