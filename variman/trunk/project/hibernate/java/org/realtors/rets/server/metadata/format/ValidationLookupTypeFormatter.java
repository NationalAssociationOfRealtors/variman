/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

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

    public abstract void format(PrintWriter out, List validationLookupTypes);

    public void setLevels(String[] levels)
    {
        mResourceName = levels[0];
        mValidationLookupName = levels[1];
    }

    protected String mValidationLookupName;
    protected String mResourceName;
}
