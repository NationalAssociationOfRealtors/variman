/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.UpdateType;

public abstract class UpdateTypeFormatter extends MetadataFormatter
{
    public static UpdateTypeFormatter getInstance()
    {
        return getInstance(getDefaultFormat());
    }

    public static UpdateTypeFormatter getInstance(int format)
    {
        if (format == COMPACT)
        {
            return new CompactUpdateTypeFormatter();
        }
        else
        {
            throw new IllegalArgumentException("Unknown format: " + format);
        }
    }

    public abstract void format(PrintWriter out, List updateTypes);

    public void setLevels(String[] levels)
    {
        mResourceName = levels[0];
        mClassName = levels[1];
        mUpdateName = levels[2];
    }

    protected String mResourceName;
    protected String mClassName;
    protected String mUpdateName;
}
