/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.MObject;

public abstract class ObjectFormatter extends MetadataFormatter
{
    public static ObjectFormatter getInstance()
    {
        return getInstance(getDefaultFormat());
    }

    public static ObjectFormatter getInstance(int format)
    {
        if (format == COMPACT)
        {
            return new CompactObjectFormatter();
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

    public abstract void format(PrintWriter out, List objects);

    protected String mResourceName;
}
