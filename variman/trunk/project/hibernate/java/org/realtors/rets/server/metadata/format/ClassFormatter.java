/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.MClass;

public abstract class ClassFormatter extends MetadataFormatter
{
    public static ClassFormatter getInstance()
    {
        return getInstance(getDefaultFormat());
    }

    public static ClassFormatter getInstance(int format)
    {
        if (format == COMPACT)
        {
            return new CompactClassFormatter();
        }
        else
        {
            throw new IllegalArgumentException("Unknown format: " + format);
        }
    }

    public void setResourceName(String resource)
    {
        mResource = resource;
    }

    public abstract void format(PrintWriter out, List classesList);

    protected String mResource;
}
