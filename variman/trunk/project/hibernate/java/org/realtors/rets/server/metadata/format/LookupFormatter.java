/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

public abstract class LookupFormatter extends MetadataFormatter
{
    public static LookupFormatter getInstance(int format)
    {
        if (format == COMPACT)
        {
            return new CompactLookupFormatter();
        }
        else
        {
            throw new IllegalArgumentException("Unknown format: " + format);
        }
    }

    public abstract void format(PrintWriter out, List lookups);

    public void setLevels(String[] levels)
    {
        mResourceName = levels[0];
    }

    protected String mResourceName;
}
