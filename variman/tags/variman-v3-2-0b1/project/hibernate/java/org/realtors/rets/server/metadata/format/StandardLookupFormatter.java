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

import org.apache.commons.lang.StringUtils;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MLookup;
import org.realtors.rets.common.util.TagBuilder;

public class StandardLookupFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> lookups,
                       String[] levels)
    {
        RetsVersion retsVersion = context.getRetsVersion();
        PrintWriter out = context.getWriter();
        
        TagBuilder metadata = new TagBuilder(out, "METADATA-LOOKUP")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), retsVersion)
            .beginContentOnNewLine();

        for (Iterator<?> i = lookups.iterator(); i.hasNext();)
        {
            MLookup lookup = (MLookup) i.next();
            
            TagBuilder tag;
            // This is ugly: Lookup and LookupType were switched between 1.5, 1.7 and the 1.7.2 DTDs
            if (retsVersion.equals(RetsVersion.RETS_1_0) || retsVersion.equals(RetsVersion.RETS_1_5) ||
                    retsVersion.equals(RetsVersion.RETS_1_7))
            {
                tag = new TagBuilder(out, "LookupType")
                                .beginContentOnNewLine();
            }
            else
            {
                tag = new TagBuilder(out, "Lookup")
                            .beginContentOnNewLine();
            }
            
            if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5))
            {
                // Added 1.7 DTD
                TagBuilder.simpleTag(out, "MetadataEntryID", lookup.getMetadataEntryID());
            }
            TagBuilder.simpleTag(out, "LookupName", lookup.getLookupName());
            TagBuilder.simpleTag(out, "VisibleName", lookup.getVisibleName());
            
            // FIXME: Prior to 1.7, the version/date names were Version and
            // Date. Not LookupTypeVerion and LookupTypeDate.
            formatVersionDateTags(context, VERSION_DATE_TAGS);

            if (context.isRecursive())
            {
                String[] path = StringUtils.split(lookup.getPath(), ":");
                context.format(lookup.getChildren(MetadataType.LOOKUP_TYPE), path);
            }

            tag.close();
        }

        metadata.close();
    }

    private static final String[] VERSION_DATE_TAGS = {"LookupType"};
}
