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

    protected MetadataFormatter getStandardFormatter()
    {
        return new StandardObjectFormatter();
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
        return getExpectedCompact();
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-OBJECT Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">" + EOL +
            "<Object>" + EOL +
            "<ObjectType>Thumbnail</ObjectType>" + EOL +
            "<StandardName></StandardName>" + EOL +
            "<MimeType>image/jpeg</MimeType>" + EOL +
            "<Description>A lower-resolution image</Description>" + EOL +
            "</Object>" + EOL +
            "</METADATA-OBJECT>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return getExpectedStandard();
    }
}