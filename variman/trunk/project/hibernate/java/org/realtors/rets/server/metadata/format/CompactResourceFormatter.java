/*
 */
package org.realtors.rets.server.metadata.format;

import org.realtors.rets.server.metadata.Resource;
import org.apache.commons.lang.StringUtils;

public class CompactResourceFormatter
    extends ResourceFormatter
{
    public String format(Resource[] resources)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<METADATA-RESOURCE ");
        buffer.append("Version=\"").append(mVersion).append("\" ");
        buffer.append("Date=\"").append(format(mDate)).append("\">\n");
        buffer.append("<COLUMNS>\t")
            .append(StringUtils.join(sColumns, "\t"))
            .append("\t</COLUMNS>\n");
        for (int i = 0; i < resources.length; i++)
        {
            Resource resource = resources[i];
            append(buffer, resource);
        }
        buffer.append("</METADATA-RESOURCE>\n");
        return buffer.toString();
    }

    private void append(StringBuffer buffer, Resource resource)
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
