/*
 */
package org.realtors.rets.server.metadata.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

public class TagBuilder
{
    public TagBuilder(StringBuffer buffer)
    {
        mBuffer = buffer;
    }

    public void begin(String tagName)
    {
        mTagName = tagName;
        mBuffer.append("<").append(mTagName);
    }

    public void end()
    {
        mBuffer.append("</").append(mTagName).append(">\n");
    }

    public void appendAttribute(String attribute, String value)
    {
        mBuffer.append(" ").append(attribute);
        mBuffer.append("=\"").append(value).append("\"");
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
        mBuffer.append(">\n");
    }

    public void appendColumns(String[] columns)
    {
        mBuffer.append("<COLUMNS>\t").append(StringUtils.join(columns, "\t"))
            .append("\t</COLUMNS>\n");
    }

    private StringBuffer mBuffer;
    private String mTagName;
}
