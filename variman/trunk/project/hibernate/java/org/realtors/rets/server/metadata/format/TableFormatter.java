/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.Table;

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

    public void setClassName(String className)
    {
        mClassName = className;
    }

    public void setResourceName(String resourceName)
    {
        mResourceName = resourceName;
    }

    public abstract void format(PrintWriter out, List tables);

    protected String mClassName;
    protected String mResourceName;
}
