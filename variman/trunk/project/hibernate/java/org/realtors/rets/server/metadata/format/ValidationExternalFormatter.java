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

    public void setResourceName(String resourceName)
    {
        mResourceName = resourceName;
    }

    public abstract void format(PrintWriter out,
                                ValidationExternal[] validationExternals);

    public void format(PrintWriter out, List validationExternals)
    {
        format(out, (ValidationExternal[])
            validationExternals.toArray(
                new ValidationExternal[validationExternals.size()]));
    }

    protected String mResourceName;
}
