/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Collection;
import java.util.Iterator;

import org.realtors.rets.server.metadata.UpdateType;

public class CompactUpdateTypeFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection updateTypes,
                       String[] levels)
    {
        if (updateTypes.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(),
                                        "METADATA-UPDATE_TYPE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Class", levels[CLASS_LEVEL])
            .appendAttribute("Update", levels[UPDATE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator iterator = updateTypes.iterator(); iterator.hasNext();)
        {
            UpdateType updateType = (UpdateType) iterator.next();
            apppendDataRow(context, updateType);
        }
        tag.close();
    }

    private void apppendDataRow(FormatterContext context, UpdateType updateType)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(updateType.getTable().getSystemName());
        row.append(updateType.getSequence());
        row.append(updateType.getAttributes());
        row.append(updateType.getDefault());
        row.append(updateType.getValidationExpressions());
        row.append(updateType.getUpdateHelp());
        row.append(updateType.getValidationLookup());
        row.append(updateType.getValidationExternal());
        row.end();
    }

    private static final String[] COLUMNS = new String[] {
        "SystemName", "Sequence", "Attributes", "Default",
        "ValidationExpressionID", "UpdateHelpID", "ValidationLookupName",
        "ValidationExternalName",
    };
}
