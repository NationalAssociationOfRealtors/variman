/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Collection;
import java.util.Iterator;

import org.realtors.rets.server.metadata.EditMask;

public class CompactEditMaskFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection editMasks,
                       String[] levels)
    {
        if (editMasks.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(),
                                        "METADATA-EDITMASK")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator iterator = editMasks.iterator(); iterator.hasNext();)
        {
            EditMask editMask = (EditMask) iterator.next();
            appendDataRow(context, editMask);
        }
        tag.close();
    }

    private void appendDataRow(FormatterContext context, EditMask editMask)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(editMask.getEditMaskID());
        row.append(editMask.getValue());
        row.end();
    }

    private static final String[] COLUMNS = new String[] {
        "EditMaskID", "Value",
    };
}
