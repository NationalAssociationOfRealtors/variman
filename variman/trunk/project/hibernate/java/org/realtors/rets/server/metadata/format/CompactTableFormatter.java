/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;

import org.realtors.rets.server.metadata.Table;

public class CompactTableFormatter extends TableFormatter
{
    public void format(PrintWriter out, Table[] tables)
    {
        TagBuilder tag = new TagBuilder(out);
        tag.begin("METADATA-TABLE");
        tag.appendAttribute("Resource", mResourceName);
        tag.appendAttribute("Class", mClassName);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(sColumns);
        for (int i = 0; i < tables.length; i++)
        {
            Table table = tables[i];
            appendDataRow(out, table);
        }
        tag.end();
    }

    private void appendDataRow(PrintWriter out, Table table)
    {
        DataRowBuilder row = new DataRowBuilder(out);
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
        row.append(table.getLookup().getLookupName());
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

    private static final String[] sColumns = new String[] {
        "SystemName", "StandardName", "LongName", "DBName", "ShortName",
        "MaximumLength", "DataType", "Precision", "Searchable",
        "Interpretation", "Alignment", "UseSeparator", "EditMaskID",
        "LookupName", "MaxSelect", "Units", "Index", "Minimum", "Maximum",
        "Default", "Required", "SearchHelpID", "Unique"
    };
}
