/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    protected List getData()
    {
        List tables = new ArrayList();

        Table table = new Table(1);
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
        tables.add(table);

        table = new Table(2);
        table.setSystemName("AGENT_ID");
        table.setStandardName(new TableStandardName("ListAgentAgentID"));
        table.setLongName("Listing Agent ID");
        table.setShortName("AgentID");
        table.setDbName("AGENT_ID");
        table.setMaximum(6);
        table.setDataType(DataTypeEnum.CHARACTER);
        table.setPrecision(0);
        table.setSearchable(true);
        table.setAlignment(AlignmentEnum.LEFT);
        table.setMaxSelect(0);
        table.setIndex(0);
        table.setMinimum(0);
        table.setMaximum(0);
        table.setDefault(5);
        table.setRequired(0);
        table.setUnique(false);
        tables.add(table);
        return tables;
    }

    protected String[] getLevels()
    {
        return new String[] {"Property", "MOB"};
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactTableFormatter();
    }

    protected String getExpectedCompact()
    {
        return
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

            "<DATA>\t" +
            "AGENT_ID\tListAgentAgentID\tListing Agent ID\tAGENT_ID\t" +
            "AgentID\t0\tCharacter\t0\t1\t\tLeft\t0\t\t\t0\t\t0\t0\t0\t5" +
            "\t0\t\t0\t" +
            "</DATA>\n" +

            "</METADATA-TABLE>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return
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

            "<DATA>\t" +
            "AGENT_ID\tListAgentAgentID\tListing Agent ID\tAGENT_ID\t" +
            "AgentID\t0\tCharacter\t0\t1\t\tLeft\t0\t\t\t0\t\t0\t0\t0\t5" +
            "\t0\t\t0\t" +
            "</DATA>\n" +

            "</METADATA-TABLE>\n";
    }
}
