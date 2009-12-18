/*
 * Variman RETS Server
 *
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.QueryCount;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.Util;

public class XmlRetsConfigUtils {
    public static String toXml(RetsConfig retsConfig) throws RetsServerException {
        Element retsCfgElmt = new Element(RETS_CONFIG);
        addChild(retsCfgElmt, ADDRESS, retsConfig.getAddress());
        addChild(retsCfgElmt, PORT, retsConfig.getPort());
        addChild(retsCfgElmt, METADATA_DIR, retsConfig.getMetadataDir());
        addChild(retsCfgElmt, GET_OBJECT_ROOT, retsConfig.getGetObjectRoot());
        addChild(retsCfgElmt, PHOTO_PATTERN, retsConfig.getPhotoPattern());
        addChild(retsCfgElmt, OBJECT_SET_PATTERN, retsConfig.getObjectSetPattern());
        addChild(retsCfgElmt, NONCE_INITIAL_TIMEOUT, retsConfig.getNonceInitialTimeout());
        addChild(retsCfgElmt, NONCE_SUCCESS_TIMEOUT, retsConfig.getNonceSuccessTimeout());
        addChild(retsCfgElmt, STRICT_PARSING, retsConfig.getStrictParsing());

        DatabaseConfig databaseConfig = retsConfig.getDatabase();
        Element database = new Element(DATABASE);
        addChild(database, TYPE, databaseConfig.getDatabaseType().getName());
        addChild(database, HOST, databaseConfig.getHostName());
        addChild(database, NAME, databaseConfig.getDatabaseName());
        addChild(database, USERNAME, databaseConfig.getUsername());
        addChild(database, PASSWORD, databaseConfig.getPassword());
        addChild(database, MAX_ACTIVE, databaseConfig.getMaxActive());
        addChild(database, MAX_IDLE, databaseConfig.getMaxIdle());
        addChild(database, MAX_WAIT, databaseConfig.getMaxWait());
        addChild(database, MAX_PS_ACTIVE, databaseConfig.getMaxPsActive());
        addChild(database, MAX_PS_IDLE, databaseConfig.getMaxPsIdle());
        addChild(database, MAX_PS_WAIT, databaseConfig.getMaxPsWait());
        addChild(database, SHOW_SQL, databaseConfig.getShowSql());
        retsCfgElmt.addContent(database);
        retsCfgElmt.addContent(getSecurityContraintsElement(retsConfig));

        Format xmlFormat = Format.getPrettyFormat();
        xmlFormat.setLineSeparator(SystemUtils.LINE_SEPARATOR);
        XMLOutputter xmlOutputter = new XMLOutputter(xmlFormat);
        return xmlOutputter.outputString(new Document(retsCfgElmt));
    }

    public static RetsConfig initFromXmlFile(String file) throws RetsServerException {
        try {
            return initFromXml(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RetsServerException(e);
        }
    }

    public static RetsConfig initFromXml(Reader xml) throws RetsServerException {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(xml);
            return elementToConfig(document.getRootElement());
        } catch (IOException e) {
            throw new RetsServerException(e);
        } catch (JDOMException e) {
            throw new RetsServerException(e);
        }
    }

    private static Element getSecurityContraintsElement(RetsConfig retsConfig) {
        Element securityContraints = new Element(SECURITY_CONSTRAINTS);
        List allGroupRules = retsConfig.getAllGroupRules();
        for (int i = 0; i < allGroupRules.size(); i++) {
            GroupRules groupRules = (GroupRules)allGroupRules.get(i);
            Element groupRulesElement = new Element(GROUP_RULES);
            groupRulesElement.setAttribute(GROUP, groupRules.getGroupName());
            addRecordLimit(groupRules, groupRulesElement);
            addFilterRules(groupRules, groupRulesElement);
            addConditionRules(groupRules, groupRulesElement);
            addTimeRestriction(groupRules, groupRulesElement);
            addQueryCountToElement(groupRules, groupRulesElement);
            if (groupRulesElement.getChildren().size() != 0) {
                securityContraints.addContent(groupRulesElement);
            }
        }
        return securityContraints;
    }

    private static void addQueryCountToElement(GroupRules groupRules, Element groupRulesElement) {
        if (groupRules.hasNoQueryLimit()) {
            return;
        }
        Element element = new Element(QUERY_COUNT_LIMIT);
        QueryCount.LimitPeriod limitPeriod = groupRules.getQueryCountLimitPeriod();
        if (limitPeriod.equals(QueryCount.PER_DAY)) {
            element.setAttribute(PERIOD, PER_DAY);
        } else if (limitPeriod.equals(QueryCount.PER_HOUR)) {
            element.setAttribute(PERIOD, PER_HOUR);
        } else if (limitPeriod.equals(QueryCount.PER_MINUTE)) {
            element.setAttribute(PERIOD, PER_MINUTE);
        } else {
            LOG.warn("Unknown query count limit period: " + limitPeriod);
            return;
        }
        element.setText("" + groupRules.getQueryCountLimit());
        groupRulesElement.addContent(element);
    }

    private static void addTimeRestriction(GroupRules groupRules, Element groupRulesElement) {
        TimeRestriction timeRestriction = groupRules.getTimeRestriction();
        if (timeRestriction == null) {
            return;
        }
        Element element = new Element(TIME_RESTRICTION);
        element.setAttribute(POLICY, timeRestriction.getPolicy().getName());
        Date time = timeRestriction.getStartAsCalendar().getTime();
        DateFormat formatter = DateFormat.getTimeInstance(DateFormat.SHORT);
        element.setAttribute(START, formatter.format(time));
        time = timeRestriction.getEndAsCalendar().getTime();
        element.setAttribute(END, formatter.format(time));
        groupRulesElement.addContent(element);
    }

    private static void addRecordLimit(GroupRules groupRules, Element groupRulesElement) {
        int recordLimit = groupRules.getRecordLimit();
        if (recordLimit > 0) {
            Element recordLimitElement = new Element(RECORD_LIMIT);
            recordLimitElement.setText(Integer.toString(recordLimit));
            groupRulesElement.addContent(recordLimitElement);
        }
    }

    private static void addFilterRules(GroupRules groupRules, Element element) {
        List rules = groupRules.getFilterRules();
        for (int i = 0; i < rules.size(); i++) {
            FilterRule filterRule = (FilterRule)rules.get(i);
            Element ruleElement;
            if (filterRule.getType() == FilterRule.INCLUDE) {
                ruleElement = new Element(INCLUDE_RULE);
            } else {
                ruleElement = new Element(EXCLUDE_RULE);
            }
            ruleElement.setAttribute(RESOURCE, filterRule.getResource());
            ruleElement.setAttribute(CLASS, filterRule.getRetsClass());
            String systemNames = StringUtils.join(filterRule.getSystemNames().iterator(), " ");
            Element systemNamesElement = new Element(SYSTEM_NAMES);
            systemNamesElement.setText(systemNames);
            ruleElement.addContent(systemNamesElement);
            element.addContent(ruleElement);
        }
    }

    private static void addConditionRules(GroupRules groupRules, Element element) {
        List conditionRules = groupRules.getConditionRules();
        for (int i = 0; i < conditionRules.size(); i++) {
            ConditionRule rule = (ConditionRule)conditionRules.get(i);
            Element ruleElement = new Element(CONDITION_RULE);
            ruleElement.setAttribute(RESOURCE, rule.getResource());
            ruleElement.setAttribute(CLASS, rule.getRetsClass());
            Element sqlConstraintElement = new Element(SQL_CONSTRAINT);
            sqlConstraintElement.setText(rule.getSqlConstraint());
            ruleElement.addContent(sqlConstraintElement);
            element.addContent(ruleElement);
        }
    }

    private static Element addChild(Element element, String name, boolean bool) {
        return element.addContent(new Element(name).setText(Util.toString(bool)));
    }

    private static Element addChild(Element element, String name, int number) {
        return element.addContent(new Element(name).setText(Integer.toString(number)));
    }

    protected static Element addChild(Element element, String name, String text) {
        return element.addContent(new Element(name).setText(text));
    }

    public static void toXml(RetsConfig retsConfig, String file) throws RetsServerException {
        try {
            IOUtils.writeString(toXml(retsConfig), file);
        } catch (IOException e) {
            throw new RetsServerException(e);
        }
    }

    public static RetsConfig initFromXml(String xml) throws RetsServerException {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(new StringReader(xml));
            return elementToConfig(document.getRootElement());
        } catch (JDOMException e) {
            throw new RetsServerException(e);
        } catch (IOException e) {
            throw new RetsServerException(e);
        }
    }

    private static RetsConfig elementToConfig(Element element) {
        RetsConfig config = new RetsConfig();
        config.setAddress(getString(element, ADDRESS));
        config.setPort(getInt(element, PORT));
        config.setMetadataDir(getString(element, METADATA_DIR));
        String photoPattern = getString(element, PHOTO_PATTERN);
        String getObjectPattern = getString(element, GET_OBJECT_PATTERN);
        if (StringUtils.isNotBlank(photoPattern)) {
            config.setPhotoPattern(photoPattern);
        } else {
            config.setPhotoPattern(getObjectPattern);
        }
        config.setGetObjectRoot(getString(element, GET_OBJECT_ROOT));
        config.setObjectSetPattern(getString(element, OBJECT_SET_PATTERN));
        config.setNonceInitialTimeout(getInt(element, NONCE_INITIAL_TIMEOUT));
        config.setNonceSuccessTimeout(getInt(element, NONCE_SUCCESS_TIMEOUT));
        config.setStrictParsing(getBoolean(element, STRICT_PARSING));

        elementToDatabaseConfig(element.getChild(DATABASE), config);
        elementToSecurityConstraints(element.getChild(SECURITY_CONSTRAINTS), config);

        return config;
    }

    private static void elementToDatabaseConfig(Element element, RetsConfig config) {
        DatabaseConfig database = new DatabaseConfig();
        database.setDatabaseType(DatabaseType.getType(getString(element, TYPE)));
        database.setHostName(getString(element, HOST));
        database.setDatabaseName(getString(element, NAME));
        database.setUsername(getString(element, USERNAME));
        database.setPassword(getString(element, PASSWORD));
        database.setMaxActive(getInt(element, MAX_ACTIVE));
        database.setMaxIdle(getInt(element, MAX_IDLE));
        database.setMaxWait(getInt(element, MAX_WAIT));
        database.setMaxPsActive(getInt(element, MAX_PS_ACTIVE));
        database.setMaxPsIdle(getInt(element, MAX_PS_IDLE));
        database.setMaxPsWait(getInt(element, MAX_PS_WAIT));
        database.setShowSql(getBoolean(element, SHOW_SQL));
        config.setDatabase(database);
    }

    private static void elementToSecurityConstraints(Element element, RetsConfig config) {
        if (element == null) {
            config.setSecurityConstraints(new ArrayList());
            return;
        }
        List securityConstraints = new ArrayList();
        List children = element.getChildren(GROUP_RULES);
        for (int i = 0; i < children.size(); i++) {
            Element child = (Element)children.get(i);
            String groupName = child.getAttributeValue(GROUP);
            GroupRules groupRules = new GroupRules(groupName);
            List grandChildren = child.getChildren();
            for (int j = 0; j < grandChildren.size(); j++) {
                Element grandChild = (Element)grandChildren.get(j);
                String ruleName = grandChild.getName();
                if (ruleName.equals(INCLUDE_RULE)) {
                    addFilterRule(groupRules, FilterRule.INCLUDE, grandChild);
                } else if (ruleName.equals(EXCLUDE_RULE)) {
                    addFilterRule(groupRules, FilterRule.EXCLUDE, grandChild);
                } else if (ruleName.equals(CONDITION_RULE)) {
                    addConditionRule(groupRules, grandChild);
                } else if (ruleName.equals(RECORD_LIMIT)) {
                    groupRules.setRecordLimit(NumberUtils.toInt(grandChild.getTextTrim()));
                } else if (ruleName.equals(TIME_RESTRICTION)) {
                    createAndAddTimeRestriction(groupRules, grandChild);
                } else if (ruleName.equals(QUERY_COUNT_LIMIT)) {
                    addQueryCountToRules(groupRules, grandChild);
                } else {
                    LOG.warn("Unknown rule " + grandChild.toString());
                    continue;
                }
            }
            securityConstraints.add(groupRules);
        }
        config.setSecurityConstraints(securityConstraints);
    }

    private static void addQueryCountToRules(GroupRules groupRules, Element element) {
        String periodString = element.getAttributeValue(PERIOD);
        String limitString = element.getTextTrim();
        try {
            long limit = Long.parseLong(limitString);
            if (limit <= 0) {
                LOG.warn("Query count must be > 0: " + limit);
                return;
            }

            QueryCount.LimitPeriod limitPeriod;
            if (periodString.equals(PER_DAY)) {
                limitPeriod = QueryCount.PER_DAY;
            } else if (periodString.equals(PER_HOUR)) {
                limitPeriod = QueryCount.PER_HOUR;
            } else if (periodString.equals(PER_MINUTE)) {
                limitPeriod = QueryCount.PER_MINUTE;
            } else {
                LOG.warn("Invalid period string (must be one of 'per-day', " + "'per-hour', or 'per-minute'): " + periodString);
                return;
            }
            groupRules.setQueryCountLimit(limit, limitPeriod);
        } catch (NumberFormatException e) {
            LOG.warn("Query count limit must be a number: " + limitString);
            return;
        }
    }

    private static void createAndAddTimeRestriction(GroupRules groupRules, Element element) {
        String policyString = element.getAttributeValue(POLICY);
        String startString = element.getAttributeValue(START);
        String endString = element.getAttributeValue(END);

        TimeRestriction.Policy policy;
        if (policyString.equals("allow")) {
            policy = TimeRestriction.ALLOW;
        } else if (policyString.equals("deny")) {
            policy = TimeRestriction.DENY;
        } else {
            LOG.warn("Unknown time restriction policy: " + policyString);
            return;
        }
        Calendar start = timeStringToCalendar(startString);
        if (start == null) {
            LOG.warn("Unparseable start time: " + startString);
            return;
        }
        Calendar end = timeStringToCalendar(endString);
        if (end == null) {
            LOG.warn("Unparseable end time: " + endString);
        }

        TimeRestriction timeRestriction = new TimeRestriction(policy, start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE), end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE));
        groupRules.setTimeRestriction(timeRestriction);
    }

    private static Calendar timeStringToCalendar(String string) {
        DateFormat parser = DateFormat.getTimeInstance(DateFormat.SHORT);
        try {
            parser.parse(string);
            return parser.getCalendar();
        } catch (ParseException e) {
            return null;
        }
    }

    private static void addFilterRule(GroupRules groupRules, FilterRule.Type ruleType, Element element) {
        FilterRule rule = new FilterRule(ruleType);
        rule.setResource(element.getAttributeValue(RESOURCE));
        rule.setRetsClass(element.getAttributeValue(CLASS));
        String systemNamesText = element.getChildTextNormalize(SYSTEM_NAMES);
        String[] systemNamesArray = StringUtils.split(systemNamesText, " ");
        rule.setSystemNames(Arrays.asList(systemNamesArray));
        groupRules.addFilterRule(rule);
    }

    private static void addConditionRule(GroupRules groupRules, Element element) {
        ConditionRule rule = new ConditionRule();
        rule.setResource(element.getAttributeValue(RESOURCE));
        rule.setRetsClass(element.getAttributeValue(CLASS));
        rule.setSqlConstraint(element.getChildTextNormalize(SQL_CONSTRAINT));
        groupRules.addConditionRule(rule);
    }

    private static boolean getBoolean(Element element, String name) {
        if (element == null) {
            return false;
        } else {
            String text = element.getChildTextTrim(name);
            return BooleanUtils.toBoolean(text);
        }
    }

    private static String getString(Element element, String name) {
        if (element == null) {
            return null;
        } else {
            return element.getChildTextTrim(name);
        }
    }

    private static int getInt(Element element, String name) {
        if (element == null) {
            return -1;
        } else {
            String text = element.getChildTextTrim(name);
            return NumberUtils.toInt(text, -1);
        }
    }

    private static final Logger LOG = Logger.getLogger(XmlRetsConfigUtils.class);
    private static final String ADDRESS = "address";
    private static final String PORT = "port";
    private static final String METADATA_DIR = "metadata-dir";
    private static final String RETS_CONFIG = "rets-config";
    private static final String GET_OBJECT_ROOT = "get-object-root";
    private static final String GET_OBJECT_PATTERN = "get-object-pattern";
    private static final String OBJECT_SET_PATTERN = "object-set-pattern";
    private static final String PHOTO_PATTERN = "photo-pattern";
    private static final String NONCE_INITIAL_TIMEOUT = "nonce-initial-timeout";
    private static final String NONCE_SUCCESS_TIMEOUT = "nonce-success-timeout";
    private static final String TYPE = "type";
    private static final String HOST = "host";
    private static final String NAME = "name";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MAX_ACTIVE = "max-active";
    private static final String MAX_IDLE = "max-idle";
    private static final String MAX_WAIT = "max-wait";
    private static final String MAX_PS_ACTIVE = "max-ps-active";
    private static final String MAX_PS_IDLE = "max-ps-idle";
    private static final String MAX_PS_WAIT = "max-ps-wait";
    private static final String DATABASE = "database";
    private static final String SHOW_SQL = "show-sql";
    private static final String SECURITY_CONSTRAINTS = "security-constraints";
    private static final String GROUP_RULES = "group-rules";
    private static final String GROUP = "group";
    private static final String INCLUDE_RULE = "include-rule";
    private static final String EXCLUDE_RULE = "exclude-rule";
    private static final String RESOURCE = "resource";
    private static final String CLASS = "class";
    private static final String SYSTEM_NAMES = "system-names";
    private static final String CONDITION_RULE = "condition-rule";
    private static final String SQL_CONSTRAINT = "sql-constraint";
    private static final String RECORD_LIMIT = "record-limit";
    private static final String TIME_RESTRICTION = "time-restriction";
    private static final String POLICY = "policy";
    private static final String START = "start";
    private static final String END = "end";
    private static final String QUERY_COUNT_LIMIT = "query-count-limit";
    private static final String PERIOD = "period";
    private static final String PER_DAY = "per-day";
    private static final String PER_HOUR = "per-hour";
    private static final String PER_MINUTE = "per-minute";
    private static final String STRICT_PARSING = "strict-parsing";
}
