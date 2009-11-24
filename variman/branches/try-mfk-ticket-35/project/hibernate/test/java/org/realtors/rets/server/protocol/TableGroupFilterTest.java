package org.realtors.rets.server.protocol;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

import junit.framework.TestCase;

import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.server.Group;
import org.realtors.rets.server.config.FilterRule;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.metadata.ObjectMother;

public class TableGroupFilterTest extends TestCase
{
    protected void setUp() throws Exception
    {
        mGroupFilter = new TableGroupFilter();
        MClass aClass = ObjectMother.createClass();
        mListingNumber = new MTable();
        mListingNumber.setSystemName("ListingNumber");
        mListingPrice = new MTable();
        mListingPrice.setSystemName("ListingPrice");
        mAssociationFee = new MTable();
        mAssociationFee.setSystemName("AssociationFee");
        mListingDate = new MTable();
        mListingDate.setSystemName("ListingDate");
        aClass.addChild(MetadataType.TABLE, mListingNumber);
        aClass.addChild(MetadataType.TABLE, mListingPrice);
        aClass.addChild(MetadataType.TABLE, mAssociationFee);
        aClass.addChild(MetadataType.TABLE, mListingDate);
        Set<MTable> tables = (Set<MTable>)aClass.getChildrenSet(MetadataType.TABLE);
        mGroupFilter.setTables("Property", "RES", tables);

        mNewspaper = new Group("mNewspaper");
        mAgent = new Group("mAgent");
        mBroker = new Group("mBroker");

        FilterRule filterRule = new FilterRule(
            FilterRule.INCLUDE);
        filterRule.setResource("Property");
        filterRule.setRetsClass("RES");
        filterRule.addSystemName("ListingNumber");
        filterRule.addSystemName("ListingPrice");
        GroupRules rules = new GroupRules(mNewspaper.getName());
        rules.addFilterRule(filterRule);
        mGroupFilter.addRules(rules);

        filterRule = new FilterRule(FilterRule.EXCLUDE);
        filterRule.setResource("Property");
        filterRule.setRetsClass("RES");
        filterRule.addSystemName("ListingPrice");
        filterRule.addSystemName("AssociationFee");
        rules = new GroupRules(mAgent.getName());
        rules.addFilterRule(filterRule);
        mGroupFilter.addRules(rules);
    }

    public void testNoMatchingRule()
    {
        Set expectedTables = new HashSet();
        expectedTables.add(mListingPrice);
        expectedTables.add(mListingNumber);
        expectedTables.add(mListingDate);
        expectedTables.add(mAssociationFee);
        assertEquals(expectedTables,
                     mGroupFilter.findTables(mBroker, "Property", "RES"));
    }

    public void testSimpleExclude()
    {
        Set expectedTables = new HashSet();
        expectedTables.add(mListingNumber);
        expectedTables.add(mListingDate);
        assertEquals(expectedTables,
                     mGroupFilter.findTables(mAgent, "Property", "RES"));
    }

    public void testSimpleInclude()
    {
        Set expectedTables = new HashSet();
        expectedTables.add(mListingPrice);
        expectedTables.add(mListingNumber);
        assertEquals(expectedTables,
                     mGroupFilter.findTables(mNewspaper, "Property", "RES"));
    }

    public void testMultipleGroups()
    {
        Set groups = new HashSet();
        groups.add(mNewspaper);
        groups.add(mAgent);
        Set expectedTables = new HashSet();
        expectedTables.add(mListingNumber);
        expectedTables.add(mListingDate);
        expectedTables.add(mListingPrice);
        assertEquals(expectedTables,
                     mGroupFilter.findTables(groups, "Property", "RES"));
    }

    public void testNoGroups()
    {
        Set groups = Collections.EMPTY_SET;
        Set expectedTables = new HashSet();
        expectedTables.add(mListingNumber);
        expectedTables.add(mListingDate);
        expectedTables.add(mListingPrice);
        expectedTables.add(mAssociationFee);
        assertEquals(expectedTables,
                     mGroupFilter.findTables(groups, "Property", "RES"));
    }

    public void testAddUnknownResource()
    {
        FilterRule filterRule = new FilterRule(
            FilterRule.INCLUDE);
        filterRule.setResource("UNKNOWN");
        filterRule.setRetsClass("RES");
        filterRule.addSystemName("ListingNumber");
        filterRule.addSystemName("ListingPrice");
        GroupRules rules = new GroupRules(mNewspaper.getName());
        rules.addFilterRule(filterRule);
        // Make sure no exception is thrown
        mGroupFilter.addRules(rules);
    }

    public void testAddUnknownClass()
    {
        FilterRule filterRule = new FilterRule(
            FilterRule.INCLUDE);
        filterRule.setResource("Property");
        filterRule.setRetsClass("UNKNOWN");
        filterRule.addSystemName("ListingNumber");
        filterRule.addSystemName("ListingPrice");
        GroupRules rules = new GroupRules(mNewspaper.getName());
        rules.addFilterRule(filterRule);
        // Make sure no exception is thrown
        mGroupFilter.addRules(rules);
    }

    public void testAddUnknownGroup()
    {
        FilterRule filterRule = new FilterRule(
            FilterRule.INCLUDE);
        filterRule.setResource("Property");
        filterRule.setRetsClass("RES");
        filterRule.addSystemName("ListingNumber");
        filterRule.addSystemName("ListingPrice");
        GroupRules rules = new GroupRules("UNKNOWN");
        rules.addFilterRule(filterRule);
        // Make sure no exception is thrown
        mGroupFilter.addRules(rules);
    }

    private TableGroupFilter mGroupFilter;
    private MTable mListingNumber;
    private MTable mListingPrice;
    private MTable mAssociationFee;
    private MTable mListingDate;
    private Group mNewspaper;
    private Group mAgent;
    private Group mBroker;
}
