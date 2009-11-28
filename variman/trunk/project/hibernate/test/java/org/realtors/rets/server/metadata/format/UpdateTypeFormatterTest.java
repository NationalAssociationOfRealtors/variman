/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.common.metadata.types.MUpdateHelp;
import org.realtors.rets.common.metadata.types.MUpdateType;
import org.realtors.rets.common.metadata.types.MValidationExpression;
import org.realtors.rets.common.metadata.types.MValidationExternal;
import org.realtors.rets.common.metadata.types.MValidationLookup;
import org.realtors.rets.server.metadata.UpdateTypeAttributeEnum;
import org.realtors.rets.server.protocol.TableGroupFilter;
import org.realtors.rets.server.Group;
import org.realtors.rets.server.config.FilterRule;
import org.realtors.rets.server.config.GroupRules;

public class UpdateTypeFormatterTest extends FormatterTestCase
{
    public UpdateTypeFormatterTest()
    {
        Set<MTable> allTables = new LinkedHashSet<MTable>();

        MTable table = new MTable();
        table.setUniqueId(Long.valueOf(1));
        table.setSystemName("STATUS");
        allTables.add(table);

        mStatusType = new MUpdateType();
        mStatusType.setUniqueId(Long.valueOf(1));
        mStatusType.setMetadataEntryID("Status");
        mStatusType.setMTable(table);
        mStatusType.setSequence(12);

        List<String> attributes = new ArrayList<String>();
        attributes.add(UpdateTypeAttributeEnum.REQUIRED.toString());
        attributes.add(UpdateTypeAttributeEnum.INTERACTIVEVALIDATE.toString());
        String attributesStr = StringUtils.join(attributes, ",");
        mStatusType.setAttributes(attributesStr);

        mStatusType.setDefault("0");

        Set<MValidationExpression> validationExpressions = new LinkedHashSet<MValidationExpression>();
        MValidationExpression ve = new MValidationExpression();
        ve.setUniqueId(Long.valueOf(1));
        ve.setValidationExpressionID("VE1");
        validationExpressions.add(ve);
        ve = new MValidationExpression();
        ve.setUniqueId(Long.valueOf(2));
        ve.setValidationExpressionID("VE2");
        validationExpressions.add(ve);
        mStatusType.setMValidationExpressions(validationExpressions);

        MUpdateHelp updateHelp = new MUpdateHelp();
        updateHelp.setUniqueId(Long.valueOf(1));
        updateHelp.setUpdateHelpID("UH1");
        mStatusType.setMUpdateHelp(updateHelp);

        MValidationLookup validationLookup = new MValidationLookup();
        validationLookup.setUniqueId(Long.valueOf(1));
        validationLookup.setValidationLookupName("VL_NAME");
        mStatusType.setMValidationLookup(validationLookup);

        MValidationExternal validationExternal = new MValidationExternal();
        validationExternal.setUniqueId(Long.valueOf(1));
        validationExternal.setValidationExternalName("VE_NAME");
        mStatusType.setMValidationExternal(validationExternal);
        mStatusType.setMaxUpdate(1);

        table = new MTable();
        table.setUniqueId(Long.valueOf(2));
        table.setSystemName("LIST_PRICE");
        allTables.add(table);
        
        mListPriceType = new MUpdateType();
        mListPriceType.setUniqueId(Long.valueOf(2));
        mListPriceType.setMTable(table);
        mListPriceType.setSequence(13);
        mListPriceType.setAttributes(attributesStr);
        mListPriceType.setDefault("0");
        mListPriceType.setMValidationExpressions(validationExpressions);
        mListPriceType.setMUpdateHelp(updateHelp);
        mListPriceType.setMValidationLookup(validationLookup);
        mListPriceType.setMValidationExternal(validationExternal);
        mListPriceType.setMaxUpdate(0);
        
        mGroupFilter = new TableGroupFilter();
        mGroupFilter.setTables("Property", "RES", allTables);

        mNewspapers = new Group("Newspapers");
        mGroups = new LinkedHashSet<Group>();
        mGroups.add(mNewspapers);

        FilterRule filterRule = new FilterRule(FilterRule.EXCLUDE);
        filterRule.setResource("Property");
        filterRule.setRetsClass("RES");
        filterRule.addSystemName("LIST_PRICE");
        GroupRules rules = new GroupRules(mNewspapers.getName());
        rules.addFilterRule(filterRule);
        mGroupFilter.addRules(rules);
    }

    protected List<MUpdateType> getData()
    {
        List<MUpdateType> updateTypes = new ArrayList<MUpdateType>();
        updateTypes.add(mStatusType);
        updateTypes.add(mListPriceType);
        return updateTypes;
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
        return new String[] {"Property", "RES", "Change"};
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactUpdateTypeFormatter();
    }

    protected MetadataFormatter getStandardFormatter()
    {
        return new StandardUpdateTypeFormatter();
    }

    protected String getExpectedCompact()
    {
        return
            "<METADATA-UPDATE_TYPE Resource=\"Property\" Class=\"RES\" " +
            "Update=\"Change\" Version=\"" + VERSION + "\" Date=\"" + DATE +
            "\">\n" +

            "<COLUMNS>\tMetadataEntryID\tSystemName\tSequence\tAttributes\tDefault\t" +
            "ValidationExpressionID\tUpdateHelpID\tValidationLookupName\t" +
            "ValidationExternalName\tMaxUpdate\t</COLUMNS>\n" +

            "<DATA>\tStatus\tSTATUS\t12\t2,4\t0\tVE1,VE2\tUH1\tVL_NAME\tVE_NAME\t1\t" +
            "</DATA>\n" +

            "</METADATA-UPDATE_TYPE>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return getExpectedCompact();
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-UPDATE_TYPE Resource=\"Property\" Class=\"RES\" " +
            "Update=\"Change\" Version=\"" + VERSION + "\" Date=\"" + DATE +
            "\">" + EOL +
            "<UpdateField>" + EOL +
            "<MetadataEntryID>Status</MetadataEntryID>" + EOL +
            "<SystemName>STATUS</SystemName>" + EOL +
            "<Sequence>12</Sequence>" + EOL +
            "<Attributes>2,4</Attributes>" + EOL +
            "<Default>0</Default>" + EOL +
            "<ValidationExpressionID>VE1,VE2</ValidationExpressionID>" + EOL +
            "<UpdateHelpID>UH1</UpdateHelpID>" + EOL +
            "<ValidationLookupName>VL_NAME</ValidationLookupName>" + EOL +
            "<ValidationExternalName>VE_NAME</ValidationExternalName>" + EOL +
            "<MaxUpdate>1</MaxUpdate>" + EOL +
            "</UpdateField>" + EOL +
            "</METADATA-UPDATE_TYPE>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return getExpectedStandard();
    }

    public void testCompactFormatIsEmptyIfAllTablesFiltered()
    {
        List<MUpdateType> data = new ArrayList<MUpdateType>();
        data.add(mListPriceType);
        String formatted = format(getCompactFormatter(), data,
                                  getLevels(), FormatterContext.NOT_RECURSIVE);
        assertLinesEqual("", formatted);
    }

    private MUpdateType mStatusType;
    private MUpdateType mListPriceType;
    private TableGroupFilter mGroupFilter;
    private Group mNewspapers;
    private Set<Group> mGroups;
}
