/*
 */
package org.realtors.rets.server.metadata.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

/***
 * Helper class to facilitate building rows in compact format. Each append()
 * method appends a value plus a tab character.
 */
public class DataRowBuilder
{
    public DataRowBuilder(StringBuffer buffer)
    {
        mBuffer = buffer;
    }

    public String toString()
    {
        return mBuffer.toString();
    }

    public DataRowBuilder begin()
    {
        mBuffer.append("<DATA>\t");
        return this;
    }

    public void end()
    {
        mBuffer.append("</DATA>\n");
    }

    public DataRowBuilder append(String string)
    {
        mBuffer.append(string).append("\t");
        return this;
    }

    public DataRowBuilder append(Object object)
    {
        if (object != null)
        {
            mBuffer.append(object.toString());
        }
        mBuffer.append("\t");
        return this;
    }

    public DataRowBuilder append(Date date)
    {
        DateFormat formatter =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        mBuffer.append(formatter.format(date)).append("\t");
        return this;
    }

    public DataRowBuilder append(int number)
    {
        mBuffer.append(number).append("\t");
        return this;
    }

    /**
     * Appends a "1" if <code>true</code> and "0" if false.
     *
     * @param b boolean value
     */
    public DataRowBuilder append(boolean b)
    {
        if (b)
        {
            mBuffer.append("1");
        }
        else
        {
            mBuffer.append("0");
        }
        mBuffer.append("\t");
        return this;
    }

    public DataRowBuilder append(Collection collection)
    {
        if (collection != null)
        {
            mBuffer.append(StringUtils.join(collection.iterator(), ","));
        }
        mBuffer.append("\t");
        return this;
    }

    private StringBuffer mBuffer;
}
