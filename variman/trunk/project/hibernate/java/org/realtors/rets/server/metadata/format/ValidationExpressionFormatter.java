/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

public abstract class ValidationExpressionFormatter extends MetadataFormatter
{
    public static ValidationExpressionFormatter getInstance(int format)
    {
        if (format == COMPACT)
        {
            return new CompactValidationExpressionFormatter();
        }
        else
        {
            throw new IllegalArgumentException("Unknown format: " + format);
        }
    }

    public abstract void format(PrintWriter out, List validationExpressions);

    public void setLevels(String[] levels)
    {
        mResourceName = levels[0];
    }

    protected String mResourceName;
}
