/*
 */
package org.realtors.rets.server.config;

import java.util.List;
import java.util.ArrayList;

import org.realtors.rets.server.LinesEqualTestCase;
import org.realtors.rets.server.RetsServerException;

public class RetsConfigTest extends LinesEqualTestCase
{
    public void testToXml()
        throws RetsServerException
    {
        RetsConfig retsConfig = new RetsConfig();
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

        List securityConstraints = new ArrayList();
        GroupRules groupRules = new GroupRules("newspaper");
        groupRules.setRecordLimit(25);
        FilterRule filterRule = new FilterRule();
        filterRule.setType(FilterRule.INCLUDE);
        filterRule.setResource("Property");
        filterRule.setRetsClass("RES");
        List systemNames = new ArrayList();
        systemNames.add("LP");
        systemNames.add("LN");
        filterRule.setSystemNames(systemNames);
        groupRules.addFilterRule(filterRule);
        filterRule = new FilterRule();
        filterRule.setType(FilterRule.EXCLUDE);
        filterRule.setResource("Property");
        filterRule.setRetsClass("COM");
        systemNames = new ArrayList();
        systemNames.add("EF");
        filterRule.setSystemNames(systemNames);
        groupRules.addFilterRule(filterRule);
        ConditionRule conditionRule = new ConditionRule();
        conditionRule.setResource("Property");
        conditionRule.setRetsClass("RES");
        conditionRule.setSqlConstraint("r_lp < 500000");
        groupRules.addConditionRule(conditionRule);
        securityConstraints.add(groupRules);

        groupRules = new GroupRules("aggregators");
        groupRules.setTimeRestriction(
            new TimeRestriction(TimeRestriction.DENY, 9, 0, 17, 30));
        securityConstraints.add(groupRules);

        groupRules = new GroupRules("admins");
        groupRules.setTimeRestriction(
            new TimeRestriction(TimeRestriction.ALLOW, 9, 0, 17, 30));
        securityConstraints.add(groupRules);

        // Test empty group rules
        securityConstraints.add(new GroupRules("agent"));
        retsConfig.setSecurityConstraints(securityConstraints);

        String xml = retsConfig.toXml();
	assertLinesEqual(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NL +
            "<rets-config>" + NL +
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
            "      </condition-rule>" + NL +
            "    </group-rules>" + NL +
            "    <group-rules group=\"aggregators\">" + NL +
            "      <time-restriction policy=\"deny\" " +
            "start=\"9:00 AM\" end=\"5:30 PM\" />" + NL +
            "    </group-rules>" + NL +
            "    <group-rules group=\"admins\">" + NL +
            "      <time-restriction policy=\"allow\" " +
            "start=\"9:00 AM\" end=\"5:30 PM\" />" + NL +
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
            "      </condition-rule>\n" +
            "      <exclude-rule resource=\"Property\" class=\"COM\">\n" +
            "        <system-names>LN</system-names>\n" +
            "      </exclude-rule>\n" +
            "      <time-restriction policy=\"allow\" " +
            "start=\"9:00 AM\" end=\"5:30 PM\" />\n" +
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
            "    </group-rules>\n" +
            "  </security-constraints>\n" +
            "</rets-config>";
        RetsConfig retsConfig = RetsConfig.initFromXml(xml);
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

        List securityConstraints = retsConfig.getAllGroupRules();
        assertEquals(3, securityConstraints.size());

        GroupRules groupRules = (GroupRules) securityConstraints.get(0);
        assertEquals("newspaper", groupRules.getGroupName());
        assertEquals(0, groupRules.getRecordLimit());

        /* Check filter rules */
        List rules = groupRules.getFilterRules();
        assertEquals(2, rules.size());
        FilterRule filterRule = (FilterRule) rules.get(0);
        assertEquals(FilterRule.INCLUDE, filterRule.getType());
        assertEquals("Property", filterRule.getResource());
        assertEquals("RES", filterRule.getRetsClass());
        List expected = new ArrayList();
        expected.add("LN");
        expected.add("LP");
        expected.add("EF");
        List systemNames = filterRule.getSystemNames();
        assertEquals(expected, systemNames);

        filterRule = (FilterRule) rules.get(1);
        assertEquals(FilterRule.EXCLUDE, filterRule.getType());
        assertEquals("Property", filterRule.getResource());
        assertEquals("COM", filterRule.getRetsClass());
        expected.clear();
        expected.add("LN");
        systemNames = filterRule.getSystemNames();
        assertEquals(expected, systemNames);

        /* Check condition rules */
        rules = groupRules.getConditionRules();
        assertEquals(1, rules.size());
        ConditionRule conditionRule = (ConditionRule) rules.get(0);
        assertEquals("Property", conditionRule.getResource());
        assertEquals("RES", conditionRule.getRetsClass());
        assertEquals("r_lp < 500000", conditionRule.getSqlConstraint());

        TimeRestriction timeRestriction =
            new TimeRestriction(TimeRestriction.ALLOW, 9, 0, 17, 30);
        assertEquals(timeRestriction, groupRules.getTimeRestriction());

        groupRules = (GroupRules) securityConstraints.get(1);
        assertEquals("agent", groupRules.getGroupName());
        assertEquals(25, groupRules.getRecordLimit());
        assertNull(groupRules.getTimeRestriction());
        rules = groupRules.getFilterRules();
        assertEquals(1, rules.size());
        filterRule = (FilterRule) rules.get(0);
        assertEquals(FilterRule.EXCLUDE, filterRule.getType());
        assertEquals("Property", filterRule.getResource());
        assertEquals("COM", filterRule.getRetsClass());
        expected.clear();
        expected.add("LN");
        expected.add("LF");

        groupRules = (GroupRules) securityConstraints.get(2);
        assertEquals("aggregators", groupRules.getGroupName());
        assertEquals(0, groupRules.getRecordLimit());
        assertEquals(0, groupRules.getFilterRules().size());
        assertEquals(0, groupRules.getConditionRules().size());
        timeRestriction =
            new TimeRestriction(TimeRestriction.DENY, 9, 0, 17, 30);
        assertEquals(timeRestriction, groupRules.getTimeRestriction());
    }

    public void testFromXmlDefaults()
        throws RetsServerException
    {
        String xml =
            "<?xml version='1.0' ?>\n" +
            "<rets-config>\n" +
            "</rets-config>";
        RetsConfig retsConfig = RetsConfig.initFromXml(xml);
        assertNull(retsConfig.getPhotoPattern());
        assertNull(retsConfig.getGetObjectRoot());
        assertEquals(-1, retsConfig.getNonceInitialTimeout());
        assertEquals(-1, retsConfig.getNonceSuccessTimeout());
    }

    public void testGetDefaults()
    {
        RetsConfig retsConfig = new RetsConfig();
        assertEquals("/", retsConfig.getGetObjectRoot("/"));
        assertEquals("%i.jpg", retsConfig.getPhotoPattern("%i.jpg"));
        assertEquals("foo", retsConfig.getObjectSetPattern("foo"));
        assertEquals(2, retsConfig.getNonceInitialTimeout(2));
        assertEquals(2, retsConfig.getNonceSuccessTimeout(2));

        retsConfig.setGetObjectRoot("/tmp/pictures");
        retsConfig.setPhotoPattern("%k-%i.jpg");
        retsConfig.setObjectSetPattern("%k.xml");
        retsConfig.setNonceInitialTimeout(5);
        retsConfig.setNonceSuccessTimeout(10);

        assertEquals("/tmp/pictures", retsConfig.getGetObjectRoot("/"));
        assertEquals("%k-%i.jpg", retsConfig.getPhotoPattern("%i.jpg"));
        assertEquals("%k.xml", retsConfig.getObjectSetPattern("foo"));
        assertEquals(5, retsConfig.getNonceInitialTimeout(2));
        assertEquals(10, retsConfig.getNonceSuccessTimeout(2));
    }
}
