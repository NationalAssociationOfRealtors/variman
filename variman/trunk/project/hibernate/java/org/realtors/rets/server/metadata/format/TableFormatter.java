/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

public abstract class TableFormatter extends MetadataFormatter
{
    public static TableFormatter getInstance(int format)
    {
        if (format == COMPACT)
        {
            return new CompactTableFormatter();
        }
        else
        {
            throw new IllegalArgumentException("Unknown format: " + format);
        }
    }

    public abstract void format(PrintWriter out, List tables);

    public void setLevels(String[] levels)
    {
        mResourceName = levels[0];
        mClassName = levels[1];
    }

    protected String mClassName;
    protected String mResourceName;
}
