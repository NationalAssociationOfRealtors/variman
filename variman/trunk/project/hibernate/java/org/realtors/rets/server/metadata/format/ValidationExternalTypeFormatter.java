/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.ValidationExternalType;

public abstract class ValidationExternalTypeFormatter extends MetadataFormatter
{
    public static ValidationExternalTypeFormatter getInstance(int format)
    {
        if (format == COMPACT)
        {
            return new CompactValidationExternalTypeFormatter();
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

    public void setValidationExternalName(String validationExternalName)
    {
        mValidationExternalName = validationExternalName;
    }

    public abstract void format(
        PrintWriter out, ValidationExternalType[] validationExternalTypes);

    public void format(PrintWriter out, List validationExternalTypes)
    {
        format(out, (ValidationExternalType[])
            validationExternalTypes.toArray(
                new ValidationExternalType[validationExternalTypes.size()]));
    }

    protected String mResourceName;
    protected String mValidationExternalName;
}
