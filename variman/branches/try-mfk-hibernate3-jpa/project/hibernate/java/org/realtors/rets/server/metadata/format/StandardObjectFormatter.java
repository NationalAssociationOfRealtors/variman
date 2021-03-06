/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Collection;
import java.util.Iterator;
import java.io.PrintWriter;

import org.realtors.rets.common.util.TagBuilder;
import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.ObjectTypeEnum;

public class StandardObjectFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection objects,
                       String[] levels)
    {
        PrintWriter out = context.getWriter();
        TagBuilder metadata = new TagBuilder(out, "METADATA-OBJECT")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine();

        for (Iterator i = objects.iterator(); i.hasNext();)
        {
            MObject object = (MObject) i.next();
            TagBuilder tag = new TagBuilder(out, "Object")
                .beginContentOnNewLine();

            TagBuilder.simpleTag(out, "MetadataEntryID", object.getMetadataEntryID());
            TagBuilder.simpleTag(out, "ObjectType", ObjectTypeEnum.toString(object.getObjectType()));
            TagBuilder.simpleTag(out, "StandardName", (Object) null);
            TagBuilder.simpleTag(out, "MimeType", object.getMimeType());
            TagBuilder.simpleTag(out, "VisibleName", object.getVisibleName());
            TagBuilder.simpleTag(out, "Description", object.getDescription());
            TagBuilder.simpleTag(out, "ObjectTimeStamp", object.getObjectTimeStamp());
            TagBuilder.simpleTag(out, "ObjectCount", object.getObjectCount());
            
            tag.close();
        }

        metadata.close();
    }
}
