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
import java.util.Date;
import java.util.Iterator;

import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;
import org.realtors.rets.server.metadata.Resource;

public class CompactResourceFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection resources,
                       String[] levels)
    {
        if (resources.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(),
                                        "METADATA-RESOURCE")
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator iterator = resources.iterator(); iterator.hasNext();)
        {
            Resource resource = (Resource) iterator.next();
            appendDataRow(context, resource);
        }
        tag.close();

        if (context.isRecursive())
        {
            for (Iterator i = resources.iterator(); i.hasNext();)
            {
                Resource resource = (Resource) i.next();
                String[] path = resource.getPathAsArray();
                context.format(resource.getClasses(), path);
                context.format(resource.getObjects(), path);
                context.format(resource.getSearchHelps(), path);
                context.format(resource.getEditMasks(), path);
                context.format(resource.getLookups(), path);
                context.format(resource.getUpdateHelps(), path);
                context.format(resource.getValidationLookups(), path);
                context.format(resource.getValidationExternals(), path);
                context.format(resource.getValidationExpressions(), path);
            }
        }
    }

    private void appendDataRow(FormatterContext context, Resource resource)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(resource.getResourceID());
        row.append(resource.getStandardName());
        row.append(resource.getVisibleName());
        row.append(resource.getDescription());
        row.append(resource.getKeyField());
        row.append(resource.getClasses().size());
        // There are 9 version/date pairs for the following tables: class,
        // object, search help, edit mask, lookup, update help, validation
        // expression, validation lookup, validation external.
        String version = context.getVersion();
        Date date = context.getDate();
        for (int i = 0; i < 9; i++)
        {
            row.append(version);
            row.append(date, context.getRetsVersion());
        }
        row.end();
    }

    private static final String[] COLUMNS = {
        "ResourceID", "StandardName", "VisibleName", "Description", "KeyField",
        "ClassCount", "ClassVersion", "ClassDate", "ObjectVersion",
        "ObjectDate", "SearchHelpVersion", "SearchHelpDate", "EditMaskVersion",
        "EditMaskDate", "LookupVersion", "LookupDate", "UpdateHelpVersion",
        "UpdateHelpDate", "ValidationExpressionVersion",
        "ValidationExpressionDate", "ValidationLookupVersion",
        "ValidationLookupDate", "ValidationExternalVersion",
        "ValidationExternalDate"
    };
}
