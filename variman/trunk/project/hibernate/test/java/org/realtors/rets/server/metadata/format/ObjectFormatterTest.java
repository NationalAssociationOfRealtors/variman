/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.ObjectTypeEnum;

public class ObjectFormatterTest extends FormatterTestCase
{
    protected List getData()
    {
        List objects = new ArrayList();
        MObject object = new MObject();
        object.setObjectType(ObjectTypeEnum.THUMBNAIL);
        object.setMimeType("image/jpeg");
        object.setVisibleName("Small Photos");
        object.setDescription("A lower-resolution image");
        objects.add(object);
        return objects;
    }

    protected String[] getLevels()
    {
        return new String[] {"Property"};
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactObjectFormatter();
    }

    protected String getExpectedCompact()
    {
        return
            "<METADATA-OBJECT Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tObjectType\tMimeType\tVisibleName\tDescription\t" +
            "</COLUMNS>\n" +

            "<DATA>\tThumbnail\timage/jpeg\tSmall Photos\t" +
            "A lower-resolution image\t</DATA>\n" +

            "</METADATA-OBJECT>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return
            "<METADATA-OBJECT Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tObjectType\tMimeType\tVisibleName\tDescription\t" +
            "</COLUMNS>\n" +

            "<DATA>\tThumbnail\timage/jpeg\tSmall Photos\t" +
            "A lower-resolution image\t</DATA>\n" +

            "</METADATA-OBJECT>\n";
    }
}