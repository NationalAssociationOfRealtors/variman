/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004-2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Collection;
import java.util.Iterator;
import java.io.PrintWriter;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.types.MLookupType;
import org.realtors.rets.common.util.TagBuilder;

public class StandardLookupTypeFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> lookupTypes,
                       String[] levels)
    {
        RetsVersion retsVersion = context.getRetsVersion();
        PrintWriter out = context.getWriter();
        
        TagBuilder metadata = new TagBuilder(out, "METADATA-LOOKUP_TYPE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Lookup", levels[LOOKUP_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), retsVersion)
            .beginContentOnNewLine();

        for (Iterator<?> i = lookupTypes.iterator(); i.hasNext();)
        {
            MLookupType lookupType = (MLookupType) i.next();
            TagBuilder tag;
            
            // This is ugly: Lookup and LookupType were switched between 1.5, 1.7 and the 1.7.2 DTDs
            if (retsVersion.equals(RetsVersion.RETS_1_0) || retsVersion.equals(RetsVersion.RETS_1_5) ||
                    retsVersion.equals(RetsVersion.RETS_1_7))
            {
                tag = new TagBuilder(out, "Lookup")
                                .beginContentOnNewLine();
            }
            else
            {
                tag = new TagBuilder(out, "LookupType")
                            .beginContentOnNewLine();
            }

            if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5))
            {
                // Added 1.7 DTD
                TagBuilder.simpleTag(out, "MetadataEntryID", lookupType.getMetadataEntryID());
            }
            TagBuilder.simpleTag(out, "LongValue", lookupType.getLongValue());
            TagBuilder.simpleTag(out, "ShortValue", lookupType.getShortValue());
            TagBuilder.simpleTag(out, "Value", lookupType.getValue());

            tag.close();
        }

        metadata.close();
    }
}
