/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

public class TagBuilder
{

    public TagBuilder(PrintWriter writer)
    {
        mWriter = writer;
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

    public void appendAttribute(String attribute, String value)
    {
        mWriter.print(" ");
        mWriter.print(attribute);
        mWriter.print("=\"");
        mWriter.print(value);
        mWriter.print("\"");
    }

    public void appendAttribute(String attribute, Date date)
    {
        DateFormat formatter =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        appendAttribute(attribute, formatter.format(date));
    }

    public void endAttributes()
    {
        mWriter.print(">\n");
    }

    public void appendColumns(String[] columns)
    {
        mWriter.print("<COLUMNS>\t");
        mWriter.print(StringUtils.join(columns, "\t"));
        mWriter.print("\t</COLUMNS>\n");
    }

    private String mTagName;
    private PrintWriter mWriter;
}
