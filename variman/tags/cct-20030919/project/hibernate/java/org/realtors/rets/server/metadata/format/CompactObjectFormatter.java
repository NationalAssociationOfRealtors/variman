/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.MObject;

public class CompactObjectFormatter extends ObjectFormatter
{
    public void format(PrintWriter out, List objects)
    {
        if (objects.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(out);
        tag.begin("METADATA-OBJECT");
        tag.appendAttribute("Resource", mResourceName);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(sColumns);
        for (int i = 0; i < objects.size(); i++)
        {
            MObject object = (MObject) objects.get(i);
            appendDataRow(out, object);
        }
        tag.end();
    }

    private void appendDataRow(PrintWriter out, MObject object)
    {
        DataRowBuilder row = new DataRowBuilder(out);
        row.begin();
        row.append(object.getObjectType());
        row.append(object.getMimeType());
        row.append(object.getVisibleName());
        row.append(object.getDescription());
        row.end();
    }

    private static final String[] sColumns = new String[] {
        "ObjectType", "MimeType", "VisibleName", "Description"
    };
}
