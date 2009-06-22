/*
 */
package org.realtors.rets.server.config;

import java.util.List;
import java.util.ArrayList;

import org.realtors.rets.server.Group;
import org.realtors.rets.server.LinesEqualTestCase;
import org.realtors.rets.server.QueryLimit;
import org.realtors.rets.server.RetsServerException;

public class RetsConfigTest extends LinesEqualTestCase
{
    public void testToXml()
    {
        RetsConfig retsConfig = new RetsConfigImpl();
        retsConfig.setGetObjectRoot("/tmp/pictures");
        retsConfig.setPhotoPattern("%k-%i.jpg");
        retsConfig.setObjectSetPattern("%k.xml");
        retsConfig.setNonceInitialTimeout(5);
        retsConfig.setNonceSuccessTimeout(10);
        retsConfig.setPort(7103);
        retsConfig.setMetadataDir("WEB-INF/rets/metadata");

        DatabaseConfig database = new DatabaseConfig();
        database.setDatabaseType(DatabaseType.POSTGRESQL);
        database.setHostName("localhost");
        database.setDatabaseName("rets_test");
        database.setUsername("dave");
        database.setPassword("");
        database.setMaxActive(100);
        database.setMaxWait(120000);
        database.setMaxIdle(10);
        database.setMaxPsActive(50);
        database.setMaxPsWait(60000);
        database.setMaxPsIdle(5);
        database.setShowSql(true);
        retsConfig.setDatabase(database);

        List/*GroupRules*/ allGroupRules = new ArrayList/*GroupRules*/();
        GroupRules groupRules = new GroupRulesImpl(new Group("newspaper"));
        groupRules.setRecordLimit(25);
        FilterRule filterRule = new FilterRuleImpl();
        filterRule.setType(FilterRule.Type.INCLUDE);
        filterRule.setResourceID("Property");
        filterRule.setRetsClassName("RES");
        List/*String*/ systemNames = new ArrayList/*String*/();
        systemNames.add("LP");
        systemNames.add("LN");
        filterRule.setSystemNames(systemNames);
        groupRules.addFilterRule(filterRule);
        filterRule = new FilterRuleImpl();
        filterRule.setType(FilterRule.Type.EXCLUDE);
        filterRule.setResourceID("Property");
        filterRule.setRetsClassName("COM");
        systemNames = new ArrayList/*String*/();
        systemNames.add("EF");
        filterRule.setSystemNames(systemNames);
        groupRules.addFilterRule(filterRule);
        ConditionRule conditionRule = new ConditionRuleImpl();
        conditionRule.setResourceID("Property");
        conditionRule.setRetsClassName("RES");
        conditionRule.setSqlConstraint("r_lp < 500000");
        conditionRule.setDmqlConstraint("(lp=500000-)");
        groupRules.addConditionRule(conditionRule);
        groupRules.setQueryLimit(
            QueryLimit.valueOf(500L, QueryLimit.Period.PER_DAY));
        allGroupRules.add(groupRules);
        
        groupRules = new GroupRulesImpl(new Group("aggregators"));
        groupRules.setTimeRestriction(
            new TimeRestriction(TimeRestriction.DENY, 9, 0, 17, 30));
        groupRules.setQueryLimit(
            QueryLimit.valueOf(50L, QueryLimit.Period.PER_HOUR));
        allGroupRules.add(groupRules);
        
        groupRules = new GroupRulesImpl(new Group("admins"));
        groupRules.setTimeRestriction(
            new TimeRestriction(TimeRestriction.ALLOW, 9, 0, 17, 30));
        groupRules.setQueryLimit(
            QueryLimit.valueOf(5L, QueryLimit.Period.PER_MINUTE));
        allGroupRules.add(groupRules);

        // Test empty group rules
        allGroupRules.add(new GroupRulesImpl(new Group("agent")));
        SecurityConstraints securityConstraints = new SecurityConstraints();
        securityConstraints.setAllGroupRules(allGroupRules);
        retsConfig.setSecurityConstraints(securityConstraints);

        String xml = XmlRetsConfigUtils.toXml(retsConfig);
        assertLinesEqual(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NL +
            "<rets-config>" + NL +
            "  <address />" + NL +
            "  <port>7103</port>" + NL +
            "  <metadata-dir>WEB-INF/rets/metadata</metadata-dir>" + NL +
            "  <get-object-root>/tmp/pictures</get-object-root>" + NL +
            "  <photo-pattern>%k-%i.jpg</photo-pattern>" + NL +
            "  <object-set-pattern>%k.xml</object-set-pattern>" + NL +
            "  <nonce-initial-timeout>5</nonce-initial-timeout>" + NL +
            "  <nonce-success-timeout>10</nonce-success-timeout>" + NL +
            "  <database>" + NL +
            "    <type>postgresql</type>" + NL +
            "    <host>localhost</host>" + NL +
            "    <name>rets_test</name>" + NL +
            "    <username>dave</username>" + NL +
            "    <password></password>" + NL +
            "    <max-active>100</max-active>" + NL +
            "    <max-idle>10</max-idle>" + NL +
            "    <max-wait>120000</max-wait>" + NL +
            "    <max-ps-active>50</max-ps-active>" + NL +
            "    <max-ps-idle>5</max-ps-idle>" + NL +
            "    <max-ps-wait>60000</max-ps-wait>" + NL +
            "    <show-sql>true</show-sql>" + NL +
            "  </database>" + NL +
            "  <security-constraints>" + NL +
            "    <group-rules group=\"newspaper\">" + NL +
            "      <record-limit>25</record-limit>" + NL +
            "      <include-rule resource=\"Property\" class=\"RES\">" + NL +
            "        <system-names>LP LN</system-names>" + NL +
            "      </include-rule>" + NL +
            "      <exclude-rule resource=\"Property\" class=\"COM\">" + NL +
            "        <system-names>EF</system-names>" + NL +
            "      </exclude-rule>" + NL +
            "      <condition-rule resource=\"Property\" class=\"RES\">" + NL +
            "        <sql-constraint>r_lp &lt; 500000</sql-constraint>" + NL +
            "        <dmql-constraint>(lp=500000-)</dmql-constraint>" + NL +
            "      </condition-rule>" + NL +
            "      <query-count-limit period=\"per-day\">500</query-count-limit>" + NL +
            "    </group-rules>" + NL +
            "    <group-rules group=\"aggregators\">" + NL +
            "      <time-restriction policy=\"deny\" " +
            "start=\"9:00 AM\" end=\"5:30 PM\" />" + NL +
            "      <query-count-limit period=\"per-hour\">50</query-count-limit>" + NL +
            "    </group-rules>" + NL +
            "    <group-rules group=\"admins\">" + NL +
            "      <time-restriction policy=\"allow\" " +
            "start=\"9:00 AM\" end=\"5:30 PM\" />" + NL +
            "      <query-count-limit period=\"per-minute\">5</query-count-limit>" + NL +
            "    </group-rules>" + NL +
            "  </security-constraints>" + NL +
            "</rets-config>" + NL +
            NL,
            xml
        );
    }

    public void testFromXml()
        throws RetsServerException
    {
        String xml =
            "<?xml version='1.0' ?>\n" +
            "<rets-config>\n" +
            "  <address>192.168.1.1</address>\n" +
            "  <port>7103</port>\n" +
            "  <metadata-dir>WEB-INF/rets/metadata</metadata-dir>\n" +
            "  <get-object-root>/tmp/pictures</get-object-root>\n" +
            "  <photo-pattern>%k-%i.jpg</photo-pattern>\n" +
            "  <object-set-pattern>%k.xml</object-set-pattern>\n" +
            "  <nonce-initial-timeout>5</nonce-initial-timeout>\n" +
            "  <nonce-success-timeout>10</nonce-success-timeout>\n" +
            "  <database>\n" +
            "    <type>postgresql</type>\n" +
            "    <host>localhost</host>\n" +
            "    <name>rets_test</name>\n" +
            "    <username>dave</username>\n" +
            "    <password/>" +
            "    <max-active>100</max-active>\n" +
            "    <max-wait>120000</max-wait>\n" +
            "    <max-idle>10</max-idle>\n" +
            "    <max-ps-active>50</max-ps-active>\n" +
            "    <max-ps-wait>60000</max-ps-wait>\n" +
            "    <max-ps-idle>5</max-ps-idle>\n" +
            "    <show-sql>true</show-sql>\n" +
            "  </database>\n" +
            "  <security-constraints>\n" +
            "    <group-rules group=\"newspaper\">\n" +
            "      <include-rule resource=\"Property\" class=\"RES\">\n" +
            "        <system-names>LN LP\nEF</system-names>\n" +
            "      </include-rule>\n" +
            "      <condition-rule resource=\"Property\" class=\"RES\">\n" +
            "        <sql-constraint>r_lp &lt; 500000</sql-constraint>\n" +
            "        <dmql-constraint>(lp=500000-)</dmql-constraint>\n" +
            "      </condition-rule>\n" +
            "      <exclude-rule resource=\"Property\" class=\"COM\">\n" +
            "        <system-names>LN</system-names>\n" +
            "      </exclude-rule>\n" +
            "      <time-restriction policy=\"allow\" " +
            "start=\"9:00 AM\" end=\"5:30 PM\" />\n" +
            "      <query-count-limit period=\"per-day\">50</query-count-limit>\n" +
            "    </group-rules>\n" +
            "    <group-rules group=\"agent\">\n" +
            "      <record-limit>25</record-limit>\n" +
            "      <exclude-rule resource=\"Property\" class=\"COM\">\n" +
            "        <system-names>LN\nEF</system-names>\n" +
            "      </exclude-rule>" +
            "    </group-rules>\n" +
            "    <group-rules group=\"aggregators\">\n" +
            "      <time-restriction policy=\"deny\" " +
            "start=\"9:00 AM\" end=\"5:30 PM\" />\n" +
            "      <query-count-limit period=\"per-hour\">15</query-count-limit>\n" +
            "    </group-rules>\n" +
            "  </security-constraints>\n" +
            "</rets-config>";
        RetsConfig retsConfig = XmlRetsConfigUtils.initFromXml(xml);
        assertEquals("192.168.1.1", retsConfig.getAddress());
        assertEquals(7103, retsConfig.getPort());
        assertEquals("WEB-INF/rets/metadata", retsConfig.getMetadataDir());
        assertEquals("%k-%i.jpg", retsConfig.getPhotoPattern());
        assertEquals("%k.xml", retsConfig.getObjectSetPattern());
        assertEquals("/tmp/pictures", retsConfig.getGetObjectRoot());
        assertEquals(5, retsConfig.getNonceInitialTimeout());
        assertEquals(10, retsConfig.getNonceSuccessTimeout());

        DatabaseConfig database = retsConfig.getDatabase();
        assertEquals(DatabaseType.POSTGRESQL, database.getDatabaseType());
        assertEquals("localhost", database.getHostName());
        assertEquals("rets_test", database.getDatabaseName());
        assertEquals("org.postgresql.Driver", database.getDriver());
        assertEquals("jdbc:postgresql://localhost/rets_test",
                     database.getUrl());
        assertEquals("dave", database.getUsername());
        assertEquals("", database.getPassword());
        assertEquals(100, database.getMaxActive());
        assertEquals(120000, database.getMaxWait());
        assertEquals(10, database.getMaxIdle());
        assertEquals(50, database.getMaxPsActive());
        assertEquals(60000, database.getMaxPsWait());
        assertEquals(5, database.getMaxPsIdle());
        assertTrue(database.getShowSql());

        List/*GroupRules*/ allGroupRules = retsConfig.getSecurityConstraints().getAllGroupRules();
        assertEquals(3, allGroupRules.size());
        
        GroupRules groupRules = (GroupRules) allGroupRules.get(0);
        assertEquals("newspaper", groupRules.getGroup().getName());
        assertEquals(0, groupRules.getRecordLimit());

        /* Check filter rules */
        List rules = groupRules.getFilterRules();
        assertEquals(2, rules.size());
        FilterRule filterRule = (FilterRule) rules.get(0);
        assertEquals(FilterRule.Type.INCLUDE, filterRule.getType());
        assertEquals("Property", filterRule.getResourceID());
        assertEquals("RES", filterRule.getRetsClassName());
        List/*String*/ expected = new ArrayList/*String*/();
        expected.add("LN");
        expected.add("LP");
        expected.add("EF");
        List/*String*/ systemNames = filterRule.getSystemNames();
        assertEquals(expected, systemNames);

        filterRule = (FilterRule) rules.get(1);
        assertEquals(FilterRule.Type.EXCLUDE, filterRule.getType());
        assertEquals("Property", filterRule.getResourceID());
        assertEquals("COM", filterRule.getRetsClassName());
        expected.clear();
        expected.add("LN");
        systemNames = filterRule.getSystemNames();
        assertEquals(expected, systemNames);

        /* Check condition rules */
        rules = groupRules.getConditionRules();
        assertEquals(1, rules.size());
        ConditionRule conditionRule = (ConditionRule) rules.get(0);
        assertEquals("Property", conditionRule.getResourceID());
        assertEquals("RES", conditionRule.getRetsClassName());
        assertEquals("r_lp < 500000", conditionRule.getSqlConstraint());
        assertEquals("(lp=500000-)", conditionRule.getDmqlConstraint());

        TimeRestriction timeRestriction =
            new TimeRestriction(TimeRestriction.ALLOW, 9, 0, 17, 30);
        assertEquals(timeRestriction, groupRules.getTimeRestriction());

        /* Check query count limits */
        QueryLimit queryLimit = groupRules.getQueryLimit();
        assertFalse(queryLimit.hasNoQueryLimit());
        assertEquals(QueryLimit.Period.PER_DAY, queryLimit.getPeriod());
        assertEquals(50, queryLimit.getLimit());

        groupRules = (GroupRules) allGroupRules.get(1);
        assertEquals("agent", groupRules.getGroup().getName());
        assertEquals(25, groupRules.getRecordLimit());
        assertNull(groupRules.getTimeRestriction());
        rules = groupRules.getFilterRules();
        assertEquals(1, rules.size());
        filterRule = (FilterRule) rules.get(0);
        assertEquals(FilterRule.Type.EXCLUDE, filterRule.getType());
        assertEquals("Property", filterRule.getResourceID());
        assertEquals("COM", filterRule.getRetsClassName());
        expected.clear();
        expected.add("LN");
        expected.add("LF");

        queryLimit = groupRules.getQueryLimit();
        assertTrue(queryLimit.hasNoQueryLimit());

        groupRules = (GroupRules) allGroupRules.get(2);
        assertEquals("aggregators", groupRules.getGroup().getName());
        assertEquals(0, groupRules.getRecordLimit());
        assertEquals(0, groupRules.getFilterRules().size());
        assertEquals(0, groupRules.getConditionRules().size());
        timeRestriction =
            new TimeRestriction(TimeRestriction.DENY, 9, 0, 17, 30);
        assertEquals(timeRestriction, groupRules.getTimeRestriction());

        queryLimit = groupRules.getQueryLimit();
        assertFalse(queryLimit.hasNoQueryLimit());
        assertEquals(QueryLimit.Period.PER_HOUR, queryLimit.getPeriod());
        assertEquals(15, queryLimit.getLimit());
    }
    
    public void testFromXmlDefaults()
        throws RetsServerException
    {
        String xml =
            "<?xml version='1.0' ?>\n" +
            "<rets-config>\n" +
            "</rets-config>";
        RetsConfig retsConfig = XmlRetsConfigUtils.initFromXml(xml);
        assertNull(retsConfig.getPhotoPattern());
        assertNull(retsConfig.getGetObjectRoot());
        assertEquals(-1, retsConfig.getNonceInitialTimeout());
        assertEquals(-1, retsConfig.getNonceSuccessTimeout());
    }

    public void testSetBlankAddress()
    {
        RetsConfig retsConfig = new RetsConfigImpl();
        assertNull(retsConfig.getAddress());
        retsConfig.setAddress("");
        assertNull(retsConfig.getAddress());
        retsConfig.setAddress("  ");
        assertNull(retsConfig.getAddress());
        retsConfig.setAddress(null);
        assertNull(retsConfig.getAddress());
    }

    public void testGetDefaults()
    {
        RetsConfig retsConfig = new RetsConfigImpl();
        assertEquals("/", RetsConfigUtils.getGetObjectRoot(retsConfig, "/"));
        assertEquals("%i.jpg", RetsConfigUtils.getPhotoPattern(retsConfig, "%i.jpg"));
        assertEquals("foo", RetsConfigUtils.getObjectSetPattern(retsConfig, "foo"));
        assertEquals(2, RetsConfigUtils.getNonceInitialTimeout(retsConfig, 2));
        assertEquals(2, RetsConfigUtils.getNonceSuccessTimeout(retsConfig, 2));

        retsConfig.setGetObjectRoot("/tmp/pictures");
        retsConfig.setPhotoPattern("%k-%i.jpg");
        retsConfig.setObjectSetPattern("%k.xml");
        retsConfig.setNonceInitialTimeout(5);
        retsConfig.setNonceSuccessTimeout(10);

        assertEquals("/tmp/pictures", RetsConfigUtils.getGetObjectRoot(retsConfig, "/"));
        assertEquals("%k-%i.jpg", RetsConfigUtils.getPhotoPattern(retsConfig, "%i.jpg"));
        assertEquals("%k.xml", RetsConfigUtils.getObjectSetPattern(retsConfig, "foo"));
        assertEquals(5, RetsConfigUtils.getNonceInitialTimeout(retsConfig, 2));
        assertEquals(10, RetsConfigUtils.getNonceSuccessTimeout(retsConfig, 2));
    }
    
}
