/*
 */
package org.realtors.rets.server.metadata.format;

import org.realtors.rets.server.metadata.MObject;

public class CompactObjectFormatter extends ObjectFormatter
{
    public String format(MObject[] objects)
    {
        StringBuffer buffer = new StringBuffer();
        TagBuilder tag = new TagBuilder(buffer);
        tag.begin("METADATA-OBJECT");
        tag.appendAttribute("Resource", mResourceName);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(sColumns);
        for (int i = 0; i < objects.length; i++)
        {
            MObject object = objects[i];
            appendDataRow(buffer, object);
        }
        tag.end();
        return buffer.toString();
    }

    private void appendDataRow(StringBuffer buffer, MObject object)
    {
        DataRowBuilder row = new DataRowBuilder(buffer);
        row.begin();
        row.append(object.getObjectType());
        row.append(object.getMimeType());
        row.append(object.getVisibleName());
        row.append(object.getDescription());
        row.end();
        System.out.println("");
    }

    private static final String[] sColumns = new String[] {
        "ObjectType", "MimeType", "VisibleName", "Description"
    };
}
