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
import org.realtors.rets.common.util.TagBuilder;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.types.MTable;

public class StandardTableFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> tables,
                       String[] levels)
    {
        RetsVersion retsVersion = context.getRetsVersion();
        PrintWriter out = context.getWriter();
        
        String resource = levels[RESOURCE_LEVEL];
        String retsClass = levels[CLASS_LEVEL];
        TagBuilder metadata = new TagBuilder(out, "METADATA-TABLE")
            .appendAttribute("Resource", resource)
            .appendAttribute("Class", retsClass)
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine();

        for (Iterator<?> i = tables.iterator(); i.hasNext();)
        {
            MTable table = (MTable) i.next();
            if (!context.isAccessibleTable(table, resource, retsClass))
            {
                continue;
            }
            TagBuilder tag = new TagBuilder(out, "Field").
                beginContentOnNewLine();

            if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5))
            {
                // Added 1.7 DTD
                TagBuilder.simpleTag(out, "MetadataEntryID", table.getMetadataEntryID());
            }
            TagBuilder.simpleTag(out, "SystemName", table.getSystemName());
            TagBuilder.simpleTag(out, "StandardName", table.getStandardName());
            TagBuilder.simpleTag(out, "LongName", table.getLongName());
            TagBuilder.simpleTag(out, "DBName", table.getDBName());
            TagBuilder.simpleTag(out, "ShortName", table.getShortName());
            TagBuilder.simpleTag(out, "MaximumLength",
                                 table.getMaximumLength());
            TagBuilder.simpleTag(out, "DataType", table.getDataType());
            TagBuilder.simpleTag(out, "Precision", table.getPrecision());
            TagBuilder.simpleTag(out, "Searchable", table.getSearchable());
            TagBuilder.simpleTag(out, "Interpretation",
                                 table.getInterpretation());
            TagBuilder.simpleTag(out, "Alignment", table.getAlignment());
            TagBuilder.simpleTag(out, "UseSeparator", table.getUseSeparator());

            TagBuilder.simpleTag(out, "EditMaskID", table.getEditMaskID());
 
            TagBuilder.simpleTag(out, "LookupName", table.getLookupName());

            TagBuilder.simpleTag(out, "MaxSelect", table.getMaxSelect());
            TagBuilder.simpleTag(out, "Units", table.getUnits());
            TagBuilder.simpleTag(out, "Index", table.getIndex());
            TagBuilder.simpleTag(out, "Minimum", table.getMinimum());
            TagBuilder.simpleTag(out, "Maximum", table.getMaximum());
            TagBuilder.simpleTag(out, "Default", table.getDefault());
            TagBuilder.simpleTag(out, "Required", table.getRequired());
            TagBuilder.simpleTag(out, "SearchHelpID", table.getSearchHelpID());
            TagBuilder.simpleTag(out, "Unique", table.getUnique());
            
            if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5))
            {
                // Added in 1.7 DTD
                TagBuilder.simpleTag(out, "ModTimeStamp", table.getModTimeStamp());
                TagBuilder.simpleTag(out, "ForeignKeyName", table.getForeignKeyName());
                TagBuilder.simpleTag(out, "ForeignField", table.getForeignField());
                TagBuilder.simpleTag(out, "KeyQuery", table.getKeyQuery());
                TagBuilder.simpleTag(out, "KeySelect", table.getKeySelect());
                if (!retsVersion.equals(RetsVersion.RETS_1_7))
                {
                    // Added in 1.7.2 DTD
                    TagBuilder.simpleTag(out, "InKeyIndex", table.getInKeyIndex());
                }
            }
            
            tag.close();
        }

        metadata.close();
    }
}
