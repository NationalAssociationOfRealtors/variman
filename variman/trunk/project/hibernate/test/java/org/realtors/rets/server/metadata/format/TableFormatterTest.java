/*
 */
package org.realtors.rets.server.metadata.format;

import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.TableStandardName;
import org.realtors.rets.server.metadata.DataTypeEnum;
import org.realtors.rets.server.metadata.InterpretationEnum;
import org.realtors.rets.server.metadata.AlignmentEnum;

public class TableFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        Table table = new Table();
        table.setSystemName("E_SCHOOL");
        table.setStandardName(new TableStandardName("ElementarySchool"));
        table.setLongName("Elementary School");
        table.setShortName("ElemSchool");
        table.setMaximumLength(4);
        table.setDataType(DataTypeEnum.INT);
        table.setPrecision(1);
        table.setSearchable(true);
        table.setInterpretation(InterpretationEnum.LOOKUP);
        table.setAlignment(AlignmentEnum.LEFT);
        table.setUseSeparator(false);
//        table.setLookup(null);
        table.setMaxSelect(2);
        table.setIndex(3);
        table.setMinimum(4);
        table.setMaximum(5);
        table.setDefault(6);
        table.setRequired(7);
        table.setUnique(false);
        mTables = new Table[] {table};
    }

    public void testCompactFormatTable()
    {
        TableFormatter formatter = getFormatter(MetadataFormatter.COMPACT);
        String formatted = formatter.format(mTables);
        assertEquals(
            "<METADATA-TABLE Resource=\"Property\" Class=\"MOB\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\t" +
            "SystemName\tStandardName\tLongName\tDBName\t" +
            "ShortName\tMaximumLength\tDataType\tPrecision\tSearchable\t" +
            "Interpretation\tAlignment\tUseSeparator\tEditMaskID\t" +
            "LookupName\tMaxSelect\tUnits\tIndex\tMinimum\tMaximum\tDefault\t" +
            "Required\tSearchHelpID\tUnique\t" +
            "</COLUMNS>\n" +

            "<DATA>\t" +
            "E_SCHOOL\tElementarySchool\tElementary School\t" +
//            "E_SCHOOL\tElemnSchool\t4\tInt\t0\t1\tLookup\tLeft\t0\t\t\t" +
//            "0\t\t0\t0\t0\t0\t0\t0\t" +
            "</DATA>\n" +

            "</METADATA-TABLE>\n",
            formatted
        );
    }

    private TableFormatter getFormatter(int format)
    {
        TableFormatter formatter = TableFormatter.getInstance(format);
        formatter.setVersion("1.00.001", getDate());
        formatter.setClassName("MOB");
        formatter.setResourceName("Property");
        return formatter;
    }

    private Table[] mTables;
}
