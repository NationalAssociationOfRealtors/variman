/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import org.realtors.rets.server.metadata.AlignmentEnum;
import org.realtors.rets.server.metadata.DataTypeEnum;
import org.realtors.rets.server.metadata.EditMask;
import org.realtors.rets.server.metadata.InterpretationEnum;
import org.realtors.rets.server.metadata.Lookup;
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.TableStandardName;
import org.realtors.rets.server.metadata.UnitEnum;

public class TableFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        Table table = new Table();
        table.setSystemName("E_SCHOOL");
        table.setStandardName(new TableStandardName("ElementarySchool"));
        table.setLongName("Elementary School");
        table.setShortName("ElemSchool");
        table.setDbName("E_SCHOOL");
        table.setMaximumLength(4);
        table.setDataType(DataTypeEnum.INT);
        table.setPrecision(0);
        table.setSearchable(true);
        table.setInterpretation(InterpretationEnum.LOOKUP);
        table.setAlignment(AlignmentEnum.LEFT);

        EditMask em1 = new EditMask(1);
        em1.setEditMaskID("EM1");
        EditMask em2 = new EditMask(2);
        em2.setEditMaskID("EM2");
        Set editMasks = new HashSet();
        editMasks.add(em1);
        editMasks.add(em2);
        table.setEditMasks(editMasks);

        table.setUseSeparator(false);
        Lookup lookup = new Lookup();
        lookup.setLookupName("E_SCHOOL");
        table.setLookup(lookup);
        table.setMaxSelect(1);
        table.setUnits(UnitEnum.FEET);
        table.setIndex(2);
        table.setMinimum(3);
        table.setMaximum(4);
        table.setDefault(5);
        table.setRequired(6);
        table.setUnique(false);
        mTables = new Table[] {table};
    }

    public void testCompactFormatTable()
    {
        TableFormatter formatter = getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), mTables);
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
            "E_SCHOOL\tElemSchool\t4\tInt\t0\t1\tLookup\tLeft\t0\tEM1,EM2\t" +
            "E_SCHOOL\t1\tFeet\t2\t3\t4\t5\t6\t\t0\t" +
            "</DATA>\n" +

            "</METADATA-TABLE>\n",
            formatted.toString());
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
