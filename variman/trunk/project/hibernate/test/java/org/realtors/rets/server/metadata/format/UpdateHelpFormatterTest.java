/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.List;
import java.util.ArrayList;

import org.realtors.rets.server.metadata.UpdateHelp;

public class UpdateHelpFormatterTest extends FormatterTestCase
{
    protected List getData()
    {
        UpdateHelp updateHelp = new UpdateHelp();
        updateHelp.setUpdateHelpID("1");
        updateHelp.setValue("Enter a number");
        List updateHelps = new ArrayList();
        updateHelps.add(updateHelp);
        return updateHelps;
    }

    protected String[] getLevels()
    {
        return new String[] {"Property"};
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactUpdateHelpFormatter();
    }

    protected MetadataFormatter getStandardFormatter()
    {
        return new StandardUpdateHelpFormatter();
    }

    protected String getExpectedCompact()
    {
        return
            "<METADATA-UPDATE_HELP Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">\n" +
            "<COLUMNS>\tUpdateHelpID\tValue\t</COLUMNS>\n" +
            "<DATA>\t1\tEnter a number\t</DATA>\n" +
            "</METADATA-UPDATE_HELP>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return getExpectedCompact();
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-UPDATE_HELP Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">" + EOL +
            "<UpdateHelp>" + EOL +
            "<UpdateHelpID>1</UpdateHelpID>" + EOL +
            "<Value>Enter a number</Value>" + EOL +
            "</UpdateHelp>" + EOL +
            "</METADATA-UPDATE_HELP>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return getExpectedStandard();
    }
}
