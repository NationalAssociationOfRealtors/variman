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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.realtors.rets.client.RetsVersion;

/***
 * Helper class to facilitate building rows in compact format. Each append()
 * method appends a value plus a tab character.
 */
public class DataRowBuilder
{

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
        mWriter.print("</DATA>\n");
    }
    public void append(Object object)
    {
        if (object != null)
        {
            mWriter.print(StringEscapeUtils.escapeXml(object.toString()));
        }
        mWriter.print(mDelimiter);
    }

    public void append(Date date, RetsVersion retsVersion)
    {
        if (date != null)
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
            mWriter.print(retsDate);
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

    public void append(Collection<?> collection)
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
