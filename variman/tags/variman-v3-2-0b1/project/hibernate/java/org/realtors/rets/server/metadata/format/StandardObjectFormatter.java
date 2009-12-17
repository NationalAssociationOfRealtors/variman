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
import org.realtors.rets.common.metadata.types.MObject;
import org.realtors.rets.common.util.TagBuilder;

public class StandardObjectFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> objects,
                       String[] levels)
    {
        RetsVersion retsVersion = context.getRetsVersion();
        PrintWriter out = context.getWriter();
        
        TagBuilder metadata = new TagBuilder(out, "METADATA-OBJECT")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine();

        for (Iterator<?> i = objects.iterator(); i.hasNext();)
        {
            MObject object = (MObject) i.next();
            TagBuilder tag = new TagBuilder(out, "Object")
                .beginContentOnNewLine();

            if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5))
            {
                // Added 1.7 DTD
                TagBuilder.simpleTag(out, "MetadataEntryID", object.getMetadataEntryID());
            }
            TagBuilder.simpleTag(out, "ObjectType", object.getObjectType());
            // Deleted in 1.7.2 DTD
            if (retsVersion.equals(RetsVersion.RETS_1_0) || retsVersion.equals(RetsVersion.RETS_1_5) ||
                    retsVersion.equals(RetsVersion.RETS_1_7))
            {
                TagBuilder.simpleTag(out, "StandardName", (Object) null);
            }
            TagBuilder.simpleTag(out, "MimeType", object.getMIMEType());
            if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5) &&
                    !retsVersion.equals(RetsVersion.RETS_1_7))
            {
                // Added in 1.7.2 DTD
                TagBuilder.simpleTag(out, "VisibleName", object.getVisibleName());
            }
            TagBuilder.simpleTag(out, "Description", object.getDescription());
            if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5))
            {
                // Added in 1.7 DTD
                TagBuilder.simpleTag(out, "ObjectTimeStamp", object.getObjectTimeStamp());
                TagBuilder.simpleTag(out, "ObjectCount", object.getObjectCount());
            }
            
            tag.close();
        }

        metadata.close();
    }
}
