/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.SearchHelp;

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

    public void setResourceName(String resourceName)
    {
        mResourceName = resourceName;
    }

    public abstract void format(PrintWriter out, SearchHelp[] searchHelps);

    public void format(PrintWriter out, List searchHelps)
    {
        format(out, (SearchHelp[]) searchHelps.toArray(
            new SearchHelp[searchHelps.size()]));
    }

    protected String mResourceName;
}
