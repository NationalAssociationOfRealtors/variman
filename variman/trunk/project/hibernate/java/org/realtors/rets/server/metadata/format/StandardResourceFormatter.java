/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.realtors.rets.server.metadata.Resource;

public class StandardResourceFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection resources,
                       String[] levels)
    {
        String version = context.getVersion();
        Date date = context.getDate();
        PrintWriter out = context.getWriter();

        TagBuilder metadata = new TagBuilder(out, "METADATA-RESOURCE")
            .appendAttribute("Version", version)
            .appendAttribute("Date", date)
            .beginContentOnNewLine();

        for (Iterator iterator = resources.iterator(); iterator.hasNext();)
        {
            Resource resource = (Resource) iterator.next();
            TagBuilder resourceTag = new TagBuilder(out, "Resource")
                .beginContentOnNewLine();

            new TagBuilder(out, "ResourceID")
                .beginContent()
                .print(resource.getResourceID())
                .close();

            new TagBuilder(out, "StandardName")
                .beginContent()
                .print(resource.getStandardName())
                .close();

            new TagBuilder(out, "VisibleName")
                .beginContent()
                .print(resource.getVisibleName())
                .close();

            new TagBuilder(out, "Description")
                .beginContent()
                .print(resource.getDescription())
                .close();

            new TagBuilder(out, "KeyField")
                .beginContent()
                .print(resource.getKeyField())
                .close();

            new TagBuilder(out, "ClassCount")
                .beginContent()
                .print(resource.getClasses().size())
                .close();

            for (int j = 0; j < VERSION_DATE_TAGS.length; j++)
            {
                String tagName = VERSION_DATE_TAGS[j];
                new TagBuilder(out, tagName + "Version")
                    .beginContent()
                    .print(version)
                    .close();

                new TagBuilder(out, tagName + "Date")
                    .beginContent()
                    .print(date)
                    .close();
            }

            if (context.isRecursive())
            {
                String[] path = resource.getPathAsArray();
                context.format(resource.getClasses(), path);
                context.format(resource.getObjects(), path);
                context.format(resource.getSearchHelps(), path);
                context.format(resource.getEditMasks(), path);
                context.format(resource.getLookups(), path);
                context.format(resource.getValidationLookups(), path);
                context.format(resource.getValidationExternals(), path);
                context.format(resource.getValidationExpressions(), path);
            }

            resourceTag.close();
        }

        metadata.close();
    }

    private static final String[] VERSION_DATE_TAGS = {
        "Class", "Object", "SearchHelp", "EditMask", "Lookup", "UpdateHelp",
        "ValidationExpression", "ValidationLookup", "ValidationExternal"
    };
}
