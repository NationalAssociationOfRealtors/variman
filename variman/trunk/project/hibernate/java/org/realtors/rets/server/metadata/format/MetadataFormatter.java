/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Date;

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

    public void setVersion(String version, Date date)
    {
        mVersion = version;
        mDate = date;
    }

    private static int sDefaultFormat = COMPACT;
    protected String mVersion;
    protected Date mDate;
}
