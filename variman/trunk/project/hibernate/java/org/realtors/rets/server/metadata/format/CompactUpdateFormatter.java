/*
 */
package org.realtors.rets.server.metadata.format;

import org.realtors.rets.server.metadata.Update;

import org.apache.commons.lang.StringUtils;

public class CompactUpdateFormatter extends UpdateFormatter
{
    public String format(Update[] updates)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<METADATA-UPDATE ");
        buffer.append("Resource=\"").append(mResourceName).append("\" ");
        buffer.append("Class=\"").append(mClassName).append("\" ");
        buffer.append("Version=\"").append(mVersion).append("\" ");
        buffer.append("Date=\"").append(format(mDate)).append("\">\n");
        buffer.append("<COLUMNS>\t").append(StringUtils.join(sColumns, "\t"))
            .append("\t</COLUMNS>\n");
        for (int i = 0; i < updates.length; i++)
        {
            Update update = updates[i];
            appendRow(buffer, update);
        }
        buffer.append("</METADATA-UPDATE>\n");
        return buffer.toString();
    }

    private void appendRow(StringBuffer buffer, Update update)
    {
        DataRowBuilder row = new DataRowBuilder(buffer);
        row.begin();
        row.append(update.getUpdateName());
        row.append(update.getDescription());
        row.append(update.getKeyField());
        row.append(mVersion);
        row.append(mDate);
        row.end();
    }

    private static final String[] sColumns = new String[] {
        "UpdateName", "Description", "KeyField", "Version", "Date",
    };
}
