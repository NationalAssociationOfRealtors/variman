/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.Update;

public abstract class UpdateFormatter extends MetadataFormatter
{
    public static UpdateFormatter getInstance()
    {
        return getInstance(getDefaultFormat());
    }

    public static UpdateFormatter getInstance(int format)
    {
        if (format == COMPACT)
        {
            return new CompactUpdateFormatter();
        }
        else
        {
            throw new IllegalArgumentException("Unknown format: " + format);
        }
    }

    public abstract void format(PrintWriter out, Update[] updates);

    public void setClassName(String className)
    {
        mClassName = className;
    }

    public void setResourceName(String resourceName)
    {
        mResourceName = resourceName;
    }

    public void format(PrintWriter out, List updates)
    {
        format(out, (Update[]) updates.toArray(new Update[updates.size()]));
    }

    protected String mClassName;
    protected String mResourceName;
}
