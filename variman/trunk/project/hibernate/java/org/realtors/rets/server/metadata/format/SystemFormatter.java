/*
 */
package org.realtors.rets.server.metadata.format;



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
}
