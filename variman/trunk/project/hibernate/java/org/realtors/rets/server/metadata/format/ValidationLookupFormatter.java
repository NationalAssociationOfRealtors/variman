/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.ValidationLookup;

public abstract class ValidationLookupFormatter extends MetadataFormatter
{
    public static ValidationLookupFormatter getInstance(int format)
    {
        if (format == COMPACT)
        {
            return new CompactValidationLookupFormatter();
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

    public abstract void format(PrintWriter out, List validationLookups);

    protected String mResourceName;
}
