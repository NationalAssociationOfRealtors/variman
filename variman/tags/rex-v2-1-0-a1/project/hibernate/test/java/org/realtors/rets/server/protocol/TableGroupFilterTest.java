package org.realtors.rets.server.protocol;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

import junit.framework.TestCase;
import org.realtors.rets.server.Group;
import org.realtors.rets.server.config.RuleDescription;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.ObjectMother;
import org.realtors.rets.server.metadata.Table;

public class TableGroupFilterTest extends TestCase
{
    protected void setUp() throws Exception
    {
        mGroupFilter = new TableGroupFilter();
        MClass aClass = ObjectMother.createClass();
        mListingNumber = new Table("ListingNumber");
        mListingPrice = new Table("ListingPrice");
        mAssociationFee = new Table("AssociationFee");
        mListingDate = new Table("ListingDate");
        aClass.addTable(mListingNumber);
        aClass.addTable(mListingPrice);
        aClass.addTable(mAssociationFee);
        aClass.addTable(mListingDate);
        mGroupFilter.setTables("Property", "RES", aClass.getTables());

        mNewspaper = new Group("mNewspaper");
        mAgent = new Group("mAgent");
        mBroker = new Group("mBroker");

        RuleDescription ruleDescription = new RuleDescription(
            RuleDescription.INCLUDE);
        ruleDescription.setResource("Property");
        ruleDescription.setRetsClass("RES");
        ruleDescription.addSystemName("ListingNumber");
        ruleDescription.addSystemName("ListingPrice");
        GroupRules rules = new GroupRules(mNewspaper.getName());
        rules.addRule(ruleDescription);
        mGroupFilter.addRules(rules);

        ruleDescription = new RuleDescription(RuleDescription.EXCLUDE);
        ruleDescription.setResource("Property");
        ruleDescription.setRetsClass("RES");
        ruleDescription.addSystemName("ListingPrice");
        ruleDescription.addSystemName("AssociationFee");
        rules = new GroupRules(mAgent.getName());
        rules.addRule(ruleDescription);
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

    private TableGroupFilter mGroupFilter;
    private Table mListingNumber;
    private Table mListingPrice;
    private Table mAssociationFee;
    private Table mListingDate;
    private Group mNewspaper;
    private Group mAgent;
    private Group mBroker;
}
