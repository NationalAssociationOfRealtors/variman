/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Collection;
import java.util.Iterator;

import org.realtors.rets.server.metadata.Update;

public class CompactUpdateFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection updates,
                       String[] levels)
    {
        if (updates.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(), "METADATA-UPDATE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Class", levels[CLASS_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator iterator = updates.iterator(); iterator.hasNext();)
        {
            Update update = (Update) iterator.next();
            appendDataRow(context, update);
        }
        tag.close();

        if (context.isRecursive())
        {
            for (Iterator iterator = updates.iterator(); iterator.hasNext();)
            {
                Update update = (Update) iterator.next();
                context.format(update.getUpdateTypes(),
                               update.getPathAsArray());
            }
        }
    }

    private void appendDataRow(FormatterContext context, Update update)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(update.getUpdateName());
        row.append(update.getDescription());
        row.append(update.getKeyField());
        row.append(context.getVersion());
        row.append(context.getDate());
        row.end();
    }

    private static final String[] COLUMNS = new String[] {
        "UpdateName", "Description", "KeyField", "Version", "Date",
    };
}
