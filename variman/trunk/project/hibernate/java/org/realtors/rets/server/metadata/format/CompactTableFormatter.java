/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Set;

import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.SearchHelp;

import org.apache.commons.lang.StringUtils;

public class CompactTableFormatter extends TableFormatter
{
    public String format(Table[] tables)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<METADATA-TABLE ");
        buffer.append("Resource=\"").append(mResourceName).append("\" ");
        buffer.append("Class=\"").append(mClassName).append("\" ");
        buffer.append("Version=\"").append(mVersion).append("\" ");
        buffer.append("Date=\"").append(format(mDate)).append("\">\n");
        buffer.append("<COLUMNS>\t").append(StringUtils.join(sColumns, "\t"))
            .append("\t</COLUMNS>\n");
        for (int i = 0; i < tables.length; i++)
        {
            Table table = tables[i];
            append(buffer, table);
        }
        buffer.append("</METADATA-TABLE>\n");
        return buffer.toString();
    }

    private void append(StringBuffer buffer, Table table)
    {
        DataRowBuilder row = new DataRowBuilder(buffer);
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
