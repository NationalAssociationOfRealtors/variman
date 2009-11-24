/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.common.metadata.types.MEditMask;

public class EditMaskFormatterTest extends FormatterTestCase
{
    protected List<MEditMask> getData()
    {
        List<MEditMask> editMasks = new ArrayList<MEditMask>();
        MEditMask editMask = new MEditMask();
        editMask.setMetadataEntryID("EditMask");
        editMask.setEditMaskID("LN_EDITMASK");
        editMask.setValue("[0-9]{4,8}");
        editMasks.add(editMask);
        return editMasks;
    }

    protected String[] getLevels()
    {
        return new String[] {"Property"};
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactEditMaskFormatter();
    }

    protected MetadataFormatter getStandardFormatter()
    {
        return new StandardEditMaskFormatter();
    }

    protected String getExpectedCompact()
    {
        return
            "<METADATA-EDITMASK Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tMetadataEntryID\tEditMaskID\tValue\t</COLUMNS>\n" +

            "<DATA>\tEditMask\tLN_EDITMASK\t[0-9]{4,8}\t</DATA>\n" +

            "</METADATA-EDITMASK>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return getExpectedCompact();
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-EDITMASK Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">" + EOL +
            "<EditMask>" + EOL +
            "<MetadataEntryID>EditMask</MetadataEntryID>" + EOL +
            "<EditMaskID>LN_EDITMASK</EditMaskID>" + EOL +
            "<Value>[0-9]{4,8}</Value>" + EOL +
            "</EditMask>" + EOL +
            "</METADATA-EDITMASK>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return getExpectedStandard();
    }
}