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

    public abstract void format(PrintWriter out, Table[] tables);

    public void setClassName(String className)
    {
        mClassName = className;
    }

    public void setResourceName(String resourceName)
    {
        mResourceName = resourceName;
    }

    public void format(PrintWriter out, List tables)
    {
        format(out, (Table[]) tables.toArray(new Table[tables.size()]));
    }

    protected String mClassName;
    protected String mResourceName;
}
