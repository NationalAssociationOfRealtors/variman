/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.Lookup;

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

    public void setResourceName(String resourceName)
    {
        mResourceName = resourceName;
    }

    public abstract void format(PrintWriter out, Lookup[] lookups);

    public void format(PrintWriter out, List lookups)
    {
        format(out, (Lookup[]) lookups.toArray(new Lookup[lookups.size()]));
    }

    protected String mResourceName;
}
