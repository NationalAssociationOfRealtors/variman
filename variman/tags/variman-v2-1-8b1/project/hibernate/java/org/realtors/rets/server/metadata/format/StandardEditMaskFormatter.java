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

import org.realtors.rets.server.metadata.EditMask;

public class StandardEditMaskFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection editMasks,
                       String[] levels)
    {
        PrintWriter out = context.getWriter();
        TagBuilder metadata = new TagBuilder(out, "METADATA-EDITMASK")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine();

        for (Iterator i = editMasks.iterator(); i.hasNext();)
        {
            EditMask editMask = (EditMask) i.next();
            TagBuilder tag = new TagBuilder(out, "EditMask")
                .beginContentOnNewLine();

            TagBuilder.simpleTag(out, "EditMaskID", editMask.getEditMaskID());
            TagBuilder.simpleTag(out, "Value", editMask.getValue());

            tag.close();
        }

        metadata.close();
    }
}
