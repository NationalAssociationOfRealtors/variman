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
        buffer.append("<DATA>\t");
        buffer.append(clazz.getClassName()).append("\t");
        buffer.append(clazz.getStandardName()).append("\t");
        buffer.append(clazz.getVisibleName()).append("\t");
        buffer.append(DB_NAME).append("\t");
        buffer.append(clazz.getDescription()).append("\t");
        // Table version and date
        buffer.append(mVersion).append("\t");
        buffer.append(format(mDate)).append("\t");
        // Update version and date
        buffer.append((mVersion)).append("\t");
        buffer.append(format(mDate)).append("\t");
        buffer.append("</DATA>\n");
    }

    private static final String[] sColumns = {
        "ClassName", "StandardName", "VisibleName", "DBName", "Description",
        "TableVersion", "TableDate", "UpdateVersion", "UpdateDate",
    };

    private static final String DB_NAME = "";
}
