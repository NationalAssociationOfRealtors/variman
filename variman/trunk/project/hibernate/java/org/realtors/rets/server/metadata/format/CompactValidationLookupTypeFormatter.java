/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;

import org.realtors.rets.server.metadata.ValidationLookupType;

public class CompactValidationLookupTypeFormatter
    extends ValidationLookupTypeFormatter
{
    public void format(PrintWriter out,
                       ValidationLookupType[] validationLookupTypes)
    {
        if (validationLookupTypes.length == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(out);
        tag.begin("METADATA-VALIDATION_LOOKUP_TYPE");
        tag.appendAttribute("Resource", mResourceName);
        tag.appendAttribute("ValidationLookup", mValidationLookupName);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(sColumns);
        for (int i = 0; i < validationLookupTypes.length; i++)
        {
            ValidationLookupType validationLookupType =
                validationLookupTypes[i];
            appendDataRow(out, validationLookupType);
        }
        tag.end();
    }

    private void appendDataRow(PrintWriter out,
                               ValidationLookupType validationLookupType)
    {
        DataRowBuilder row = new DataRowBuilder(out);
        row.begin();
        row.append(validationLookupType.getValidText());
        row.append(validationLookupType.getParent1Value());
        row.append(validationLookupType.getParent2Value());
        row.end();
    }

    private static final String[] sColumns = new String[] {
        "ValidText", "Parent1Value", "Parent2Value",
    };
}
