/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;

import org.realtors.rets.server.metadata.MClass;

public class CompactClassFormatter extends ClassFormatter
{
    public void format(PrintWriter out, MClass[] classes)
    {
        TagBuilder tag = new TagBuilder(out);
        tag.begin("METADATA-CLASS");
        tag.appendAttribute("Resource", mResource);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(sColumns);
        for (int i = 0; i < classes.length; i++)
        {
            MClass clazz = classes[i];
            appendDataRow(out, clazz);
        }
        tag.end();
    }

    private void appendDataRow(PrintWriter out, MClass clazz)
    {
        DataRowBuilder row = new DataRowBuilder(out);
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
