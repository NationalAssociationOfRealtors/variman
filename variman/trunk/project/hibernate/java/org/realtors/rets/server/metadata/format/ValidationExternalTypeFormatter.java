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

    public abstract void format(PrintWriter out, List validationExternalTypes);

    public void setLevels(String[] levels)
    {
        mResourceName = levels[0];
        mValidationExternalName = levels[1];
    }

    protected String mResourceName;
    protected String mValidationExternalName;
}
