/*
 * Rex RETS Server
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

import org.realtors.rets.server.metadata.SearchHelp;

public class StandardSearchHelpFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection searchHelps,
                       String[] levels)
    {
        PrintWriter out = context.getWriter();
        TagBuilder metadata = new TagBuilder(out, "METADATA-SEARCH_HELP")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine();

        for (Iterator i = searchHelps.iterator(); i.hasNext();)
        {
            SearchHelp searchHelp = (SearchHelp) i.next();
            TagBuilder tag = new TagBuilder(out, "SearchHelp")
                .beginContentOnNewLine();

            TagBuilder.simpleTag(out, "SearchHelpID",
                                 searchHelp.getSearchHelpID());
            TagBuilder.simpleTag(out, "Value", searchHelp.getValue());

            tag.close();
        }

        metadata.close();
    }
}
