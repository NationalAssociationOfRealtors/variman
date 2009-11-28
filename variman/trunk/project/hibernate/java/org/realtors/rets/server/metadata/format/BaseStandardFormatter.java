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

import org.realtors.rets.common.util.TagBuilder;

public abstract class BaseStandardFormatter extends MetadataFormatter
{
    protected static void formatVersionDateTags(FormatterContext context,
                                       String[] versionDateTags)
    {
        for (int i = 0; i < versionDateTags.length; i++)
        {
            String tagName = versionDateTags[i];
            new TagBuilder(context.getWriter(), tagName + "Version")
                .beginContent()
                .print(context.getVersion())
                .close();

            new TagBuilder(context.getWriter(), tagName + "Date")
                .beginContent()
                .print(context.getDate(), context.getRetsVersion())
                .close();
        }
    }
}
