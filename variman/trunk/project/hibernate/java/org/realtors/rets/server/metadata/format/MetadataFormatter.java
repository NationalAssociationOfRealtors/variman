/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Date;
import java.util.List;
import java.io.PrintWriter;

public abstract class MetadataFormatter
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

    public abstract void format(PrintWriter out, List systems);

    public void setLevels(String[] levels)
    {
    }

    private static int sDefaultFormat = COMPACT;
    protected String mVersion;
    protected Date mDate;
}
