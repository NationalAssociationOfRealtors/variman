/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.ValidationExternal;

public abstract class ValidationExternalFormatter extends MetadataFormatter
{
    public static ValidationExternalFormatter getInstance(int format)
    {
        if (format == COMPACT)
        {
            return new CompactValidationExternalFormatter();
        }
        else
        {
            throw new IllegalArgumentException("Unknown format: " + format);
        }
    }

    public abstract void format(PrintWriter out, List validationExternals);

    public void setLevels(String[] levels)
    {
        mResourceName = levels[0];
    }

    protected String mResourceName;
}
