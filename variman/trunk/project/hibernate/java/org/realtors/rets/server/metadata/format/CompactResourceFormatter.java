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
            .append(StringUtils.join(sResourceColumns, "\t"))
            .append("\t</COLUMNS>\n");
        for (int i = 0; i < resources.length; i++)
        {
            Resource resource = resources[i];
            append(buffer, resource);
        }
        buffer.append("</METADATA-RESOURCE>\n");
        return buffer.toString();
    }

    private String append(StringBuffer buffer, Resource resource)
    {
        buffer.append("<DATA>\t");
        buffer.append(resource.getResourceID()).append("\t");
        buffer.append(resource.getStandardName()).append("\t");
        buffer.append(resource.getVisibleName()).append("\t");
        buffer.append(resource.getDescription()).append("\t");
        buffer.append(resource.getKeyField()).append("\t");
        buffer.append(resource.getClasses().size()).append("\t");
        // There are 9 version/date pairs for the following tables: class,
        // object, search help, edit mask, lookup, update help, validation
        // expression, validation lookup, validation external.
        for (int i = 0; i < 9; i++)
        {
            buffer.append(mVersion).append("\t");
            buffer.append(format(mDate)).append("\t");
        }
        buffer.append("</DATA>\n");
        return buffer.toString();
    }

    private static final String[] sResourceColumns = {
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
