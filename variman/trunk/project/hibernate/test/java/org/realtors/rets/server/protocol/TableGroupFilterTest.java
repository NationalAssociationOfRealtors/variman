package org.realtors.rets.server.protocol;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.server.Group;
import org.realtors.rets.server.config.FilterRule;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.metadata.ObjectMother;

/**
 * Unit tests for the TableGroupFilter class
 *
 * TODO: Add test for multiple groups with exclude rules
 * TODO: Add tests for SQL condition rules.
 */
@RunWith(JUnit4.class)
public class TableGroupFilterTest
{
    private static final String RESOURCE_NAME = "Property";
    private static final String CLASS_NAME = "RES";
    /**
     * Setup objects that are common to all tests.
     *
     * @throws Exception
     */
    @org.junit.Before
    public void setUp()
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
        mGroupFilter.setTables(RESOURCE_NAME, CLASS_NAME, tables);

        mIncludeGroup = new Group("mIncludeGroup");
        mIncludeGroup2 = new Group("mIncludeGroup2");
        mExcludeGroup = new Group("mExcludeGroup");
        mExcludeGroup2 = new Group("mExcludeGroup2");
        mNoFilterGroup = new Group("mNoFilterGroup");

        FilterRule filterRule = new FilterRule(
            FilterRule.INCLUDE);
        filterRule.setResource(RESOURCE_NAME);
        filterRule.setRetsClass(CLASS_NAME);
        filterRule.addSystemName("ListingNumber");
        filterRule.addSystemName("ListingPrice");
        GroupRules rules = new GroupRules(mIncludeGroup.getName());
        rules.addFilterRule(filterRule);
        mGroupFilter.addRules(rules);

        filterRule = new FilterRule(FilterRule.INCLUDE);
        filterRule.setResource(RESOURCE_NAME);
        filterRule.setRetsClass(CLASS_NAME);
        filterRule.addSystemName("ListingDate");
        rules = new GroupRules(mIncludeGroup2.getName());
        rules.addFilterRule(filterRule);
        mGroupFilter.addRules(rules);

        filterRule = new FilterRule(FilterRule.EXCLUDE);
        filterRule.setResource(RESOURCE_NAME);
        filterRule.setRetsClass(CLASS_NAME);
        filterRule.addSystemName("ListingPrice");
        filterRule.addSystemName("AssociationFee");
        rules = new GroupRules(mExcludeGroup.getName());
        rules.addFilterRule(filterRule);
        mGroupFilter.addRules(rules);

        filterRule = new FilterRule(FilterRule.EXCLUDE);
        filterRule.setResource(RESOURCE_NAME);
        filterRule.setRetsClass(CLASS_NAME);
        filterRule.addSystemName(mListingDate.getSystemName());
        rules = new GroupRules(mExcludeGroup2.getName());
        rules.addFilterRule(filterRule);
        mGroupFilter.addRules(rules);
    }

    /**
     * Test that a group with no filter rules correctly returns all
     * entries for the resource/class. 
     */
    @Test
    public void noMatchingRules()
    {
        Set expectedTables = new HashSet();
        expectedTables.add(mListingPrice);
        expectedTables.add(mListingNumber);
        expectedTables.add(mListingDate);
        expectedTables.add(mAssociationFee);

        assertEquals(expectedTables,
                     mGroupFilter.findTables(mNoFilterGroup, RESOURCE_NAME, CLASS_NAME));
    }

    /**
     * Test a group which has a single EXCLUDE rule.
     */
    @Test
    public void singleMatchingExclude()
    {
        Set expectedTables = new HashSet();
        expectedTables.add(mListingNumber);
        expectedTables.add(mListingDate);
        assertEquals(expectedTables,
                     mGroupFilter.findTables(mExcludeGroup, RESOURCE_NAME, CLASS_NAME));
    }

    /**
     * Test two groups, both with EXCLUDE rules.
     */
    @Test
    public void multipleMatchingExcludes()
    {
        Set groups = new HashSet();
        Set expectedTables = new HashSet();

        groups.add(mExcludeGroup);
        groups.add(mExcludeGroup2);

        expectedTables.add(mListingNumber);

        assertEquals(expectedTables,
                     mGroupFilter.findTables(groups, RESOURCE_NAME, CLASS_NAME));
    }

    /**
     * Test a group which has a single INCLUDE rule.
     */
    @Test
    public void singleMatchingInclude()
    {
        Set expectedTables = new HashSet();
        expectedTables.add(mListingPrice);
        expectedTables.add(mListingNumber);
        assertEquals(expectedTables,
                     mGroupFilter.findTables(mIncludeGroup, RESOURCE_NAME, CLASS_NAME));
    }

    /**
     * Test multiple groups with INCLUDE rules.
     */
    @Test
    public void multipleMatchingIncludes() {
        Set<MTable> expectedTables = new HashSet<MTable>();
        Set<Group> groups = new HashSet<Group>();

        groups.add(mIncludeGroup);
        groups.add(mIncludeGroup2);

        expectedTables.add(mListingPrice);
        expectedTables.add(mListingNumber);
        expectedTables.add(mListingDate);

        assertEquals(expectedTables,
                     mGroupFilter.findTables(groups, RESOURCE_NAME, CLASS_NAME));
    }

    /**
     * Tests multiple groups when one has an INCLUDE rule and the other
     * has an EXCLUDE rule containing included fields.
     */
    @Test
    public void multipleGroupsWithOverlappingIncludeAndExclude()
    {
        Set groups = new HashSet();
        groups.add(mIncludeGroup);
        groups.add(mExcludeGroup);
        Set expectedTables = new HashSet();
        expectedTables.add(mListingNumber);
        assertEquals(expectedTables,
                     mGroupFilter.findTables(groups, RESOURCE_NAME, CLASS_NAME));
    }

    /**
     * Tests tables filters for empty group list.
     */
    @Test
    public void noGroups()
    {
        Set groups = Collections.EMPTY_SET;
        Set expectedTables = new HashSet();
        expectedTables.add(mListingNumber);
        expectedTables.add(mListingDate);
        expectedTables.add(mListingPrice);
        expectedTables.add(mAssociationFee);
        assertEquals(expectedTables,
                     mGroupFilter.findTables(groups, RESOURCE_NAME, CLASS_NAME));
    }

    /**
     * Test that adding a filter with an unknown resource does not
     * throw an exception.
     */
    @Test
    public void addFilterWithUnknownResourceDoesNotThrow()
    {
        FilterRule filterRule = new FilterRule(
            FilterRule.INCLUDE);
        filterRule.setResource("UNKNOWN");
        filterRule.setRetsClass(CLASS_NAME);
        filterRule.addSystemName("ListingNumber");
        filterRule.addSystemName("ListingPrice");
        GroupRules rules = new GroupRules(mIncludeGroup.getName());
        rules.addFilterRule(filterRule);
        // Make sure no exception is thrown
        try {
            mGroupFilter.addRules(rules);
        } catch (Exception ex) {
            fail("Adding filter with unknown resource threw exception: " + ex.getMessage());
        }
    }

    /**
     * Test that adding a filter with an unknown class does not
     * throw an exception.
     */
    @Test
    public void addFilterWithUnknownClassDoesNotThrow()
    {
        FilterRule filterRule = new FilterRule(
            FilterRule.INCLUDE);
        filterRule.setResource(RESOURCE_NAME);
        filterRule.setRetsClass("UNKNOWN");
        filterRule.addSystemName("ListingNumber");
        filterRule.addSystemName("ListingPrice");
        GroupRules rules = new GroupRules(mIncludeGroup.getName());
        rules.addFilterRule(filterRule);

        try {
            mGroupFilter.addRules(rules);
        } catch (Exception ex) {
            fail("Adding filter with unknown class threw exception: " + ex.getMessage());
        }
    }

    /**
     * Test that adding a filter rule for an unknown group does not
     * throw an exception.
     */
    @Test
    public void addUnknownGroupDoesNotThrow()
    {
        FilterRule filterRule = new FilterRule(
            FilterRule.INCLUDE);
        filterRule.setResource(RESOURCE_NAME);
        filterRule.setRetsClass(CLASS_NAME);
        filterRule.addSystemName("ListingNumber");
        filterRule.addSystemName("ListingPrice");
        GroupRules rules = new GroupRules("UNKNOWN");
        rules.addFilterRule(filterRule);

        try {
            mGroupFilter.addRules(rules);
        } catch (Exception ex) {
            fail("Adding filter with unknown group threw exception: " + ex.getMessage());
        }
    }

    private TableGroupFilter mGroupFilter;
    private MTable mListingNumber;
    private MTable mListingPrice;
    private MTable mAssociationFee;
    private MTable mListingDate;
    private Group mIncludeGroup;
    private Group mIncludeGroup2;
    private Group mExcludeGroup;
    private Group mExcludeGroup2;
    private Group mNoFilterGroup;
}
