/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import org.realtors.rets.common.metadata.MetaObject;

public class TestFormatterLookup implements FormatterLookup
{
    public MetadataFormatter lookupFormatter(Collection<MetaObject> metadataCollection)
    {
        return sTestFormatter;
    }

    static class TestFormatter extends MetadataFormatter
    {
        public void format(FormatterContext context, Collection<MetaObject> metadatums,
                           String[] levels)
        {
            PrintWriter out = context.getWriter();
            int formatCount = 0;
            for (Iterator<MetaObject> iterator = metadatums.iterator(); iterator.hasNext();)
            {
                MetaObject metadata = iterator.next();
                out.print(metadata.getMetadataType().name() + "\n");
                formatCount++;
            }
            if (formatCount == 0)
            {
                // Print something so we can tell if recursive was invoked
                // unintentionally
                out.print("Empty list\n");
            }
        }
    }

    private static MetadataFormatter sTestFormatter = new TestFormatter();
}
