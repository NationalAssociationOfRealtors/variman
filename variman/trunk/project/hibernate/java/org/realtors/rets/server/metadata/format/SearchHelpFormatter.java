/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

public abstract class SearchHelpFormatter extends MetadataFormatter
{
    public static SearchHelpFormatter getInstance(int format)
    {
        if (format == COMPACT)
        {
            return new CompactSearchHelpFormatter();
        }
        else
        {
            throw new IllegalArgumentException("Unknown format: " + format);
        }
    }

    public abstract void format(PrintWriter out, List searchHelps);

    public void setLevels(String[] levels)
    {
        mResourceName = levels[0];
    }

    protected String mResourceName;
}
