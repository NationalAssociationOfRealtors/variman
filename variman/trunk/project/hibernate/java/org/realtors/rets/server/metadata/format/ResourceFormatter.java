/*
 */
package org.realtors.rets.server.metadata.format;

import org.realtors.rets.server.metadata.Resource;

public abstract class ResourceFormatter extends MetadataFormatter
{
    public static ResourceFormatter getInstance()
    {
        return getInstance(getDefaultFormat());
    }

    public static ResourceFormatter getInstance(int format)
    {
        if (format == COMPACT)
        {
            return new CompactResourceFormatter();
        }
        else
        {
            throw new IllegalArgumentException("Unknow format: " + format);
        }
    }

    public abstract String format(Resource[] resources);
}
