/*
 */
package org.realtors.rets.server.metadata.format;

import org.realtors.rets.server.metadata.MClass;
import org.apache.commons.lang.StringUtils;

public class CompactClassFormatter extends ClassFormatter
{
    public String format(MClass[] classes)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<METADATA-CLASS ");
        buffer.append("Resource=\"").append(mResource).append("\" ");
        buffer.append("Version=\"").append(mVersion).append("\" ");
        buffer.append("Date=\"").append(format(mDate)).append("\">\n");
        buffer.append("<COLUMNS>\t").append(StringUtils.join(sColumns,"\t"))
            .append("\t</COLUMNS>\n");
        for (int i = 0; i < classes.length; i++)
        {
            MClass clazz = classes[i];
            append(buffer, clazz);
        }
        buffer.append("</METADATA-CLASS>\n");
        return buffer.toString();
    }

    private void append(StringBuffer buffer, MClass clazz)
    {
        DataRowBuilder row = new DataRowBuilder(buffer);
        row.begin();
        row.append(clazz.getClassName());
        row.append(clazz.getStandardName());
        row.append(clazz.getVisibleName());
        row.append(DB_NAME);
        row.append(clazz.getDescription());
        // Table version and date
        row.append(mVersion);
        row.append(mDate);
        // Update version and date
        row.append((mVersion));
        row.append(mDate);
        row.end();
    }

    private static final String[] sColumns = {
        "ClassName", "StandardName", "VisibleName", "DBName", "Description",
        "TableVersion", "TableDate", "UpdateVersion", "UpdateDate",
    };

    private static final String DB_NAME = "";
}
