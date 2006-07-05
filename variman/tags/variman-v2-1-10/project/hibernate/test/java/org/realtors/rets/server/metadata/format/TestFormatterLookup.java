/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import org.realtors.rets.server.metadata.ServerMetadata;

public class TestFormatterLookup implements FormatterLookup
{
    public MetadataFormatter lookupFormatter(Collection metadataCollection)
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
                out.print(metadata.getTableName() + "\n");
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
