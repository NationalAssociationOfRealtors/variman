/*
 */
package org.realtors.rets.server.metadata.format;

import org.realtors.rets.server.metadata.format.MetadataFormatter;
import org.realtors.rets.server.metadata.MSystem;

public abstract class SystemFormatter extends MetadataFormatter
{
    public static SystemFormatter getInstance()
    {
        return getInstance(getDefaultFormat());
    }

    public static SystemFormatter getInstance(int format)
    {
        if (format == COMPACT)
        {
            return new CompactSystemFormatter();
        }
        else
        {
            throw new IllegalArgumentException("Unknown format: " + format);
        }
    }

    public abstract String format(MSystem system);
}
