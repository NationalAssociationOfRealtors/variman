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
import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.types.MTable;

public class CompactTableFormatter extends MetadataFormatter
{
    private Collection filterInaccessibleTables(FormatterContext context,
                                                Collection<MetaObject> tables,
                                                String[] levels)
    {
        String resource = levels[RESOURCE_LEVEL];
        String retsClass = levels[CLASS_LEVEL];
        List<MTable> filteredTables = new ArrayList<MTable>();
        for (Iterator<?> iterator = tables.iterator(); iterator.hasNext();)
        {
            MTable table = (MTable) iterator.next();
            if (context.isAccessibleTable(table, resource, retsClass))
            {
                filteredTables.add(table);
            }
        }
        return filteredTables;
    }

    public void format(FormatterContext context, Collection tables,
                       String[] levels)
    {
        tables = filterInaccessibleTables(context, tables, levels);
        if (tables.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(), "METADATA-TABLE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Class", levels[CLASS_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator<?> iterator = tables.iterator(); iterator.hasNext();)
        {
            MTable table = (MTable) iterator.next();
            appendDataRow(context, table);
        }
        tag.close();
    }

    // TODO: May be able to replace with a method similar to
    // org.realtors.rets.common.metadata.formatDataRow(MetaObject).
    private void appendDataRow(FormatterContext context, MTable table)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(table.getMetadataEntryID());
        row.append(table.getSystemName());
        row.append(table.getStandardName());
        row.append(table.getLongName());
        row.append(table.getDBName());
        row.append(table.getShortName());
        row.append(table.getMaximumLength());
        row.append(table.getDataType());
        row.append(table.getPrecision());
        row.append(table.getSearchable());
        row.append(table.getInterpretation());
        row.append(table.getAlignment());
        row.append(table.getUseSeparator());
        row.append(table.getEditMaskID());
        
        row.append(table.getLookupName());
        
        row.append(table.getMaxSelect());
        row.append(table.getUnits());
        row.append(table.getIndex());
        row.append(table.getMinimum());
        row.append(table.getMaximum());
        row.append(table.getDefault());
        row.append(table.getRequired());
        row.append(table.getSearchHelpID());
        row.append(table.getUnique());
        
        // 1.7.2
        row.append(table.getModTimeStamp());
        row.append(table.getForeignKeyName());
        row.append(table.getForeignField());
        row.append(table.getKeyQuery());
        row.append(table.getKeySelect());
        row.append(table.getInKeyIndex());
        
        row.end();
    }

    // TODO: Replace with static constants from org.realtors.rets.common.metadata.types.MTable
    // or use MTable.getAttributeNames() instead.
    private static final String[] COLUMNS = new String[] {
        "MetadataEntryID",
        "SystemName", "StandardName", "LongName", "DBName", "ShortName",
        "MaximumLength", "DataType", "Precision", "Searchable",
        "Interpretation", "Alignment", "UseSeparator", "EditMaskID",
        "LookupName", "MaxSelect", "Units", "Index", "Minimum", "Maximum",
        "Default", "Required", "SearchHelpID", "Unique",
        // 1.7.2
        "ModTimeStamp","ForeignKeyName","ForeignField",
        "KeyQuery","KeySelect","InKeyIndex",
    };
}
