/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;

import org.realtors.rets.server.metadata.ValidationExternal;

public class CompactValidationExternalFormatter
    extends ValidationExternalFormatter
{
    public void format(PrintWriter out,
                       ValidationExternal[] validationExternals)
    {
        TagBuilder tag = new TagBuilder(out);
        tag.begin("METADATA-VALIDATION_EXTERNAL");
        tag.appendAttribute("Resource", mResourceName);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(sColumns);
        for (int i = 0; i < validationExternals.length; i++)
        {
            ValidationExternal validationExternal = validationExternals[i];
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

    private static final String[] sColumns = new String[] {
        "ValidationExternalName", "SearchResource", "SearchClass", "Version",
        "Date",
    };
}
