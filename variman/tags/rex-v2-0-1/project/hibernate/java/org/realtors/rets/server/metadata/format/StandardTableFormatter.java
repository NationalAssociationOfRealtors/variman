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

import org.realtors.rets.server.metadata.Table;

public class StandardTableFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection tables,
                       String[] levels)
    {
        PrintWriter out = context.getWriter();
        TagBuilder metadata = new TagBuilder(out, "METADATA-TABLE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Class", levels[CLASS_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine();

        for (Iterator i = tables.iterator(); i.hasNext();)
        {
            Table table = (Table) i.next();
            TagBuilder tag = new TagBuilder(out, "Field").
                beginContentOnNewLine();

            TagBuilder.simpleTag(out, "SystemName", table.getSystemName());
            TagBuilder.simpleTag(out, "StandardName", table.getStandardName());
            TagBuilder.simpleTag(out, "LongName", table.getLongName());
            TagBuilder.simpleTag(out, "DBName", table.getDbName());
            TagBuilder.simpleTag(out, "ShortName", table.getShortName());
            TagBuilder.simpleTag(out, "MaximumLength",
                                 table.getMaximumLength());
            TagBuilder.simpleTag(out, "DataType", table.getDataType());
            TagBuilder.simpleTag(out, "Precision", table.getPrecision());
            TagBuilder.simpleTag(out, "Searchable", table.isSearchable());
            TagBuilder.simpleTag(out, "Interpretation",
                                 table.getInterpretation());
            TagBuilder.simpleTag(out, "Alignment", table.getAlignment());
            TagBuilder.simpleTag(out, "UseSeparator", table.isUseSeparator());
            TagBuilder.simpleTag(out, "EditMaskID", table.getEditMasks());

            String lookupName = null;
            if (table.getLookup() != null)
            {
                lookupName = table.getLookup().getLookupName();
            }
            TagBuilder.simpleTag(out, "LookupName", lookupName);

            TagBuilder.simpleTag(out, "MaxSelect", table.getMaxSelect());
            TagBuilder.simpleTag(out, "Units", table.getUnits());
            TagBuilder.simpleTag(out, "Index", table.getIndex());
            TagBuilder.simpleTag(out, "Minimum", table.getMinimum());
            TagBuilder.simpleTag(out, "Maximum", table.getMaximum());
            TagBuilder.simpleTag(out, "Default", table.getDefault());
            TagBuilder.simpleTag(out, "Required", table.getRequired());
            TagBuilder.simpleTag(out, "SearchHelpID", table.getSearchHelp());
            TagBuilder.simpleTag(out, "Unique", table.isUnique());

            tag.close();
        }

        metadata.close();
    }
}
