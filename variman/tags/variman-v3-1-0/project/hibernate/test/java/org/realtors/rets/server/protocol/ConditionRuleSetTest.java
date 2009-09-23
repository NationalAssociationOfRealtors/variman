package org.realtors.rets.server.protocol;

import java.util.Set;
import java.util.TreeSet;
import java.util.Collections;

import junit.framework.TestCase;
import org.realtors.rets.server.Group;
import org.realtors.rets.server.config.ConditionRule;
import org.realtors.rets.server.config.GroupRules;

public class ConditionRuleSetTest extends TestCase
{
    protected void setUp()
    {
        mConditionRuleSet = new ConditionRuleSet();
        mNewspaper1 = new Group("Newspaper 1");
        mNewspaper2 = new Group("Newspaper 2");
        mNewspaper3 = new Group("Newspaper 3");
        mAgents = new Group("Agents");

        ConditionRule rule = new ConditionRule();
        rule.setResource("Property");
        rule.setRetsClass("RES");
        rule.setSqlConstraint("price < 500000");
        GroupRules rules = new GroupRules(mNewspaper1.getName());
        rules.addConditionRule(rule);
        mConditionRuleSet.addRules(rules);

        rule = new ConditionRule();
        rule.setResource("Property");
        rule.setRetsClass("MOB");
        rule.setSqlConstraint("price > 250000");
        rules = new GroupRules(mNewspaper2.getName());
        rules.addConditionRule(rule);
        mConditionRuleSet.addRules(rules);

        rule = new ConditionRule();
        rule.setResource("Property");
        rule.setRetsClass("RES");
        rule.setSqlConstraint("color = 'Red'");
        rules = new GroupRules(mNewspaper3.getName());
        rules.addConditionRule(rule);
        mConditionRuleSet.addRules(rules);

        rule = new ConditionRule();
        rule.setResource("Property");
        rule.setRetsClass("RES");
        rule.setSqlConstraint("idx_viewable = 'Y'");
        rules = new GroupRules(mAgents.getName());
        rules.addConditionRule(rule);
        mConditionRuleSet.addRules(rules);
    }

    public void testNoGroupsWithConstraints()
    {
        Set groups = new TreeSet();
        groups.add(mNewspaper1);
        groups.add(mAgents);
        String sqlConstraint =
            mConditionRuleSet.findSqlConstraint(groups, "Property", "MOB");
        assertEquals("", sqlConstraint);
    }

    public void testOneGroupWithConstraints()
    {
        Set groups = new TreeSet();
        groups.add(mNewspaper1);
        groups.add(mNewspaper2);
        String sqlConstraint =
            mConditionRuleSet.findSqlConstraint(groups, "Property", "RES");
        assertEquals("(price < 500000)", sqlConstraint);
    }

    public void testTwoGroupsWihtConstraints()
    {
        Set groups = new TreeSet();
        groups.add(mNewspaper1);
        groups.add(mNewspaper2);
        groups.add(mAgents);
        String sqlConstraint =
            mConditionRuleSet.findSqlConstraint(groups, "Property", "RES");
        assertEquals("((idx_viewable = 'Y') AND (price < 500000))",
                     sqlConstraint);
    }

    public void testThreeGroupsWihtConstraints()
    {
        Set groups = new TreeSet();
        groups.add(mNewspaper1);
        groups.add(mNewspaper2);
        groups.add(mAgents);
        groups.add(mNewspaper3);
        String sqlConstraint =
            mConditionRuleSet.findSqlConstraint(groups, "Property", "RES");
        assertEquals("((idx_viewable = 'Y') AND (price < 500000) AND " +
                     "(color = 'Red'))",
                     sqlConstraint);
    }

    public void testNoGroups()
    {
        String sqlConstraint =
            mConditionRuleSet.findSqlConstraint(Collections.EMPTY_SET,
                                                "Property", "MOB");
        assertEquals("", sqlConstraint);
    }

    private ConditionRuleSet mConditionRuleSet;
    private Group mNewspaper1;
    private Group mNewspaper2;
    private Group mNewspaper3;
    private Group mAgents;
}
