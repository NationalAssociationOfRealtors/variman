/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

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

    public abstract void format(PrintWriter out, List lookupTypes);

    public void setLevels(String[] levels)
    {
        mResourceName = levels[0];
        mLookupName = levels[1];
    }

    protected String mLookupName;
    protected String mResourceName;
}
