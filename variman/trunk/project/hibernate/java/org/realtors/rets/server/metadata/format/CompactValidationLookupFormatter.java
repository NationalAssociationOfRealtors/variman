/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Collection;
import java.util.Iterator;

import org.realtors.rets.server.metadata.ValidationLookup;

public class CompactValidationLookupFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection validationLookups,
                       String[] levels)
    {
        if (validationLookups.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(),
                                        "METADATA-VALIDATION_LOOKUP")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator i = validationLookups.iterator(); i.hasNext();)
        {
            ValidationLookup validationLookup = (ValidationLookup) i.next();
            appendDataRow(context, validationLookup);
        }
        tag.close();

        if (context.isRecursive())
        {
            for (Iterator i = validationLookups.iterator(); i.hasNext();)
            {
                ValidationLookup validationLookup = (ValidationLookup) i.next();
                context.format(validationLookup.getValidationLookupTypes(),
                               validationLookup.getPathAsArray());
            }
        }
    }

    private void appendDataRow(FormatterContext context,
                               ValidationLookup validationLookup)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(validationLookup.getValidationLookupName());
        row.append(validationLookup.getParent1Field());
        row.append(validationLookup.getParent2Field());
        row.append(context.getVersion());
        row.append(context.getDate());
        row.end();
    }

    private static final String[] COLUMNS = new String[] {
        "ValidationLookupName", "Parent1Field", "Parent2Field", "Version",
        "Date",
    };
}
