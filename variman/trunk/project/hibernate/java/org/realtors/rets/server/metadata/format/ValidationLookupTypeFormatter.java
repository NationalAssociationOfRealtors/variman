/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;

import org.realtors.rets.server.metadata.ValidationLookupType;

abstract public class ValidationLookupTypeFormatter extends MetadataFormatter
{
    public static ValidationLookupTypeFormatter getInstance(int format)
    {
        if (format == COMPACT)
        {
            return new CompactValidationLookupTypeFormatter();
        }
        else
        {
            throw new IllegalArgumentException("Unknown format: " + format);
        }
    }

    public void setValidationLookupName(String validationLookupName)
    {
        mValidationLookupName = validationLookupName;
    }

    public void setResourceName(String resourceName)
    {
        mResourceName = resourceName;
    }

    public abstract void format(PrintWriter out,
                                ValidationLookupType[] validationLookupTypes);

    protected String mValidationLookupName;
    protected String mResourceName;
}
