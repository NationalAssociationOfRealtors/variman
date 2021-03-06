/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Collection;
import java.util.Iterator;
import java.io.PrintWriter;

import org.realtors.rets.server.metadata.ValidationLookupType;

public class StandardValidationLookupTypeFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context,
                       Collection validationLookupTypes, String[] levels)
    {
        PrintWriter out = context.getWriter();
        TagBuilder metadata =
            new TagBuilder(out, "METADATA-VALIDATION_LOOKUP_TYPE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("ValidationLookup",
                             levels[VALIDATION_LOOKUP_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine();

        for (Iterator i = validationLookupTypes.iterator(); i.hasNext();)
        {
            ValidationLookupType validationLookupType =
                (ValidationLookupType) i.next();
            TagBuilder tag = new TagBuilder(out, "ValidationLookup")
                .beginContentOnNewLine();

            TagBuilder.simpleTag(out, "ValidText",
                                 validationLookupType.getValidText());
            TagBuilder.simpleTag(out, "Parent1Value",
                                 validationLookupType.getParent1Value());
            TagBuilder.simpleTag(out, "Parent2Value",
                                 validationLookupType.getParent2Value());

            tag.close();
        }

        metadata.close();
    }
}
