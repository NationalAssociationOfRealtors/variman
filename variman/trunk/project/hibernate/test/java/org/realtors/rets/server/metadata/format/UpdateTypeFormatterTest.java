/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.UpdateHelp;
import org.realtors.rets.server.metadata.UpdateType;
import org.realtors.rets.server.metadata.UpdateTypeAttributeEnum;
import org.realtors.rets.server.metadata.ValidationExpression;
import org.realtors.rets.server.metadata.ValidationExternal;
import org.realtors.rets.server.metadata.ValidationLookup;
import org.realtors.rets.server.protocol.TableGroupFilter;
import org.realtors.rets.server.Group;
import org.realtors.rets.server.config.RuleDescription;
import org.realtors.rets.server.config.GroupRules;

public class UpdateTypeFormatterTest extends FormatterTestCase
{
    public UpdateTypeFormatterTest()
    {
        HashSet allTables = new HashSet();

        Table table = new Table(1);
        table.setSystemName("STATUS");
        allTables.add(table);

        mStatusType = new UpdateType(1);
        mStatusType.setTable(table);
        mStatusType.setSequence(12);

        Set attributes = new HashSet();
        attributes.add(UpdateTypeAttributeEnum.INTERACTIVEVALIDATE);
        attributes.add(UpdateTypeAttributeEnum.REQUIRED);
        mStatusType.setAttributes(attributes);

        mStatusType.setDefault("0");

        Set validationExpressions = new HashSet();
        ValidationExpression ve = new ValidationExpression(1);
        ve.setValidationExpressionID("VE1");
        validationExpressions.add(ve);
        ve = new ValidationExpression(2);
        ve.setValidationExpressionID("VE2");
        validationExpressions.add(ve);
        mStatusType.setValidationExpressions(validationExpressions);

        UpdateHelp updateHelp = new UpdateHelp(1);
        updateHelp.setUpdateHelpID("UH1");
        mStatusType.setUpdateHelp(updateHelp);

        ValidationLookup validationLookup = new ValidationLookup(1);
        validationLookup.setValidationLookupName("VL_NAME");
        mStatusType.setValidationLookup(validationLookup);

        ValidationExternal validationExternal = new ValidationExternal(1);
        validationExternal.setValidationExternalName("VE_NAME");
        mStatusType.setValidationExternal(validationExternal);

        table = new Table(2);
        table.setSystemName("LIST_PRICE");
        allTables.add(table);
        mListPriceType = new UpdateType(2);
        mListPriceType.setTable(table);
        mListPriceType.setSequence(13);
        mListPriceType.setAttributes(attributes);
        mListPriceType.setDefault("0");
        mListPriceType.setValidationExpressions(validationExpressions);
        mListPriceType.setUpdateHelp(updateHelp);
        mListPriceType.setValidationLookup(validationLookup);
        mListPriceType.setValidationExternal(validationExternal);

        mGroupFilter = new TableGroupFilter();
        mGroupFilter.setTables("Property", "RES", allTables);

        mNewspapers = new Group("Newspapers");
        mGroups = new HashSet();
        mGroups.add(mNewspapers);

        RuleDescription ruleDescription = new RuleDescription(
            RuleDescription.EXCLUDE);
        ruleDescription.setResource("Property");
        ruleDescription.setRetsClass("RES");
        ruleDescription.addSystemName("LIST_PRICE");
        GroupRules rules = new GroupRules(mNewspapers.getName());
        rules.addRule(ruleDescription);
        mGroupFilter.addRules(rules);
    }

    protected List getData()
    {
        List updateTypes = new ArrayList();
        updateTypes.add(mStatusType);
        updateTypes.add(mListPriceType);
        return updateTypes;
    }

    protected TableGroupFilter getGroupFilter()
    {
        return mGroupFilter;
    }

    protected Set getGroups()
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

            "<COLUMNS>\tSystemName\tSequence\tAttributes\tDefault\t" +
            "ValidationExpressionID\tUpdateHelpID\tValidationLookupName\t" +
            "ValidationExternalName\t</COLUMNS>\n" +

            "<DATA>\tSTATUS\t12\t2,4\t0\tVE1,VE2\tUH1\tVL_NAME\tVE_NAME\t" +
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
            "<SystemName>STATUS</SystemName>" + EOL +
            "<Sequence>12</Sequence>" + EOL +
            "<Attributes>2,4</Attributes>" + EOL +
            "<Default>0</Default>" + EOL +
            "<ValidationExpressionID>VE1,VE2</ValidationExpressionID>" + EOL +
            "<UpdateHelpID>UH1</UpdateHelpID>" + EOL +
            "<ValidationLookupName>VL_NAME</ValidationLookupName>" + EOL +
            "<ValidationExternalName>VE_NAME</ValidationExternalName>" + EOL +
            "</UpdateField>" + EOL +
            "</METADATA-UPDATE_TYPE>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return getExpectedStandard();
    }

    public void testCompactFormatIsEmptyIfAllTablesFiltered()
    {
        ArrayList data = new ArrayList();
        data.add(mListPriceType);
        String formatted = format(getCompactFormatter(), data,
                                  getLevels(), FormatterContext.NOT_RECURSIVE);
        assertLinesEqual("", formatted);
    }

    private UpdateType mStatusType;
    private UpdateType mListPriceType;
    private TableGroupFilter mGroupFilter;
    private Group mNewspapers;
    private HashSet mGroups;
}
