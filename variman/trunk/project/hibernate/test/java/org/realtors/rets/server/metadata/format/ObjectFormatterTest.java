/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.ObjectTypeEnum;

public class ObjectFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        MObject object = new MObject();
        object.setObjectType(ObjectTypeEnum.THUMBNAIL);
        object.setMimeType("image/jpeg");
        object.setVisibleName("Small Photos");
        object.setDescription("A lower-resolution image");
        mObjects = new MObject[] {object};
    }

    private ObjectFormatter getFormatter(int format)
    {
        ObjectFormatter formatter = ObjectFormatter.getInstance(format);
        formatter.setVersion("1.00.001", getDate());
        formatter.setResourceName("Property");
        return formatter;
    }

    public void testCompactFormatObject()
    {
        ObjectFormatter formatter = getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), mObjects);
        assertEquals(
            "<METADATA-OBJECT Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tObjectType\tMimeType\tVisibleName\tDescription\t" +
            "</COLUMNS>\n" +

            "<DATA>\tThumbnail\timage/jpeg\tSmall Photos\t" +
            "A lower-resolution image\t</DATA>\n" +

            "</METADATA-OBJECT>\n",
            formatted.toString());
    }

    private MObject[] mObjects;
}