/*
 */
package org.realtors.rets.server.metadata.format;

import org.realtors.rets.server.metadata.Resource;

public class CompactResourceFormatter
    extends ResourceFormatter
{
    public String format(Resource[] resources)
    {
        StringBuffer buffer = new StringBuffer();
        TagBuilder tag = new TagBuilder(buffer);
        tag.begin("METADATA-RESOURCE");
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(sColumns);
        for (int i = 0; i < resources.length; i++)
        {
            Resource resource = resources[i];
            appendDataRow(buffer, resource);
        }
        tag.end();
        return buffer.toString();
    }

    private void appendDataRow(StringBuffer buffer, Resource resource)
    {
        DataRowBuilder row = new DataRowBuilder(buffer);
        row.begin();
        row.append(resource.getResourceID());
        row.append(resource.getStandardName());
        row.append(resource.getVisibleName());
        row.append(resource.getDescription());
        row.append(resource.getKeyField());
        row.append(resource.getClasses().size());
        // There are 9 version/date pairs for the following tables: class,
        // object, search help, edit mask, lookup, update help, validation
        // expression, validation lookup, validation external.
        for (int i = 0; i < 9; i++)
        {
            row.append(mVersion);
            row.append(mDate);
        }
        row.end();
    }

    private static final String[] sColumns = {
        "ResourceID", "StandardName", "VisibleName", "Description", "KeyField",
        "ClassCount", "ClassVersion", "ClassDate", "ObjectVersion",
        "ObjectDate", "SearchHelpVersion", "SearchHelpDate", "EditMaskVersion",
        "EditMaskDate", "LookupVersion", "LookupDate", "UpdateHelpVersion",
        "UpdateHelpDate", "ValidationExpressionVersion",
        "ValidationExpressionDate", "ValidationLookupVersion",
        "ValidationLookupDate", "ValidationExternalVersion",
        "ValidationExternalDate"
    };
}
