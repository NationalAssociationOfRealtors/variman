/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.EditMask;

public class CompactEditMaskFormatter extends EditMaskFormatter
{
    public void format(PrintWriter out, List editMasks)
    {
        if (editMasks.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(out);
        tag.begin("METADATA-EDITMASK");
        tag.appendAttribute("Resource", mResourceName);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(COLUMNS);
        for (int i = 0; i < editMasks.size(); i++)
        {
            EditMask editMask = (EditMask) editMasks.get(i);
            appendDataRow(out, editMask);
        }
        tag.end();
    }

    private void appendDataRow(PrintWriter out, EditMask editMask)
    {
        DataRowBuilder row = new DataRowBuilder(out);
        row.begin();
        row.append(editMask.getEditMaskID());
        row.append(editMask.getValue());
        row.end();
    }

    private static final String[] COLUMNS = new String[] {
        "EditMaskID", "Value",
    };
}
