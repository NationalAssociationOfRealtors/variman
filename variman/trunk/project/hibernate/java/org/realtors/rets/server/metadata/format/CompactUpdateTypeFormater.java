/*
 */
package org.realtors.rets.server.metadata.format;

import org.realtors.rets.server.metadata.UpdateType;

public class CompactUpdateTypeFormater extends UpdateTypeFormatter
{
    public String format(UpdateType[] updateTypes)
    {
        StringBuffer buffer = new StringBuffer();
        TagBuilder tag = new TagBuilder(buffer);
        tag.begin("METADATA-UPDATE_TYPE");
        tag.appendAttribute("Resource", mResourceName);
        tag.appendAttribute("Class", mClassName);
        tag.appendAttribute("Update", mUpdateName);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(sColumns);
        for (int i = 0; i < updateTypes.length; i++)
        {
            UpdateType updateType = updateTypes[i];
            apppendDataRow(buffer, updateType);
        }
        tag.end();
        return buffer.toString();
    }

    private void apppendDataRow(StringBuffer buffer, UpdateType updateType)
    {
        DataRowBuilder row = new DataRowBuilder(buffer);
        row.begin();
        row.append(updateType.getSystemName());
        row.append(updateType.getSequence());
        row.append(updateType.getAttributes());
        row.append(updateType.getDefault());
        row.append(updateType.getValidationExpressions());
        row.append(updateType.getUpdateHelp());
        row.append(updateType.getValidationLookup());
        row.append(updateType.getValidationExternal());
        row.end();
    }

    private static final String[] sColumns = new String[] {
        "SystemName", "Sequence", "Attributes", "Default",
        "ValidationExpressionID", "UpdateHelpID", "ValidationLookupName",
        "ValidationExternalName",
    };
}
