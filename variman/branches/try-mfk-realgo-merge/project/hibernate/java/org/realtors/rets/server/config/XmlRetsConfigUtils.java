/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
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
import java.util.Iterator;
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
import org.jdom.output.XMLOutputter;
import org.realtors.rets.server.Group;
import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.QueryLimit;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.Util;

public class XmlRetsConfigUtils
{
    private XmlRetsConfigUtils()
    {
        // Prevents instantiation.
    }

    public static RetsConfig initFromXmlFile(String file)
            throws RetsServerException
    {
        try
        {
            return initFromXml(new FileReader(file));
        }
        catch (FileNotFoundException e)
        {
            throw new RetsServerException(e);
        }
    }

    public static RetsConfig initFromXml(Reader xml)
            throws RetsServerException
    {
        try
        {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(xml);
            return elementToConfig(document.getRootElement());
        }
        catch (IOException e)
        {
            throw new RetsServerException(e);
        }
        catch (JDOMException e)
        {
            throw new RetsServerException(e);
        }
    }

    public static RetsConfig initFromXml(String xml) throws RetsServerException
    {
        try
        {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(new StringReader(xml));
            return elementToConfig(document.getRootElement());
        }
        catch (JDOMException e)
        {
            throw new RetsServerException(e);
        }
        catch (IOException e)
        {
            throw new RetsServerException(e);
        }
    }

    public static void toXml(final RetsConfig retsConfig, final String file) throws RetsServerException
    {
        try
        {
            IOUtils.writeString(toXml(retsConfig), file);
        }
        catch (IOException e)
        {
            throw new RetsServerException(e);
        }
    }

    public static String toXml(final RetsConfig retsConfig)
    {
        Element retsCfgElmt = new Element(RETS_CONFIG);
        addChild(retsCfgElmt, ADDRESS, retsConfig.getAddress());
        addChild(retsCfgElmt, PORT, retsConfig.getPort());
        addChild(retsCfgElmt, METADATA_DIR, retsConfig.getMetadataDir());
        addChild(retsCfgElmt, GET_OBJECT_ROOT, retsConfig.getGetObjectRoot());
        addChild(retsCfgElmt, PHOTO_PATTERN, retsConfig.getPhotoPattern());
        addChild(retsCfgElmt, OBJECT_SET_PATTERN, retsConfig.getObjectSetPattern());
        addChild(retsCfgElmt, NONCE_INITIAL_TIMEOUT, retsConfig.getNonceInitialTimeout());
        addChild(retsCfgElmt, NONCE_SUCCESS_TIMEOUT, retsConfig.getNonceSuccessTimeout());
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

        XMLOutputter xmlOutputter = new XMLOutputter("  ", true);
        xmlOutputter.setLineSeparator(SystemUtils.LINE_SEPARATOR);
        return xmlOutputter.outputString(new Document(retsCfgElmt));
    }

    private static Element getSecurityContraintsElement(final RetsConfig retsConfig)
    {
        Element securityContraints = new Element(SECURITY_CONSTRAINTS);
        List allGroupRules = retsConfig.getSecurityConstraints().getAllGroupRules();
        for (Iterator iter = allGroupRules.iterator(); iter.hasNext(); )
        {
            GroupRules groupRules = (GroupRules)iter.next();
            Element groupRulesElement = new Element(GROUP_RULES);
            groupRulesElement.setAttribute(GROUP, groupRules.getGroup().getName());
            addRecordLimit(groupRules, groupRulesElement);
            addFilterRules(groupRules, groupRulesElement);
            addConditionRules(groupRules, groupRulesElement);
            addTimeRestriction(groupRules, groupRulesElement);
            addQueryCountToElement(groupRules, groupRulesElement);
            if (groupRulesElement.getChildren().size() != 0)
            {
                securityContraints.addContent(groupRulesElement);
            }
        }
        return securityContraints;
    }

    private static void addQueryCountToElement(GroupRules groupRules,
            Element groupRulesElement)
    {
        QueryLimit queryLimit = groupRules.getQueryLimit();
        if (queryLimit.hasNoQueryLimit())
            return;
        Element element = new Element(QUERY_COUNT_LIMIT);
        QueryLimit.Period limitPeriod = queryLimit.getPeriod();
        if (limitPeriod.equals(QueryLimit.Period.PER_DAY))
            element.setAttribute(PERIOD, PER_DAY);
        else if (limitPeriod.equals(QueryLimit.Period.PER_HOUR))
            element.setAttribute(PERIOD, PER_HOUR);
        else if (limitPeriod.equals(QueryLimit.Period.PER_MINUTE))
            element.setAttribute(PERIOD, PER_MINUTE);
        else
        {
            LOG.warn("Unknown query count limit period: " + limitPeriod);
            return;
        }
        element.setText("" + queryLimit.getLimit());
        groupRulesElement.addContent(element);
    }

    private static void addTimeRestriction(GroupRules groupRules,
            Element groupRulesElement)
    {
        TimeRestriction timeRestriction = groupRules.getTimeRestriction();
        if (timeRestriction == null)
            return;
        Element element = new Element(TIME_RESTRICTION);
        element.setAttribute(
            POLICY, timeRestriction.getPolicy().getName());
        Date time = timeRestriction.getStartAsCalendar().getTime();
        DateFormat formatter = DateFormat.getTimeInstance(DateFormat.SHORT);
        element.setAttribute(START, formatter.format(time));
        time = timeRestriction.getEndAsCalendar().getTime();
        element.setAttribute(END, formatter.format(time));
        groupRulesElement.addContent(element);
    }

    private static void addRecordLimit(GroupRules groupRules, 
            Element groupRulesElement)
    {
        int recordLimit = groupRules.getRecordLimit();
        if (recordLimit > 0)
        {
            Element recordLimitElement = new Element(RECORD_LIMIT);
            recordLimitElement.setText(Integer.toString(recordLimit));
            groupRulesElement.addContent(recordLimitElement);
        }
    }

    private static void addFilterRules(GroupRules groupRules, Element element)
    {
        List rules = groupRules.getFilterRules();
        for (int i = 0; i < rules.size(); i++)
        {
            FilterRule filterRule = (FilterRule) rules.get(i);
            Element ruleElement;
            if (filterRule.getType() == FilterRule.Type.INCLUDE)
            {
                ruleElement = new Element(INCLUDE_RULE);
            }
            else
            {
                ruleElement = new Element(EXCLUDE_RULE);
            }
            ruleElement.setAttribute(RESOURCE, filterRule.getResourceID());
            ruleElement.setAttribute(CLASS, filterRule.getRetsClassName());
            String systemNames = StringUtils.join(
                filterRule.getSystemNames().iterator(), " ");
            Element systemNamesElement = new Element(SYSTEM_NAMES);
            systemNamesElement.setText(systemNames);
            ruleElement.addContent(systemNamesElement);
            element.addContent(ruleElement);
        }
    }

    private static void addConditionRules(GroupRules groupRules, Element element)
    {
        List conditionRules = groupRules.getConditionRules();
        for (int i = 0; i < conditionRules.size(); i++)
        {
            ConditionRule rule = (ConditionRule) conditionRules.get(i);
            Element ruleElement = new Element(CONDITION_RULE);
            ruleElement.setAttribute(RESOURCE, rule.getResourceID());
            ruleElement.setAttribute(CLASS, rule.getRetsClassName());
            Element sqlConstraintElement = new Element(SQL_CONSTRAINT);
            sqlConstraintElement.setText(rule.getSqlConstraint());
            ruleElement.addContent(sqlConstraintElement);
            Element dmqlConstraintElement = new Element(DMQL_CONSTRAINT);
            dmqlConstraintElement.setText(rule.getDmqlConstraint());
            ruleElement.addContent(dmqlConstraintElement);
            element.addContent(ruleElement);
        }
    }

    private static Element addChild(Element element, String name, boolean bool)
    {
        return element.addContent(
            new Element(name).setText(Util.toString(bool)));
    }

    private static Element addChild(Element element, String name, int number)
    {
        return element.addContent(
            new Element(name).setText(Integer.toString(number)));
    }

    private static Element addChild(Element element, String name, String text)
    {
        return element.addContent(new Element(name).setText(text));
    }

    private static RetsConfig elementToConfig(Element element)
    {
        RetsConfig retsConfig = new RetsConfigImpl();
        retsConfig.setAddress(getString(element, ADDRESS));
        retsConfig.setPort(getInt(element, PORT));
        retsConfig.setMetadataDir(getString(element, METADATA_DIR));
        String photoPattern = getString(element, PHOTO_PATTERN);
        String getObjectPattern = getString(element, GET_OBJECT_PATTERN);
        if (StringUtils.isNotBlank(photoPattern))
        {
            retsConfig.setPhotoPattern(photoPattern);
        }
        else
        {
            retsConfig.setPhotoPattern(getObjectPattern);
        }
        retsConfig.setGetObjectRoot(getString(element, GET_OBJECT_ROOT));
        retsConfig.setObjectSetPattern(getString(element, OBJECT_SET_PATTERN));
        retsConfig.setNonceInitialTimeout(getInt(element, NONCE_INITIAL_TIMEOUT));
        retsConfig.setNonceSuccessTimeout(getInt(element, NONCE_SUCCESS_TIMEOUT));

        elementToDatabaseConfig(element.getChild(DATABASE), retsConfig);
        elementToSecurityConstraints(element.getChild(SECURITY_CONSTRAINTS),
                                     retsConfig);

        return retsConfig;
    }

    private static void elementToDatabaseConfig(Element element,
                                                RetsConfig retsConfig)
    {
        DatabaseConfig databaseConfig = new DatabaseConfig();
        databaseConfig.setDatabaseType(
        DatabaseType.getType(getString(element, TYPE)));
        databaseConfig.setHostName(getString(element, HOST));
        databaseConfig.setDatabaseName(getString(element, NAME));
        databaseConfig.setUsername(getString(element, USERNAME));
        databaseConfig.setPassword(getString(element, PASSWORD));
        databaseConfig.setMaxActive(getInt(element, MAX_ACTIVE));
        databaseConfig.setMaxIdle(getInt(element, MAX_IDLE));
        databaseConfig.setMaxWait(getInt(element, MAX_WAIT));
        databaseConfig.setMaxPsActive(getInt(element, MAX_PS_ACTIVE));
        databaseConfig.setMaxPsIdle(getInt(element, MAX_PS_IDLE));
        databaseConfig.setMaxPsWait(getInt(element, MAX_PS_WAIT));
        databaseConfig.setShowSql(getBoolean(element, SHOW_SQL));
        retsConfig.setDatabase(databaseConfig);
    }

    private static void elementToSecurityConstraints(Element element,
                                                     RetsConfig retsConfig)
    {
        if (element == null)
        {
            retsConfig.setSecurityConstraints(new SecurityConstraints());
            return;
        }
        List/*GroupRules*/ allGroupRules = new ArrayList/*GroupRules*/();
        List children = element.getChildren(GROUP_RULES);
        for (int i = 0; i < children.size(); i++)
        {
            Element child = (Element) children.get(i);
            String groupName = child.getAttributeValue(GROUP);
            GroupRules groupRules = new GroupRulesImpl(new Group(groupName));
            List grandChildren = child.getChildren();
            for (int j = 0; j < grandChildren.size(); j++)
            {
                Element grandChild = (Element) grandChildren.get(j);
                String ruleName = grandChild.getName();
                if (ruleName.equals(INCLUDE_RULE))
                {
                    addFilterRule(groupRules, FilterRule.Type.INCLUDE,
                                  grandChild);
                }
                else if (ruleName.equals(EXCLUDE_RULE))
                {
                    addFilterRule(groupRules, FilterRule.Type.EXCLUDE,
                                  grandChild);
                }
                else if (ruleName.equals(CONDITION_RULE))
                {
                    addConditionRule(groupRules, grandChild);
                }
                else if (ruleName.equals(RECORD_LIMIT))
                {
                    groupRules.setRecordLimit(
                        NumberUtils.toInt(grandChild.getTextTrim()));
                }
                else if (ruleName.equals(TIME_RESTRICTION))
                {
                    createAndAddTimeRestriction(groupRules, grandChild);
                }
                else if (ruleName.equals(QUERY_COUNT_LIMIT))
                {
                    addQueryCountToRules(groupRules, grandChild);
                }
                else
                {
                    LOG.warn("Unknown rule " + grandChild.toString());
                    continue;
                }
            }
            allGroupRules.add(groupRules);
        }
        SecurityConstraints securityConstraints = new SecurityConstraints();
        securityConstraints.setAllGroupRules(allGroupRules);
        retsConfig.setSecurityConstraints(securityConstraints);
    }

    private static void addQueryCountToRules(GroupRules groupRules,
                                             Element element)
    {
        String periodString = element.getAttributeValue(PERIOD);
        String limitString = element.getTextTrim();
        try
        {
            long limit = Long.parseLong(limitString);
            if (limit <= 0)
            {
                LOG.warn("Query count must be > 0: " + limit);
                return;
            }

            QueryLimit.Period limitPeriod;
            if (periodString.equals(PER_DAY))
            {
                limitPeriod = QueryLimit.Period.PER_DAY;
            }
            else if (periodString.equals(PER_HOUR))
            {
                limitPeriod = QueryLimit.Period.PER_HOUR;
            }
            else if (periodString.equals(PER_MINUTE))
            {
                limitPeriod = QueryLimit.Period.PER_MINUTE;
            }
            else
            {
                LOG.warn("Invalid period string (must be one of 'per-day', " +
                         "'per-hour', or 'per-minute'): " + periodString);
                return;
            }
            groupRules.setQueryLimit(QueryLimit.valueOf(limit, limitPeriod));
        }
        catch(NumberFormatException e)
        {
            LOG.warn("Query count limit must be a number: " + limitString);
            return;
        }
    }

    private static void createAndAddTimeRestriction(GroupRules groupRules,
                                                    Element element)
    {
        String policyString = element.getAttributeValue(POLICY);
        String startString = element.getAttributeValue(START);
        String endString  = element.getAttributeValue(END);

        TimeRestriction.Policy policy;
        if (policyString.equals("allow"))
        {
            policy = TimeRestriction.ALLOW;
        }
        else if (policyString.equals("deny"))
        {
            policy = TimeRestriction.DENY;
        }
        else
        {
            LOG.warn("Unknown time restriction policy: " + policyString);
            return;
        }
        Calendar start = timeStringToCalendar(startString);
        if (start == null)
        {
            LOG.warn("Unparseable start time: " + startString);
            return;
        }
        Calendar end = timeStringToCalendar(endString);
        if (end == null)
        {
            LOG.warn("Unparseable end time: " + endString);
        }
        else
        {
            TimeRestriction timeRestriction =
                new TimeRestriction(policy,
                                    start.get(Calendar.HOUR_OF_DAY),
                                    start.get(Calendar.MINUTE),
                                    end.get(Calendar.HOUR_OF_DAY),
                                    end.get(Calendar.MINUTE));
            groupRules.setTimeRestriction(timeRestriction);
        }
    }

    private static Calendar timeStringToCalendar(String string)
    {
        DateFormat parser = DateFormat.getTimeInstance(DateFormat.SHORT);
        try
        {
            parser.parse(string);
            return parser.getCalendar();
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    private static void addFilterRule(GroupRules groupRules,
                                      FilterRule.Type ruleType,
                                      Element element)
    {
        FilterRule rule = new FilterRuleImpl(ruleType);
        rule.setResourceID(element.getAttributeValue(RESOURCE));
        rule.setRetsClassName(element.getAttributeValue(CLASS));
        String systemNamesText = element.getChildTextNormalize(SYSTEM_NAMES);
        String[] systemNamesArray = StringUtils.split(systemNamesText, " ");
        rule.setSystemNames(Arrays.asList(systemNamesArray));
        groupRules.addFilterRule(rule);
    }

    private static void addConditionRule(GroupRules groupRules, Element element)
    {
        ConditionRule rule = new ConditionRuleImpl();
        rule.setResourceID(element.getAttributeValue(RESOURCE));
        rule.setRetsClassName(element.getAttributeValue(CLASS));
        String sqlConstraint = element.getChildTextNormalize(SQL_CONSTRAINT);
        if (!StringUtils.isBlank(sqlConstraint)) {
            rule.setSqlConstraint(sqlConstraint);
        }
        String dmqlConstraint = element.getChildTextNormalize(DMQL_CONSTRAINT);
        if (!StringUtils.isBlank(dmqlConstraint)) {
            rule.setDmqlConstraint(dmqlConstraint);
        }
        groupRules.addConditionRule(rule);
    }

    private static boolean getBoolean(Element element, String name)
    {
        if (element == null)
        {
            return false;
        }
        else
        {
            String text = element.getChildTextTrim(name);
            return BooleanUtils.toBoolean(text);
        }
    }

    private static String getString(Element element, String name)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            return element.getChildTextTrim(name);
        }
    }

    private static int getInt(Element element, String name)
    {
        if (element == null)
        {
            return -1;
        }
        else
        {
            String text = element.getChildTextTrim(name);
            return NumberUtils.toInt(text, -1);
        }
    }

    public static final String ADDRESS = "address";
    public static final String PORT = "port";
    public static final String METADATA_DIR = "metadata-dir";
    public static final String RETS_CONFIG = "rets-config";
    public static final String GET_OBJECT_ROOT = "get-object-root";
    public static final String GET_OBJECT_PATTERN = "get-object-pattern";
    public static final String OBJECT_SET_PATTERN = "object-set-pattern";
    public static final String PHOTO_PATTERN = "photo-pattern";
    public static final String NONCE_INITIAL_TIMEOUT = "nonce-initial-timeout";
    public static final String NONCE_SUCCESS_TIMEOUT = "nonce-success-timeout";
    public static final String TYPE = "type";
    public static final String HOST = "host";
    public static final String NAME = "name";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String MAX_ACTIVE = "max-active";
    public static final String MAX_IDLE = "max-idle";
    public static final String MAX_WAIT = "max-wait";
    public static final String MAX_PS_ACTIVE = "max-ps-active";
    public static final String MAX_PS_IDLE = "max-ps-idle";
    public static final String MAX_PS_WAIT = "max-ps-wait";
    public static final String DATABASE = "database";
    public static final String SHOW_SQL = "show-sql";
    public static final String SECURITY_CONSTRAINTS = "security-constraints";
    public static final String GROUP_RULES = "group-rules";
    public static final String GROUP = "group";
    public static final String INCLUDE_RULE = "include-rule";
    public static final String EXCLUDE_RULE = "exclude-rule";
    public static final String RESOURCE = "resource";
    public static final String CLASS = "class";
    public static final String SYSTEM_NAMES = "system-names";
    public static final String CONDITION_RULE = "condition-rule";
    public static final String SQL_CONSTRAINT = "sql-constraint";
    public static final String DMQL_CONSTRAINT = "dmql-constraint";
    public static final String RECORD_LIMIT = "record-limit";
    public static final String TIME_RESTRICTION = "time-restriction";
    public static final String POLICY = "policy";
    public static final String START = "start";
    public static final String END = "end";
    public static final String QUERY_COUNT_LIMIT = "query-count-limit";
    public static final String PERIOD = "period";
    public static final String PER_DAY = "per-day";
    public static final String PER_HOUR = "per-hour";
    public static final String PER_MINUTE = "per-minute";
    
    private static final Logger LOG = Logger.getLogger(XmlRetsConfigUtils.class);
}
