package org.realtors.rets.server.protocol;

import java.util.Set;
import java.util.TreeSet;
import java.util.Collections;

import junit.framework.TestCase;
import org.realtors.rets.server.Group;
import org.realtors.rets.server.config.ConditionRule;
import org.realtors.rets.server.config.ConditionRuleImpl;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.config.GroupRulesImpl;

public class ConditionRuleSetTest extends TestCase
{
    protected void setUp()
    {
        mConditionRuleSet = new ConditionRuleSet();
        mNewspaper1 = new Group("Newspaper 1");
        mNewspaper2 = new Group("Newspaper 2");
        mNewspaper3 = new Group("Newspaper 3");
        mNewspaper4 = new Group("Newspaper 4");
        mNewspaper5 = new Group("Newspaper 5");
        mNewspaper6 = new Group("Newspaper 6");
        mAgents1 = new Group("Agents 1");
        mAgents2 = new Group("Agents 2");

        ConditionRule rule = new ConditionRuleImpl();
        rule.setResourceID("Property");
        rule.setRetsClassName("RES");
        rule.setSqlConstraint("price < 500000");
        GroupRules rules = new GroupRulesImpl(mNewspaper1);
        rules.addConditionRule(rule);
        mConditionRuleSet.addRules(rules);

        rule = new ConditionRuleImpl();
        rule.setResourceID("Property");
        rule.setRetsClassName("MOB");
        rule.setSqlConstraint("price > 250000");
        rules = new GroupRulesImpl(mNewspaper2);
        rules.addConditionRule(rule);
        mConditionRuleSet.addRules(rules);

        rule = new ConditionRuleImpl();
        rule.setResourceID("Property");
        rule.setRetsClassName("RES");
        rule.setSqlConstraint("color = 'Red'");
        rules = new GroupRulesImpl(mNewspaper3);
        rules.addConditionRule(rule);
        mConditionRuleSet.addRules(rules);

        rule = new ConditionRuleImpl();
        rule.setResourceID("Property");
        rule.setRetsClassName("RES");
        rule.setSqlConstraint("idx_viewable = 'Y'");
        rules = new GroupRulesImpl(mAgents1);
        rules.addConditionRule(rule);
        mConditionRuleSet.addRules(rules);
        
        rule = new ConditionRuleImpl();
        rule.setResourceID("Property");
        rule.setRetsClassName("RES");
        rule.setDmqlConstraint("price=500000-");
        rules = new GroupRulesImpl(mNewspaper4);
        rules.addConditionRule(rule);
        mConditionRuleSet.addRules(rules);

        rule = new ConditionRuleImpl();
        rule.setResourceID("Property");
        rule.setRetsClassName("MOB");
        rule.setDmqlConstraint("price=250000-");
        rules = new GroupRulesImpl(mNewspaper5);
        rules.addConditionRule(rule);
        mConditionRuleSet.addRules(rules);

        rule = new ConditionRuleImpl();
        rule.setResourceID("Property");
        rule.setRetsClassName("RES");
        rule.setDmqlConstraint("color=\"Red\"");
        rules = new GroupRulesImpl(mNewspaper6);
        rules.addConditionRule(rule);
        mConditionRuleSet.addRules(rules);

        rule = new ConditionRuleImpl();
        rule.setResourceID("Property");
        rule.setRetsClassName("RES");
        rule.setDmqlConstraint("idx_viewable=\"Y\"");
        rules = new GroupRulesImpl(mAgents2);
        rules.addConditionRule(rule);
        mConditionRuleSet.addRules(rules);
    }

    public void testNoGroupsWithSqlConstraints()
    {
        Set groups = new TreeSet();
        groups.add(mNewspaper1);
        groups.add(mAgents1);
        String sqlConstraint =
            mConditionRuleSet.findSqlConstraint(groups, "Property", "MOB");
        assertEquals("", sqlConstraint);
    }

    public void testOneGroupWithSqlConstraints()
    {
        Set groups = new TreeSet();
        groups.add(mNewspaper1);
        groups.add(mNewspaper2);
        String sqlConstraint =
            mConditionRuleSet.findSqlConstraint(groups, "Property", "RES");
        assertEquals("(price < 500000)", sqlConstraint);
    }

    public void testTwoGroupsWithSqlConstraints()
    {
        Set groups = new TreeSet();
        groups.add(mNewspaper1);
        groups.add(mNewspaper2);
        groups.add(mAgents1);
        String sqlConstraint =
            mConditionRuleSet.findSqlConstraint(groups, "Property", "RES");
        assertEquals("((idx_viewable = 'Y') AND (price < 500000))",
                     sqlConstraint);
    }

    public void testThreeGroupsWithSqlConstraints()
    {
        Set groups = new TreeSet();
        groups.add(mNewspaper1);
        groups.add(mNewspaper2);
        groups.add(mAgents1);
        groups.add(mNewspaper3);
        String sqlConstraint =
            mConditionRuleSet.findSqlConstraint(groups, "Property", "RES");
        assertEquals("((idx_viewable = 'Y') AND (price < 500000) AND " +
                     "(color = 'Red'))",
                     sqlConstraint);
    }

    public void testNoGroupsWithDmqlConstraints()
    {
        Set groups = new TreeSet();
        groups.add(mNewspaper4);
        groups.add(mAgents2);
        String dmqlConstraint =
            mConditionRuleSet.findDmqlConstraint(groups, "Property", "MOB");
        assertEquals("", dmqlConstraint);
    }

    public void testOneGroupWithDmqlConstraints()
    {
        Set groups = new TreeSet();
        groups.add(mNewspaper4);
        groups.add(mNewspaper5);
        String dmqlConstraint =
            mConditionRuleSet.findDmqlConstraint(groups, "Property", "RES");
        assertEquals("(price=500000-)", dmqlConstraint);
    }

    public void testTwoGroupsWithDmqlConstraints()
    {
        Set groups = new TreeSet();
        groups.add(mNewspaper4);
        groups.add(mNewspaper5);
        groups.add(mAgents2);
        String dmqlConstraint =
            mConditionRuleSet.findDmqlConstraint(groups, "Property", "RES");
        assertEquals("((idx_viewable=\"Y\"),(price=500000-))",
                     dmqlConstraint);
    }

    public void testThreeGroupsWithDmqlConstraints()
    {
        Set groups = new TreeSet();
        groups.add(mNewspaper4);
        groups.add(mNewspaper5);
        groups.add(mAgents2);
        groups.add(mNewspaper6);
        String dmqlConstraint =
            mConditionRuleSet.findDmqlConstraint(groups, "Property", "RES");
        assertEquals("((idx_viewable=\"Y\"),(price=500000-)," +
                     "(color=\"Red\"))",
                     dmqlConstraint);
    }

    public void testNoGroups()
    {
        String sqlConstraint =
            mConditionRuleSet.findSqlConstraint(Collections.EMPTY_SET,
                                                "Property", "MOB");
        assertEquals("", sqlConstraint);
        
        String dmqlConstraint =
            mConditionRuleSet.findDmqlConstraint(Collections.EMPTY_SET,
                                                "Property", "MOB");
        assertEquals("", dmqlConstraint);
    }

    private ConditionRuleSet mConditionRuleSet;
    private Group mNewspaper1;
    private Group mNewspaper2;
    private Group mNewspaper3;
    private Group mNewspaper4;
    private Group mNewspaper5;
    private Group mNewspaper6;
    private Group mAgents1;
    private Group mAgents2;
}
