/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

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
            mWriter.print(EscapeUtils.escapeXml(object.toString()));
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
            List strings = toSortedStringList(collection);
            append(StringUtils.join(strings.iterator(), ","));
        }
        else
        {
            mWriter.print("\t");
        }
    }

    /**
     * Takes toString() of each element in the collection and puts the values
     * into a list and then sorts the list.
     *
     * @param collection Collection to convert
     * @return sorted list of toString value foreach item in collection
     */
    private List toSortedStringList(Collection collection)
    {
        List strings = new ArrayList(collection.size());
        for (Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            Object o = iterator.next();
            strings.add(o.toString());
        }
        Collections.sort(strings);
        return strings;
    }

    private PrintWriter mWriter;
}
