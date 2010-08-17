/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.realtors.rets.common.metadata.types.MEditMask;
import org.realtors.rets.common.metadata.types.MLookup;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.server.metadata.AlignmentEnum;
import org.realtors.rets.server.metadata.DataTypeEnum;
import org.realtors.rets.server.metadata.InterpretationEnum;
import org.realtors.rets.server.metadata.UnitEnum;
import org.realtors.rets.server.Group;
import org.realtors.rets.server.protocol.TableGroupFilter;
import org.realtors.rets.server.config.FilterRule;
import org.realtors.rets.server.config.GroupRules;

public class TableFormatterTest extends FormatterTestCase
{
    public TableFormatterTest()
    {
        Set<MTable> allTables = new HashSet<MTable>();

        mSchool = new MTable();
        mSchool.setUniqueId(Long.valueOf(1));
        mSchool.setSystemName("E_SCHOOL");
        mSchool.setStandardName("ElementarySchool");
        mSchool.setLongName("Elementary School");
        mSchool.setShortName("ElemSchool");
        mSchool.setDBName("E_SCHOOL");
        mSchool.setMaximumLength(4);
        mSchool.setDataType(DataTypeEnum.INT.toString());
        mSchool.setPrecision(0);
        mSchool.setSearchable(true);
        mSchool.setInterpretation(InterpretationEnum.LOOKUP.toString());
        mSchool.setAlignment(AlignmentEnum.LEFT.toString());
        mSchool.setMetadataEntryID("ElementarySchool");

        MEditMask em1 = new MEditMask();
        em1.setUniqueId(Long.valueOf(1));
        em1.setEditMaskID("EM1");
        MEditMask em2 = new MEditMask();
        em2.setUniqueId(Long.valueOf(2));
        em2.setEditMaskID("EM2");
        Set<MEditMask> editMasks = new LinkedHashSet<MEditMask>();
        editMasks.add(em1);
        editMasks.add(em2);
        String editMaskId = MTable.toEditMaskId(editMasks);
        mSchool.setEditMaskID(editMaskId);

        mSchool.setUseSeparator(false);
        MLookup lookup = new MLookup();
        lookup.setLookupName("E_SCHOOL");
        mSchool.setMLookup(lookup);

        mSchool.setMaxSelect(1);
        mSchool.setUnits(UnitEnum.FEET.toString());
        mSchool.setIndex(true);
        mSchool.setMinimum(3);
        mSchool.setMaximum(4);
        mSchool.setDefault(5);
        mSchool.setRequired(6);
        mSchool.setUnique(false);
        allTables.add(mSchool);

        mAgent = new MTable();
        mAgent.setUniqueId(Long.valueOf(2));
        mAgent.setSystemName("AGENT_ID");
        mAgent.setStandardName("ListAgentAgentID");
        mAgent.setLongName("Listing Agent ID");
        mAgent.setShortName("AgentID");
        mAgent.setDBName("AGENT_ID");
        mAgent.setMaximum(6);
        mAgent.setDataType(DataTypeEnum.CHARACTER.toString());
        mAgent.setPrecision(0);
        mAgent.setSearchable(true);
        mAgent.setAlignment(AlignmentEnum.LEFT.toString());
        mAgent.setMaxSelect(0);
        mAgent.setIndex(false);
        mAgent.setMinimum(0);
        mAgent.setMaximum(0);
        mAgent.setDefault(5);
        mAgent.setRequired(0);
        mAgent.setUnique(false);
        mAgent.setMetadataEntryID("ListAgentAgentID");
        allTables.add(mAgent);

        mListingPrice = new MTable();
        mListingPrice.setUniqueId(Long.valueOf(3));
        mListingPrice.setSystemName("LISTING_PRICE");
        mListingPrice.setStandardName("ListingPrice");
        mListingPrice.setLongName("Listing Price");
        mListingPrice.setShortName("ListingPrice");
        mListingPrice.setDBName("LP");
        mListingPrice.setMaximum(6);
        mListingPrice.setDataType(DataTypeEnum.INT.toString());
        mListingPrice.setPrecision(0);
        mListingPrice.setSearchable(true);
        mListingPrice.setAlignment(AlignmentEnum.LEFT.toString());
        mListingPrice.setMaxSelect(0);
        mListingPrice.setIndex(false);
        mListingPrice.setMinimum(0);
        mListingPrice.setMaximum(0);
        mListingPrice.setDefault(5);
        mListingPrice.setRequired(0);
        mListingPrice.setUnique(false);
        mListingPrice.setMetadataEntryID("ListingPrice");
        allTables.add(mListingPrice);

        mGroupFilter = new TableGroupFilter();
        mGroupFilter.setTables("Property", "MOB", allTables);

        mNewspapers = new Group("Newspapers");
        mGroups = new HashSet<Group>();
        mGroups.add(mNewspapers);

        FilterRule filterRule = new FilterRule(FilterRule.EXCLUDE);
        filterRule.setResource("Property");
        filterRule.setRetsClass("MOB");
        filterRule.addSystemName("LISTING_PRICE");
        GroupRules rules = new GroupRules(mNewspapers.getName());
        rules.addFilterRule(filterRule);
        mGroupFilter.addRules(rules);
    }

    protected List<MTable> getData()
    {
        List<MTable> tables = new ArrayList<MTable>();
        tables.add(mSchool);
        tables.add(mAgent);
        tables.add(mListingPrice);
        return tables;
    }

    protected TableGroupFilter getGroupFilter()
    {
        return mGroupFilter;
    }

    protected Set<Group> getGroups()
    {
        return mGroups;
    }

    protected String[] getLevels()
    {
        return new String[] {"Property", "MOB"};
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactTableFormatter();
    }

    protected MetadataFormatter getStandardFormatter()
    {
        return new StandardTableFormatter();
    }

    protected String getExpectedCompact()
    {
        return
            "<METADATA-TABLE Resource=\"Property\" Class=\"MOB\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\t" +
            "MetadataEntryID\tSystemName\tStandardName\tLongName\tDBName\t" +
            "ShortName\tMaximumLength\tDataType\tPrecision\tSearchable\t" +
            "Interpretation\tAlignment\tUseSeparator\tEditMaskID\t" +
            "LookupName\tMaxSelect\tUnits\tIndex\tMinimum\tMaximum\tDefault\t" +
            "Required\tSearchHelpID\tUnique\t" +
            "ModTimeStamp\tForeignKeyName\tForeignField\tKeyQuery\t" +
            "KeySelect\tInKeyIndex\t" +
            "</COLUMNS>\n" +

            "<DATA>\t" +
            "ElementarySchool\tE_SCHOOL\tElementarySchool\tElementary School\t" +
            "E_SCHOOL\tElemSchool\t4\tInt\t0\t1\tLookup\tLeft\t0\tEM1,EM2\t" +
            "E_SCHOOL\t1\tFeet\t1\t3\t4\t5\t6\t\t0\t" +
            "0\t\t\t0\t0\t0\t" +
            "</DATA>\n" +

            "<DATA>\t" +
            "ListAgentAgentID\tAGENT_ID\tListAgentAgentID\tListing Agent ID\tAGENT_ID\t" +
            "AgentID\t0\tCharacter\t0\t1\t\tLeft\t0\t\t\t0\t\t0\t0\t0\t5" +
            "\t0\t\t0\t" +
            "0\t\t\t0\t0\t0\t" +
            "</DATA>\n" +

            "</METADATA-TABLE>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return getExpectedCompact();
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-TABLE Resource=\"Property\" Class=\"MOB\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">" + EOL +
            "<Field>" + EOL +
            "<MetadataEntryID>ElementarySchool</MetadataEntryID>" + EOL +
            "<SystemName>E_SCHOOL</SystemName>" + EOL +
            "<StandardName>ElementarySchool</StandardName>" + EOL +
            "<LongName>Elementary School</LongName>" + EOL +
            "<DBName>E_SCHOOL</DBName>" + EOL +
            "<ShortName>ElemSchool</ShortName>" + EOL +
            "<MaximumLength>4</MaximumLength>" + EOL +
            // Line 10
            "<DataType>Int</DataType>" + EOL +
            "<Precision>0</Precision>" + EOL +
            "<Searchable>1</Searchable>" + EOL +
            "<Interpretation>Lookup</Interpretation>" + EOL +
            "<Alignment>Left</Alignment>" + EOL +
            "<UseSeparator>0</UseSeparator>" + EOL +
            "<EditMaskID>EM1,EM2</EditMaskID>" + EOL +
            "<LookupName>E_SCHOOL</LookupName>" + EOL +
            "<MaxSelect>1</MaxSelect>" + EOL +
            "<Units>Feet</Units>" + EOL +
            // Line 20
            "<Index>1</Index>" + EOL +
            "<Minimum>3</Minimum>" + EOL +
            "<Maximum>4</Maximum>" + EOL +
            "<Default>5</Default>" + EOL +
            "<Required>6</Required>" + EOL +
            "<SearchHelpID></SearchHelpID>" + EOL +
            "<Unique>0</Unique>" + EOL +
            "<ModTimeStamp>0</ModTimeStamp>" + EOL +
            "<ForeignKeyName></ForeignKeyName>" + EOL +
            "<ForeignField></ForeignField>" + EOL +
            "<KeyQuery>0</KeyQuery>" + EOL +
            "<KeySelect>0</KeySelect>" + EOL +
            "<InKeyIndex>0</InKeyIndex>" + EOL +
            "</Field>" + EOL +
            
            "<Field>" + EOL +
            "<MetadataEntryID>ListAgentAgentID</MetadataEntryID>" + EOL +
            "<SystemName>AGENT_ID</SystemName>" + EOL +
            "<StandardName>ListAgentAgentID</StandardName>" + EOL +
            //Line 30
            "<LongName>Listing Agent ID</LongName>" + EOL +
            "<DBName>AGENT_ID</DBName>" + EOL +
            "<ShortName>AgentID</ShortName>" + EOL +
            "<MaximumLength>0</MaximumLength>" + EOL +
            "<DataType>Character</DataType>" + EOL +
            "<Precision>0</Precision>" + EOL +
            "<Searchable>1</Searchable>" + EOL +
            "<Interpretation></Interpretation>" + EOL +
            "<Alignment>Left</Alignment>" + EOL +
            "<UseSeparator>0</UseSeparator>" + EOL +
            // Line 40
            "<EditMaskID></EditMaskID>" + EOL +
            "<LookupName></LookupName>" + EOL +
            "<MaxSelect>0</MaxSelect>" + EOL +
            "<Units></Units>" + EOL +
            "<Index>0</Index>" + EOL +
            "<Minimum>0</Minimum>" + EOL +
            "<Maximum>0</Maximum>" + EOL +
            "<Default>5</Default>" + EOL +
            "<Required>0</Required>" + EOL +
            "<SearchHelpID></SearchHelpID>" + EOL +
            // Line 50
            "<Unique>0</Unique>" + EOL +
            "<ModTimeStamp>0</ModTimeStamp>" + EOL +
            "<ForeignKeyName></ForeignKeyName>" + EOL +
            "<ForeignField></ForeignField>" + EOL +
            "<KeyQuery>0</KeyQuery>" + EOL +
            "<KeySelect>0</KeySelect>" + EOL +
            "<InKeyIndex>0</InKeyIndex>" + EOL +
            "</Field>" + EOL +
            "</METADATA-TABLE>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return getExpectedStandard();
    }

    public void testCompactFormatIsEmptyIfAllTablesFiltered()
    {
        List<MTable> data = new ArrayList<MTable>();
        data.add(mListingPrice);
        String formatted = format(getCompactFormatter(), data, 
                                  getLevels(), FormatterContext.NOT_RECURSIVE);
        assertLinesEqual("", formatted);
    }

    private MTable mSchool;
    private MTable mAgent;
    private MTable mListingPrice;
    private Group mNewspapers;
    private Set<Group> mGroups;
    private TableGroupFilter mGroupFilter;
}
