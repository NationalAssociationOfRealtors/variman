/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.ValidationExternal;

public class CompactValidationExternalFormatter
    extends ValidationExternalFormatter
{
    public void format(PrintWriter out, List validationExternals)
    {
        if (validationExternals.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(out);
        tag.begin("METADATA-VALIDATION_EXTERNAL");
        tag.appendAttribute("Resource", mResourceName);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(COLUMNS);
        for (int i = 0; i < validationExternals.size(); i++)
        {
            ValidationExternal validationExternal =
                (ValidationExternal) validationExternals.get(i);
            appendDataRow(out, validationExternal);
        }
        tag.end();
    }

    private void appendDataRow(PrintWriter out,
                               ValidationExternal validationExternal)
    {
        DataRowBuilder row = new DataRowBuilder(out);
        row.begin();
        row.append(validationExternal.getValidationExternalName());
        row.append(validationExternal.getSearchResource());
        row.append(validationExternal.getSearchClass());
        row.append(mVersion);
        row.append(mDate);
        row.end();
    }

    private static final String[] COLUMNS = new String[] {
        "ValidationExternalName", "SearchResource", "SearchClass", "Version",
        "Date",
    };
}
