/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;

import org.realtors.rets.server.metadata.ValidationLookup;

public class CompactValidationLookupFormatter extends ValidationLookupFormatter
{
    public void format(PrintWriter out, ValidationLookup[] validationLookups)
    {
        if (validationLookups.length == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(out);
        tag.begin("METADATA-VALIDATION_LOOKUP");
        tag.appendAttribute("Resource", mResourceName);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(sColumns);
        for (int i = 0; i < validationLookups.length; i++)
        {
            ValidationLookup validationLookup = validationLookups[i];
            appendDataRow(out, validationLookup);
        }
        tag.end();
    }

    private void appendDataRow(PrintWriter out,
                               ValidationLookup validationLookup)
    {
        DataRowBuilder row = new DataRowBuilder(out);
        row.begin();
        row.append(validationLookup.getValidationLookupName());
        row.append(validationLookup.getParent1Field());
        row.append(validationLookup.getParent2Field());
        row.append(mVersion);
        row.append(mDate);
        row.end();
    }

    private static final String[] sColumns = new String[] {
        "ValidationLookupName", "Parent1Field", "Parent2Field", "Version",
        "Date",
    };
}
