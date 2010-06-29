/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;


import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.realtors.rets.common.metadata.Metadata;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MSystem;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.config.RetsConfigDao;
import org.realtors.rets.server.config.SecurityConstraints;
import org.realtors.rets.server.metadata.MetadataDao;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.StandardNameDao;
import org.realtors.rets.server.protocol.ConditionRuleSet;
import org.realtors.rets.server.protocol.ObjectSet;
import org.realtors.rets.server.protocol.SearchTransaction;
import org.realtors.rets.server.protocol.TableGroupFilter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RetsServer implements ApplicationContextAware
{
    public static void setSessions(SessionFactory sessionFactory)
    {
        synchronized (sLock) {
            sSessions = sessionFactory;
        }
    }

    public static SessionFactory getSessions()
    {
        synchronized (sLock) {
            if (sSessions == null && sApplicationContext != null)
            {
                try
                {
                    // only lookup from spring if rets-config.xml didn't already load it
                    sSessions = (SessionFactory)sApplicationContext.getBean("sessionFactory", SessionFactory.class);
                }
                catch (NoSuchBeanDefinitionException e)
                {
                    throw new IllegalStateException("No db connection properties or hibernate session factory has been configured via rets-config.xml or via spring config!");
                }
            }
            return sSessions;
        }
    }

    public static RetsConfigDao getRetsConfigDao()
    {
        RetsConfigDao retsConfigDao = null;
        try
        {
            retsConfigDao = (RetsConfigDao)sApplicationContext.getBean("retsConfigDao", RetsConfigDao.class);
        }
        catch (NoSuchBeanDefinitionException e)
        {
            LOG.warn("No rets config dao has been configured via spring config, defaulting to reading rets config xml file.");
        }
        return retsConfigDao;
    }

    public static ConnectionHelper createHelper()
    {
        return new SessionHelper(getSessions());
    }

    public static SessionHelper createSessionHelper()
    {
        return new SessionHelper(getSessions());
    }

    public static void setTableGroupFilter(TableGroupFilter tableGroupFilter)
    {
        synchronized (sLock) {
            sTableGroupFilter = tableGroupFilter;
        }
    }

    public static TableGroupFilter getTableGroupFilter()
    {
        synchronized (sLock) {
            return sTableGroupFilter;
        }
    }

    public static void setConditionRuleSet(ConditionRuleSet conditionRuleSet)
    {
        synchronized (sLock) {
            sConditionRuleSet = conditionRuleSet;
        }
    }

    public static ConditionRuleSet getConditionRuleSet()
    {
        synchronized (sLock) {
            return sConditionRuleSet;
        }
    }

    public static void setSecurityConstraints(
        SecurityConstraints securityConstraints)
    {
        synchronized (sLock) {
            sSecurityConstraints = securityConstraints;
        }
    }

    public static SecurityConstraints getSecurityConstraints() {
        synchronized (sLock) {
            return sSecurityConstraints;
        }
    }

    public static QueryCountTable getQueryCountTable()
    {
        synchronized (sLock) {
            return sQueryCountTable;
        }
    }

    public static void setQueryCountTable(QueryCountTable queryCountTable)
    {
        synchronized (sLock) {
            sQueryCountTable = queryCountTable;
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException
    {
        synchronized (sLock) {
            sApplicationContext = applicationContext;
        }
    }

    public static SearchTransaction createSearchTransaction()
    {
        return (SearchTransaction)
            sApplicationContext.getBean("searchTransaction");
    }
    
    public static ObjectSet createCustomObjectSet()
    {
        try
        {
            return (ObjectSet)
                sApplicationContext.getBean("customObjectSet");
        }
        catch (NoSuchBeanDefinitionException e)
        {
            LOG.debug("customObjectSet bean not found.");
            return null;
        }
    }

    public static void setRetsConfiguration(RetsConfig retsConfig) throws RetsServerException
    {
        synchronized (sLock)
        {
            sRetsConfig = retsConfig;
            setSecurityConstraints(retsConfig.getSecurityConstraints());

            ConditionRuleSet ruleSet = getConditionRuleSet(retsConfig);
            setConditionRuleSet(ruleSet);

            MetadataManager manager = createMetadataManager();
            sMetadataManager = manager;

            TableGroupFilter groupFilter = getTableGroupFilter(retsConfig, manager);
            setTableGroupFilter(groupFilter);
        }
    }

    public static RetsConfig getRetsConfiguration()
    {
        synchronized (sLock)
        {
            return sRetsConfig;
        }
    }

    public static MetadataManager getMetadataManager()
    {
        synchronized (sLock)
        {
            return sMetadataManager;
        }
    }

    private static MetadataManager createMetadataManager() throws RetsServerException
    {
        LOG.debug("Initializing metadata");
        LOG.debug("Creating metadata manager");
        MetadataManager manager = new MetadataManager();
        MetadataDao metadataDao = getMetadataDao();
        Metadata metadata = metadataDao.getMetadata();
        MSystem system = metadata.getSystem();
        manager.addRecursive(system);
        return manager;
    }

    private static ConditionRuleSet getConditionRuleSet(RetsConfig config)
    {
        ConditionRuleSetFactory conditionRuleSetFactory = null;
        try
        {
            conditionRuleSetFactory = (ConditionRuleSetFactory)sApplicationContext.getBean("conditionRuleSetFactory", ConditionRuleSetFactory.class);
        } catch (NoSuchBeanDefinitionException e) {
            throw new IllegalStateException("No conditionRuleSetFactory has been configured via spring config!");
        }

        ConditionRuleSet ruleSet = conditionRuleSetFactory.getConditionRuleSet(config);
        return ruleSet;
    }

    private static TableGroupFilter getTableGroupFilter(RetsConfig config, MetadataManager manager)
    {
        LOG.debug("Initializing group filter");
        TableGroupFilter groupFilter = new TableGroupFilter();
        MSystem system = (MSystem)manager.findUniqueByLevel(MetadataType.SYSTEM.name(), "");
        MResource[] resources = system.getMResources();
        for (MResource resource : resources) {
            String resourceID = resource.getResourceID();
            MClass[] classes = resource.getMClasses();
            for (MClass aClass : classes) {
                String className = aClass.getClassName();
                LOG.debug("Setting tables for " + resourceID + ":" + className);
                Set<MTable> tables = (Set<MTable>)aClass.getChildrenSet(MetadataType.TABLE);
                groupFilter.setTables(resourceID, className, tables);
            }
        }
        List<GroupRules> groupRulesSet = config.getSecurityConstraints().getAllGroupRules();
        for (Iterator<GroupRules> iter = groupRulesSet.iterator(); iter.hasNext(); ) {
            GroupRules rules = iter.next();
            LOG.debug("Adding rules for " + rules.getGroupName());
            groupFilter.addRules(rules);
        }

        return groupFilter;
    }

    public static MetadataDao getMetadataDao()
    {
        MetadataDao metadataDao = null;
        try
        {
            metadataDao = (MetadataDao)sApplicationContext.getBean("metadataDao", MetadataDao.class);
        }
        catch (NoSuchBeanDefinitionException e)
        {
            throw new IllegalStateException("No MetadataDao bean named 'metadataDao' has been configured in spring application context.");
        }
        return metadataDao;
    }
    
    public static StandardNameDao getStandardNameDao()
    {
        StandardNameDao standardNameDao = null;
        try
        {
            standardNameDao = (StandardNameDao)sApplicationContext.getBean("standardNameDao", StandardNameDao.class);
        }
        catch (NoSuchBeanDefinitionException e)
        {
            throw new IllegalStateException("No StandardNameDao bean named 'standardNameDao' has been configured in spring application context.");
        }
        return standardNameDao;        
    }

    private static final Logger LOG =
        Logger.getLogger(RetsServer.class);
    private static SessionFactory sSessions;
    private static TableGroupFilter sTableGroupFilter;
    private static ConditionRuleSet sConditionRuleSet;
    private static SecurityConstraints sSecurityConstraints;
    private static QueryCountTable sQueryCountTable = new QueryCountTable();
    private static ApplicationContext sApplicationContext;
    private static RetsConfig sRetsConfig;
    private static MetadataManager sMetadataManager;
    private static Object sLock = new Object();
}
