/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.LookupType;

public abstract class LookupTypeFormatter extends MetadataFormatter
{
    public static LookupTypeFormatter getInstance(int format)
    {
        if (format == COMPACT)
        {
            return new CompactLookupTypeFormatter();
        }
        else
        {
            throw new IllegalArgumentException("Unknown format: " + format);
        }
    }

    public void setLookupName(String lookupName)
    {
        mLookupName = lookupName;
    }

    public void setResourceName(String resourceName)
    {
        mResourceName = resourceName;
    }

    public abstract void format(PrintWriter out, List lookupTypes);

    protected String mLookupName;
    protected String mResourceName;
}
