/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.Update;

public class CompactUpdateFormatter extends UpdateFormatter
{
    public void format(PrintWriter out, List updates)
    {
        if (updates.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(out);
        tag.begin("METADATA-UPDATE");
        tag.appendAttribute("Resource", mResourceName);
        tag.appendAttribute("Class", mClassName);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(sColumns);
        for (int i = 0; i < updates.size(); i++)
        {
            Update update = (Update) updates.get(i);
            appendDataRow(out, update);
        }
        tag.end();
    }

    private void appendDataRow(PrintWriter out, Update update)
    {
        DataRowBuilder row = new DataRowBuilder(out);
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
