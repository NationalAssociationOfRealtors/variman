/*
 */
package org.realtors.rets.server.metadata.format;

import org.realtors.rets.server.metadata.Table;
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
        buffer.append("<DATA>\t");
        buffer.append(table.getSystemName()).append("\t");
        buffer.append(table.getStandardName()).append("\t");
        buffer.append(table.getLongName()).append("\t");
        buffer.append("</DATA>\n");
    }

    private static final String[] sColumns = new String[] {
        "SystemName", "StandardName", "LongName", "DBName", "ShortName",
        "MaximumLength", "DataType", "Precision", "Searchable",
        "Interpretation", "Alignment", "UseSeparator", "EditMaskID",
        "LookupName", "MaxSelect", "Units", "Index", "Minimum", "Maximum",
        "Default", "Required", "SearchHelpID", "Unique"
    };
}
