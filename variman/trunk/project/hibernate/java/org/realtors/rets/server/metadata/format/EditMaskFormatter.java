/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.EditMask;

public abstract class EditMaskFormatter extends MetadataFormatter
{
    public static EditMaskFormatter getInstance(int format)
    {
        if (format == COMPACT)
        {
            return new CompactEditMaskFormatter();
        }
        else
        {
            throw new IllegalArgumentException("Unknown format: " + format);
        }
    }

    public abstract void format(PrintWriter out, List editMasks);

    public void setLevels(String[] levels)
    {
        mResourceName = levels[0];
    }

    protected String mResourceName;
}
