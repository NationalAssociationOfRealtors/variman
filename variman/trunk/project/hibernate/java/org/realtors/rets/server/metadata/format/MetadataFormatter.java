/*
 */
package org.realtors.rets.server.metadata.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MetadataFormatter
{
    public static final int COMPACT = 0;

    public static void setDefaultFormat(int format)
    {
        sDefaultFormat = format;
    }

    public static int getDefaultFormat()
    {
        return sDefaultFormat;
    }

    protected String format(Date date)
    {
        DateFormat formatter =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formatter.format(date);
    }

    public void setVersion(String version, Date date)
    {
        mVersion = version;
        mDate = date;
    }

    private static int sDefaultFormat = COMPACT;
    protected String mVersion;
    protected Date mDate;
}
