/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.metadata.Update;
import org.realtors.rets.server.metadata.UpdateType;

public class UpdateFormatterTest extends FormatterTestCase
{
    protected List getData()
    {
        List updates = new ArrayList();
        Update update = new Update();
        update.setUpdateName("Add");
        update.setDescription("Add a new Residential Listing");
        update.setKeyField("key");
        update.setUpdateTypeVersion(10000001);
        update.setUpdateTypeDate(getDate());
        update.addUpdateType(new UpdateType(1));
        update.setMetadataEntryID("Add");
        updates.add(update);
        return updates;
    }

    protected String[] getLevels()
    {
        return new String[] {"Property", "RES"};
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactUpdateFormatter();
    }

    protected MetadataFormatter getStandardFormatter()
    {
        return new StandardUpdateFormatter();
    }

    protected String getExpectedCompact()
    {
        return
            "<METADATA-UPDATE Resource=\"Property\" Class=\"RES\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tMetadataEntryID\tUpdateName\tDescription\t" + 
            "KeyField\tUpdateTypeVersion\tUpdateTypeDate\t" +
            "</COLUMNS>\n" +

            "<DATA>\tAdd\tAdd\tAdd a new Residential Listing\tkey" + VERSION_DATE +
            "\t</DATA>\n" +

            "</METADATA-UPDATE>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return
            "<METADATA-UPDATE Resource=\"Property\" Class=\"RES\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tMetadataEntryID\tUpdateName\tDescription\t" + 
            "KeyField\tUpdateTypeVersion\tUpdateTypeDate\t" +
            "</COLUMNS>\n" +

            "<DATA>\tAdd\tAdd\tAdd a new Residential Listing\tkey" + VERSION_DATE +
            "\t</DATA>\n" +

            "</METADATA-UPDATE>\n" +
            UpdateType.TABLE + "\n";
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-UPDATE Resource=\"Property\" Class=\"RES\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">" + EOL +
            "<UpdateType>" + EOL +
            "<MetadataEntryID>Add</MetadataEntryID>" + EOL +
            "<UpdateName>Add</UpdateName>" + EOL +
            "<Description>Add a new Residential Listing</Description>" + EOL +
            "<KeyField>key</KeyField>" + EOL +
            "<UpdateTypeVersion>" + VERSION + "</UpdateTypeVersion>" + EOL +
            "<UpdateTypeDate>" + DATE + "</UpdateTypeDate>" + EOL +
            "</UpdateType>" + EOL +
            "</METADATA-UPDATE>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return
            "<METADATA-UPDATE Resource=\"Property\" Class=\"RES\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">" + EOL +
            "<UpdateType>" + EOL +
            "<MetadataEntryID>Add</MetadataEntryID>" + EOL +
            "<UpdateName>Add</UpdateName>" + EOL +
            "<Description>Add a new Residential Listing</Description>" + EOL +
            "<KeyField>key</KeyField>" + EOL +
            "<UpdateTypeVersion>" + VERSION + "</UpdateTypeVersion>" + EOL +
            "<UpdateTypeDate>" + DATE + "</UpdateTypeDate>" + EOL +
            UpdateType.TABLE + EOL +
            "</UpdateType>" + EOL +
            "</METADATA-UPDATE>" + EOL;
    }
}
