/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Collection;
import java.util.Iterator;

import org.realtors.rets.server.metadata.Table;

public class CompactTableFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection tables,
                       String[] levels)
    {
        if (tables.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(), "METADATA-TABLE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Class", levels[CLASS_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator iterator = tables.iterator(); iterator.hasNext();)
        {
            Table table = (Table) iterator.next();
            appendDataRow(context, table);
        }
        tag.close();
    }

    private void appendDataRow(FormatterContext context, Table table)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(table.getSystemName());
        row.append(table.getStandardName());
        row.append(table.getLongName());
        row.append(table.getDbName());
        row.append(table.getShortName());
        row.append(table.getMaximumLength());
        row.append(table.getDataType());
        row.append(table.getPrecision());
        row.append(table.isSearchable());
        row.append(table.getInterpretation());
        row.append(table.getAlignment());
        row.append(table.isUseSeparator());
        row.append(table.getEditMasks());

        String lookupName = null;
        if (table.getLookup() != null)
        {
            lookupName = table.getLookup().getLookupName();
        }
        row.append(lookupName);

        row.append(table.getMaxSelect());
        row.append(table.getUnits());
        row.append(table.getIndex());
        row.append(table.getMinimum());
        row.append(table.getMaximum());
        row.append(table.getDefault());
        row.append(table.getRequired());
        row.append(table.getSearchHelp());
        row.append(table.isUnique());
        row.end();
    }

    private static final String[] COLUMNS = new String[] {
        "SystemName", "StandardName", "LongName", "DBName", "ShortName",
        "MaximumLength", "DataType", "Precision", "Searchable",
        "Interpretation", "Alignment", "UseSeparator", "EditMaskID",
        "LookupName", "MaxSelect", "Units", "Index", "Minimum", "Maximum",
        "Default", "Required", "SearchHelpID", "Unique"
    };
}
