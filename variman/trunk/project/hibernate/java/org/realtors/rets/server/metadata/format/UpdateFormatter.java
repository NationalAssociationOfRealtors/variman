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

    public abstract void format(PrintWriter out, List updates);

    public void setLevels(String[] levels)
    {
        mResourceName = levels[0];
        mClassName = levels[1];
    }

    protected String mClassName;
    protected String mResourceName;
}
