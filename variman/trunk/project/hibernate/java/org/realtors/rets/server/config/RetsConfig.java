/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.Util;

public class RetsConfig
{
    public RetsConfig()
    {
        mPort = 6103;
        mNonceInitialTimeout = -1;
        mNonceSuccessTimeout = -1;
        mSecurityConstraints = new SecurityConstraints();
    }

    public int getPort()
    {
        return mPort;
    }

    public void setPort(int port)
    {
        mPort = port;
    }

    public String getGetObjectRoot()
    {
        return mGetObjectRoot;
    }

    public void setGetObjectRoot(String getObjectRoot)
    {
        mGetObjectRoot = getObjectRoot;
    }

    public String getGetObjectPattern()
    {
        return mGetObjectPattern;
    }

    public void setGetObjectPattern(String getObjectPattern)
    {
        mGetObjectPattern = getObjectPattern;
    }

    public int getNonceInitialTimeout()
    {
        return mNonceInitialTimeout;
    }

    public void setNonceInitialTimeout(int nonceInitialTimeout)
    {
        mNonceInitialTimeout = nonceInitialTimeout;
    }

    public int getNonceSuccessTimeout()
    {
        return mNonceSuccessTimeout;
    }

    public void setNonceSuccessTimeout(int nonceSuccessTimeout)
    {
        mNonceSuccessTimeout = nonceSuccessTimeout;
    }

    public DatabaseConfig getDatabase()
    {
        return mDatabase;
    }

    public void setDatabase(DatabaseConfig database)
    {
        mDatabase = database;
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("GetObject root", mGetObjectRoot)
            .append("GetObject pattern", mGetObjectPattern)
            .append("nonce initial timeout", mNonceInitialTimeout)
            .append("nonce success timeout", mNonceSuccessTimeout)
            .append(mDatabase)
            .toString();
    }

    public String toXml() throws RetsServerException
    {
        Element retsConfig = new Element(RETS_CONFIG);
        addChild(retsConfig, PORT, mPort);
        addChild(retsConfig, METADATA_DIR, mMetadataDir);
        addChild(retsConfig, GET_OBJECT_ROOT, mGetObjectRoot);
        addChild(retsConfig, GET_OBJECT_PATTERN, mGetObjectPattern);
        addChild(retsConfig, NONCE_INITIAL_TIMEOUT, mNonceInitialTimeout);
        addChild(retsConfig, NONCE_SUCCESS_TIMEOUT, mNonceSuccessTimeout);
        Element database = new Element(DATABASE);
        addChild(database, TYPE, mDatabase.getDatabaseType().getName());
        addChild(database, HOST, mDatabase.getHostName());
        addChild(database, NAME, mDatabase.getDatabaseName());
        addChild(database, USERNAME, mDatabase.getUsername());
        addChild(database, PASSWORD, mDatabase.getPassword());
        addChild(database, MAX_ACTIVE, mDatabase.getMaxActive());
        addChild(database, MAX_IDLE, mDatabase.getMaxIdle());
        addChild(database, MAX_WAIT, mDatabase.getMaxWait());
        addChild(database, MAX_PS_ACTIVE, mDatabase.getMaxPsActive());
        addChild(database, MAX_PS_IDLE, mDatabase.getMaxPsIdle());
        addChild(database, MAX_PS_WAIT, mDatabase.getMaxPsWait());
        addChild(database, SHOW_SQL, mDatabase.getShowSql());
        retsConfig.addContent(database);
        retsConfig.addContent(getSecurityContraintsElement());

        XMLOutputter xmlOutputter = new XMLOutputter("  ", true);
        xmlOutputter.setLineSeparator(SystemUtils.LINE_SEPARATOR);
        return xmlOutputter.outputString(new Document(retsConfig));
    }

    private Element getSecurityContraintsElement()
    {
        Element securityContraints = new Element(SECURITY_CONSTRAINTS);
        List allGroupRules = mSecurityConstraints.getAllGroupRules();
        for (int i = 0; i < allGroupRules.size(); i++)
        {
            GroupRules groupRules = (GroupRules) allGroupRules.get(i);
            List rules = groupRules.getRules();
            if (rules.size() == 0)
            {
                continue;
            }
            Element groupRulesElement = new Element(GROUP_RULES);
            groupRulesElement.setAttribute(GROUP, groupRules.getGroupName());
            for (int j = 0; j < rules.size(); j++)
            {
                RuleDescription ruleDescription =
                    (RuleDescription) rules.get(j);
                Element ruleElement;
                if (ruleDescription.getType() == RuleDescription.INCLUDE)
                {
                    ruleElement = new Element(INCLUDE_RULE);
                }
                else
                {
                    ruleElement = new Element(EXCLUDE_RULE);
                }
                ruleElement.setAttribute(RESOURCE,
                                         ruleDescription.getResource());
                ruleElement.setAttribute(CLASS,
                                         ruleDescription.getRetsClass());
                String systemNames = StringUtils.join(
                    ruleDescription.getSystemNames().iterator(), " ");
                Element systemNamesElement = new Element(SYSTEM_NAMES);
                systemNamesElement.setText(systemNames);
                ruleElement.addContent(systemNamesElement);
                groupRulesElement.addContent(ruleElement);
            }
            securityContraints.addContent(groupRulesElement);
        }
        return securityContraints;
    }

    private Element addChild(Element element, String name, boolean bool)
    {
        return element.addContent(
            new Element(name).setText(Util.toString(bool)));
    }

    private Element addChild(Element element, String name, int number)
    {
        return element.addContent(
            new Element(name).setText(Integer.toString(number)));
    }

    private Element addChild(Element element, String name, String text)
    {
        return element.addContent(new Element(name).setText(text));
    }

    public void toXml(String file) throws RetsServerException
    {
        try
        {
            IOUtils.writeString(toXml(), file);
        }
        catch (IOException e)
        {
            throw new RetsServerException(e);
        }
    }

    public static RetsConfig initFromXml(String xml)
        throws RetsServerException
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

    private static RetsConfig elementToConfig(Element element)
    {
        RetsConfig config = new RetsConfig();
        config.mPort = getInt(element, PORT);
        config.mMetadataDir = getString(element, METADATA_DIR);
        config.mGetObjectPattern = getString(element, GET_OBJECT_PATTERN);
        config.mGetObjectRoot = getString(element, GET_OBJECT_ROOT);
        config.mNonceInitialTimeout = getInt(element, NONCE_INITIAL_TIMEOUT);
        config.mNonceSuccessTimeout = getInt(element, NONCE_SUCCESS_TIMEOUT);

        elementToDatabaseConfig(element.getChild(DATABASE), config);
        elementToSecurityConstraints(element.getChild(SECURITY_CONSTRAINTS),
                                     config);

        return config;
    }

    private static void elementToDatabaseConfig(Element element,
                                                RetsConfig config)
    {
        DatabaseConfig database = new DatabaseConfig();
        database.setDatabaseType(
            DatabaseType.getType(getString(element, TYPE)));
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

    private static void elementToSecurityConstraints(Element element,
                                                     RetsConfig config)
    {
        if (element == null)
        {
            config.setSecurityConstraints(new ArrayList());
            return;
        }
        List securityConstraints = new ArrayList();
        List children = element.getChildren(GROUP_RULES);
        for (int i = 0; i < children.size(); i++)
        {
            Element child = (Element) children.get(i);
            String groupName = child.getAttributeValue(GROUP);
            GroupRules groupRules = new GroupRules(groupName);
            List grandChildren = child.getChildren();
            for (int j = 0; j < grandChildren.size(); j++)
            {
                Element grandChild = (Element) grandChildren.get(j);
                RuleDescription ruleDescription = new RuleDescription();
                if (grandChild.getName().equals(INCLUDE_RULE))
                {
                    ruleDescription.setType(RuleDescription.INCLUDE);
                }
                else if (grandChild.getName().equals(EXCLUDE_RULE))
                {
                    ruleDescription.setType(RuleDescription.EXCLUDE);
                }
                else
                {
                    LOG.warn("Unknown rule" + grandChild.toString());
                    continue;
                }
                ruleDescription.setResource(
                    grandChild.getAttributeValue(RESOURCE));
                ruleDescription.setRetsClass(
                    grandChild.getAttributeValue(CLASS));
                String systemNamesText =
                    grandChild.getChildTextNormalize(SYSTEM_NAMES);
                String[] systemNamesArray =
                    StringUtils.split(systemNamesText, " ");
                ruleDescription.setSystemNames(Arrays.asList(systemNamesArray));
                groupRules.addRule(ruleDescription);
            }
            securityConstraints.add(groupRules);
        }
        config.setSecurityConstraints(securityConstraints);
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
            return NumberUtils.stringToInt(text, -1);
        }
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

    public String getGetObjectPattern(String defaultValue)
    {
        return getDefault(mGetObjectPattern, defaultValue);
    }

    public String  getGetObjectRoot(String defaultValue)
    {
        return getDefault(mGetObjectRoot, defaultValue);
    }

    public int getNonceInitialTimeout(int defaultValue)
    {
        return getDefault(mNonceInitialTimeout, defaultValue);
    }

    public int getNonceSuccessTimeout(int defaultValue)
    {
        return getDefault(mNonceSuccessTimeout, defaultValue);
    }

    private String getDefault(String string, String defaultValue)
    {
        if (string == null)
        {
            return defaultValue;
        }
        else
        {
            return string;
        }
    }

    private int getDefault(int number, int defaultValue)
    {
        if (number == -1)
        {
            return defaultValue;
        }
        else
        {
            return number;
        }
    }

    public Properties createHibernateProperties()
    {
        return mDatabase.createHibernateProperties();
    }

    public void setMetadataDir(String metadataDir)
    {
        mMetadataDir = metadataDir;
    }

    public String getMetadataDir()
    {
        return mMetadataDir;
    }

    public String getMetadataDir(String defaultValue)
    {
        return getDefault(mMetadataDir, defaultValue);
    }

    public List getAllGroupRules()
    {
        return mSecurityConstraints.getAllGroupRules();
    }

    public SecurityConstraints getSecurityConstraints()
    {
        return mSecurityConstraints;
    }

    public void setSecurityConstraints(List securityConstraints)
    {
        mSecurityConstraints.setAllConstraints(securityConstraints);
    }

    private static final Logger LOG =
        Logger.getLogger(RetsConfig.class);
    private static final String PORT = "port";
    private static final String METADATA_DIR = "metadata-dir";
    private static final String RETS_CONFIG = "rets-config";
    private static final String GET_OBJECT_PATTERN = "get-object-pattern";
    private static final String GET_OBJECT_ROOT = "get-object-root";
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

    private int mPort;
    private String mGetObjectRoot;
    private String mGetObjectPattern;
    private int mNonceInitialTimeout;
    private int mNonceSuccessTimeout;
    private DatabaseConfig mDatabase;
    private String mMetadataDir;
//    private List mSecurityConstraints;
    private SecurityConstraints mSecurityConstraints;
}
