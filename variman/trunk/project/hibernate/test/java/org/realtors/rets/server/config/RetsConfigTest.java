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
        retsConfig.setGetObjectPattern("%k-%i.jpg");
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
        RuleDescription ruleDescription = new RuleDescription();
        ruleDescription.setType(RuleDescription.INCLUDE);
        ruleDescription.setResource("Property");
        ruleDescription.setRetsClass("RES");
        List systemNames = new ArrayList();
        systemNames.add("LP");
        systemNames.add("LN");
        ruleDescription.setSystemNames(systemNames);
        groupRules.addRule(ruleDescription);
        ruleDescription = new RuleDescription();
        ruleDescription.setType(RuleDescription.EXCLUDE);
        ruleDescription.setResource("Property");
        ruleDescription.setRetsClass("COM");
        systemNames = new ArrayList();
        systemNames.add("EF");
        ruleDescription.setSystemNames(systemNames);
        groupRules.addRule(ruleDescription);
        securityConstraints.add(groupRules);
        retsConfig.setSecurityConstraints(securityConstraints);

        String xml = retsConfig.toXml();
        assertLinesEqual(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<rets-config>\n" +
            "  <port>7103</port>\n" +
            "  <metadata-dir>WEB-INF/rets/metadata</metadata-dir>\n" +
            "  <get-object-root>/tmp/pictures</get-object-root>\n" +
            "  <get-object-pattern>%k-%i.jpg</get-object-pattern>\n" +
            "  <nonce-initial-timeout>5</nonce-initial-timeout>\n" +
            "  <nonce-success-timeout>10</nonce-success-timeout>\n" +
            "  <database>\n" +
            "    <type>postgresql</type>\n" +
            "    <host>localhost</host>\n" +
            "    <name>rets_test</name>\n" +
            "    <username>dave</username>\n" +
            "    <password></password>\n" +
            "    <max-active>100</max-active>\n" +
            "    <max-idle>10</max-idle>\n" +
            "    <max-wait>120000</max-wait>\n" +
            "    <max-ps-active>50</max-ps-active>\n" +
            "    <max-ps-idle>5</max-ps-idle>\n" +
            "    <max-ps-wait>60000</max-ps-wait>\n" +
            "    <show-sql>true</show-sql>\n" +
            "  </database>\n" +
            "  <security-constraints>\n" +
            "    <group-rules group=\"newspaper\">\n" +
            "      <include-rule resource=\"Property\" class=\"RES\">\n" +
            "        <system-names>LP LN</system-names>\n" +
            "      </include-rule>\n" +
            "      <exclude-rule resource=\"Property\" class=\"COM\">\n" +
            "        <system-names>EF</system-names>\n" +
            "      </exclude-rule>\n" +
            "    </group-rules>\n" +
            "  </security-constraints>\n" +
            "</rets-config>\n" +
            "\n",
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
            "  <get-object-pattern>%k-%i.jpg</get-object-pattern>\n" +
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
            "      <exclude-rule resource=\"Property\" class=\"COM\">\n" +
            "        <system-names>LN</system-names>\n" +
            "      </exclude-rule>\n" +
            "      <condition-rule resource=\"Property\" class=\"RES\">\n" +
            "        <sql-constraint>r_lp &lt; 500000</sql-constraint>\n" +
            "      </condition-rule>\n" +
            "    </group-rules>\n" +
            "    <group-rules group=\"agent\">\n" +
            "      <exclude-rule resource=\"Property\" class=\"COM\">\n" +
            "        <system-names>LN\nEF</system-names>\n" +
            "      </exclude-rule>" +
            "    </group-rules>\n" +
            "  </security-constraints>\n" +
            "</rets-config>";
        RetsConfig retsConfig = RetsConfig.initFromXml(xml);
        assertEquals(7103, retsConfig.getPort());
        assertEquals("WEB-INF/rets/metadata", retsConfig.getMetadataDir());
        assertEquals("%k-%i.jpg", retsConfig.getGetObjectPattern());
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
        assertEquals(2, securityConstraints.size());

        GroupRules groupRules = (GroupRules) securityConstraints.get(0);
        assertEquals("newspaper", groupRules.getGroupName());
        List rules = groupRules.getRules();
        assertEquals(2, rules.size());
        RuleDescription ruleDescription = (RuleDescription) rules.get(0);
        assertEquals(RuleDescription.INCLUDE, ruleDescription.getType());
        assertEquals("Property", ruleDescription.getResource());
        assertEquals("RES", ruleDescription.getRetsClass());
        List expected = new ArrayList();
        expected.add("LN");
        expected.add("LP");
        expected.add("EF");
        List systemNames = ruleDescription.getSystemNames();
        assertEquals(expected, systemNames);

        ruleDescription = (RuleDescription) rules.get(1);
        assertEquals(RuleDescription.EXCLUDE, ruleDescription.getType());
        assertEquals("Property", ruleDescription.getResource());
        assertEquals("COM", ruleDescription.getRetsClass());
        expected.clear();
        expected.add("LN");
        systemNames = ruleDescription.getSystemNames();
        assertEquals(expected, systemNames);

        /* Check condition rules */
        rules = groupRules.getConditionRules();
        assertEquals(1, rules.size());
        ConditionRule conditionRule = (ConditionRule) rules.get(0);
        assertEquals("Property", conditionRule.getResource());
        assertEquals("RES", conditionRule.getRetsClass());
        assertEquals("r_lp < 500000", conditionRule.getSqlConstraint());
        
        groupRules = (GroupRules) securityConstraints.get(1);
        assertEquals("agent", groupRules.getGroupName());
        rules = groupRules.getRules();
        assertEquals(1, rules.size());
        ruleDescription = (RuleDescription) rules.get(0);
        assertEquals(RuleDescription.EXCLUDE, ruleDescription.getType());
        assertEquals("Property", ruleDescription.getResource());
        assertEquals("COM", ruleDescription.getRetsClass());
        expected.clear();
        expected.add("LN");
        expected.add("LF");
    }

    public void testFromXmlDefaults()
        throws RetsServerException
    {
        String xml =
            "<?xml version='1.0' ?>\n" +
            "<rets-config>\n" +
            "</rets-config>";
        RetsConfig retsConfig = RetsConfig.initFromXml(xml);
        assertNull(retsConfig.getGetObjectPattern());
        assertNull(retsConfig.getGetObjectRoot());
        assertEquals(-1, retsConfig.getNonceInitialTimeout());
        assertEquals(-1, retsConfig.getNonceSuccessTimeout());
    }

    public void testGetDefaults()
    {
        RetsConfig retsConfig = new RetsConfig();
        assertEquals("/", retsConfig.getGetObjectRoot("/"));
        assertEquals("%i.jpg", retsConfig.getGetObjectPattern("%i.jpg"));
        assertEquals(2, retsConfig.getNonceInitialTimeout(2));
        assertEquals(2, retsConfig.getNonceSuccessTimeout(2));

        retsConfig.setGetObjectRoot("/tmp/pictures");
        retsConfig.setGetObjectPattern("%k-%i.jpg");
        retsConfig.setNonceInitialTimeout(5);
        retsConfig.setNonceSuccessTimeout(10);

        assertEquals("/tmp/pictures", retsConfig.getGetObjectRoot("/"));
        assertEquals("%k-%i.jpg", retsConfig.getGetObjectPattern("%i.jpg"));
        assertEquals(5, retsConfig.getNonceInitialTimeout(2));
        assertEquals(10, retsConfig.getNonceSuccessTimeout(2));
    }
}
