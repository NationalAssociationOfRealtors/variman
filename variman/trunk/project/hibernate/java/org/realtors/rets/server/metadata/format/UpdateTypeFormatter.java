/*
 */
package org.realtors.rets.server.metadata.format;

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

    public void setResourceName(String resourceName)
    {
        mResourceName = resourceName;
    }

    public void setClassName(String className)
    {
        mClassName = className;
    }

    public void setUpdateName(String updateName)
    {
        mUpdateName = updateName;
    }

    public abstract String format(UpdateType[] updateTypes);

    protected String mResourceName;
    protected String mClassName;
    protected String mUpdateName;
}
