/*
 */
package org.realtors.rets.server.metadata.format;

import org.realtors.rets.server.metadata.Update;

public class CompactUpdateFormatter extends UpdateFormatter
{
    public String format(Update[] updates)
    {
        StringBuffer buffer = new StringBuffer();
        TagBuilder tag = new TagBuilder(buffer);
        tag.begin("METADATA-UPDATE");
        tag.appendAttribute("Resource", mResourceName);
        tag.appendAttribute("Class", mClassName);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(sColumns);
        for (int i = 0; i < updates.length; i++)
        {
            Update update = updates[i];
            appendDataRow(buffer, update);
        }
        tag.end();
        return buffer.toString();
    }

    private void appendDataRow(StringBuffer buffer, Update update)
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
