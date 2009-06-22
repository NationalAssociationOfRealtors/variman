/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.apache.log4j.Logger;
import org.realtors.rets.server.config.RetsConfigLoader;
import org.realtors.rets.server.config.SecurityConstraints;
import org.realtors.rets.server.metadata.MetadataLoader;
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
        sSessions = sessionFactory;
    }

    public static SessionFactory getSessions()
    {
        // if a session factory was set by the setSessions method use that one,
        // otherwise use the one configured via Spring.
        if (sSessions != null) {
            return sSessions;
        } else {
            SessionFactory sessionFactory = (SessionFactory)sApplicationContext.getBean("sessionFactory", SessionFactory.class);
            return sessionFactory;
        }
    }

    public static SessionHelper createHelper()
    {
        return new SessionHelper(getSessions());
    }

    public static Session openSession() throws HibernateException
    {
        return getSessions().openSession();
    }

    public static SessionHelper createSessionHelper()
    {
        return new SessionHelper(getSessions());
    }

    public static void setTableGroupFilter(TableGroupFilter tableGroupFilter)
    {
        sTableGroupFilter = tableGroupFilter;
    }

    public static TableGroupFilter getTableGroupFilter()
    {
        return sTableGroupFilter;
    }

    public static void setConditionRuleSet(ConditionRuleSet conditionRuleSet)
    {
        sConditionRuleSet = conditionRuleSet;
    }

    public static ConditionRuleSet getConditionRuleSet()
    {
        return sConditionRuleSet;
    }

    public static void setSecurityConstraints(
        SecurityConstraints securityConstraints)
    {
        sSecurityConstraints = securityConstraints;
    }

    public static SecurityConstraints getSecurityConstraints()
    {
        return sSecurityConstraints;
    }

    public static QueryCountTable getQueryCountTable()
    {
        return sQueryCountTable;
    }

    public static void setQueryCountTable(QueryCountTable queryCountTable)
    {
        sQueryCountTable = queryCountTable;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        sApplicationContext = applicationContext;
    }

    public static SearchTransaction createSearchTransaction()
    {
        SearchTransaction searchTransaction = (SearchTransaction)sApplicationContext.getBean("searchTransaction");
        return searchTransaction;
    }
    
    public static ObjectSet createCustomObjectSet()
    {
        try {
            ObjectSet objectSet = (ObjectSet)sApplicationContext.getBean("customObjectSet");
            return objectSet;
        } catch(NoSuchBeanDefinitionException ex) {
            LOG.debug("customObjectSet bean not found.");
            return null;
        }
    }
    
    public static MetadataLoader getMetadataLoader()
    {
        MetadataLoader metadataLoader = (MetadataLoader)sApplicationContext.getBean("metadataLoader", MetadataLoader.class);
        return metadataLoader;
    }
    
    public static MetadataFetcher getMetadataFetcher()
    {
        MetadataFetcher metadataFetcher = (MetadataFetcher)sApplicationContext.getBean("metadataFetcher", MetadataFetcher.class);
        return metadataFetcher;
    }
    
    public static RetsConfigLoader getRetsConfigLoader()
    {
        RetsConfigLoader retsConfig = (RetsConfigLoader)sApplicationContext.getBean("retsConfigLoader", RetsConfigLoader.class);
        return retsConfig;
    }
    
    private static final Logger LOG = Logger.getLogger(RetsServer.class);
    private static SessionFactory sSessions;
    private static TableGroupFilter sTableGroupFilter;
    private static ConditionRuleSet sConditionRuleSet;
    private static SecurityConstraints sSecurityConstraints;
    private static QueryCountTable sQueryCountTable;
    private static ApplicationContext sApplicationContext;
}
