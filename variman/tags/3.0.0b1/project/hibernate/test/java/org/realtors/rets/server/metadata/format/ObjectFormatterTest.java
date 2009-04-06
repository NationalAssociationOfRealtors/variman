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
        object.setMetadataEntryID("Thumbnail");
        object.setObjectType(ObjectTypeEnum.THUMBNAIL);
        object.setMimeType("image/jpeg");
        object.setVisibleName("Small Photos");
        object.setDescription("A lower-resolution image");
        object.setObjectTimeStamp("ThumbTimeStamp");
        object.setObjectCount("ThumbCount");
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

            "<COLUMNS>\tMetadataEntryID\tObjectType\tMimeType\tVisibleName\tDescription\t" +
            "ObjectTimeStamp\tObjectCount\t" +
            "</COLUMNS>\n" +

            "<DATA>\tThumbnail\tThumbnail\timage/jpeg\tSmall Photos\t" +
            "A lower-resolution image\tThumbTimeStamp\tThumbCount\t</DATA>\n" +

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
            "<MetadataEntryID>Thumbnail</MetadataEntryID>" + EOL +
            "<ObjectType>Thumbnail</ObjectType>" + EOL +
            "<StandardName></StandardName>" + EOL +
            "<MimeType>image/jpeg</MimeType>" + EOL +
	    "<VisibleName>Small Photos</VisibleName>" + EOL +
            "<Description>A lower-resolution image</Description>" + EOL +
            "<ObjectTimeStamp>ThumbTimeStamp</ObjectTimeStamp>" + EOL +
            "<ObjectCount>ThumbCount</ObjectCount>" + EOL +
            "</Object>" + EOL +
            "</METADATA-OBJECT>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return getExpectedStandard();
    }
}
