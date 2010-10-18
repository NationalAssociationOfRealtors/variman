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

import org.apache.commons.lang.StringUtils;

import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;

public class CompactResourceFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> resources,
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
        for (Iterator<?> iterator = resources.iterator(); iterator.hasNext();)
        {
            MResource resource = (MResource) iterator.next();
            appendDataRow(context, resource);
        }
        tag.close();

        if (context.isRecursive())
        {
            for (Iterator<?> i = resources.iterator(); i.hasNext();)
            {
                MResource resource = (MResource) i.next();
                String[] path = StringUtils.split(resource.getPath(), ":");
                context.format(resource.getChildren(MetadataType.CLASS), path);
                context.format(resource.getChildren(MetadataType.OBJECT), path);
                context.format(resource.getChildren(MetadataType.SEARCH_HELP), path);
                context.format(resource.getChildren(MetadataType.EDITMASK), path);
                context.format(resource.getChildren(MetadataType.LOOKUP), path);
                context.format(resource.getChildren(MetadataType.UPDATE_HELP), path);
                context.format(resource.getChildren(MetadataType.VALIDATION_LOOKUP), path);
                context.format(resource.getChildren(MetadataType.VALIDATION_EXTERNAL), path);
                context.format(resource.getChildren(MetadataType.VALIDATION_EXPRESSION), path);
            }
        }
    }

    private void appendDataRow(FormatterContext context, MResource resource)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(resource.getResourceID());
        row.append(resource.getStandardName());
        row.append(resource.getVisibleName());
        row.append(resource.getDescription());
        row.append(resource.getKeyField());
        row.append(resource.getClassCount());
        // FIXME: The actual version/date pairs are available in the common
        // MResource class. Consider using those instead of the context's
        // version and date.
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

    // FIXME: MetaObject.getAttributeNames() but takes a RetsVersion so the
    // correct attribute names are returned.
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
