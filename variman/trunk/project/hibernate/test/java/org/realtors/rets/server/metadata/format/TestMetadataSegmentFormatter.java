/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.realtors.rets.server.metadata.ServerMetadata;

public class TestMetadataSegmentFormatter extends MetadataSegmentFormatter
{
    public TestMetadataSegmentFormatter(PrintWriter out)
    {
        super(out, MetadataFormatter.COMPACT);
    }

    public TestMetadataSegmentFormatter()
    {
        super(MetadataFormatter.COMPACT);
    }

    protected MetadataFormatter getFormatter(List metadataList)
    {
        return sTestFormatter;
    }

    public MetadataFormatter getFormatter(Collection metadataCollection)
    {
        return sTestFormatter;
    }

    static class TestFormatter extends MetadataFormatter
    {
        public void format(FormatterContext context, Collection metadatums,
                           String[] levels)
        {
            PrintWriter out = context.getWriter();
            int formatCount = 0;
            for (Iterator iterator = metadatums.iterator(); iterator.hasNext();)
            {
                ServerMetadata metadata = (ServerMetadata) iterator.next();
                out.println(metadata.getTableName());
                formatCount++;
            }
            if (formatCount == 0)
            {
                // Print something so we can tell if recursive was invoked
                // unintentionally
                out.println("Empty list");
            }
        }
    }

    private static MetadataFormatter sTestFormatter = new TestFormatter();
}
