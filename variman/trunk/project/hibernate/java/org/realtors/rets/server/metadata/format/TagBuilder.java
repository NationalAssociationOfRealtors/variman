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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.StringEscapeUtils;

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
        mWriter.print(value);
        mWriter.print("\"");
        return this;
    }

    public TagBuilder appendAttribute(String attribute, Date date)
    {
        DateFormat formatter =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        appendAttribute(attribute, formatter.format(date));
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

    public TagBuilder print(Date date)
    {
        DateFormat formatter =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return print(formatter.format(date));
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

    public TagBuilder print(Collection collection)
    {
        if (collection != null)
        {
            List strings = FormatUtil.toSortedStringList(collection);
            print(StringUtils.join(strings.iterator(), ","));
        }
        return this;
    }

    public static void emptyTag(PrintWriter writer, String tagName)
    {
        new TagBuilder(writer, tagName).close();
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

    public static void simpleTag(PrintWriter writer, String tagName,
                                 boolean value)
    {
        new TagBuilder(writer, tagName).beginContent().print(value).close();
    }

    public static void simpleTag(PrintWriter writer, String tagName,
                                 Collection value)
    {
        new TagBuilder(writer, tagName).beginContent().print(value).close();
    }

    private String mTagName;
    private PrintWriter mWriter;
    private boolean mContentIsEmpty;
}
