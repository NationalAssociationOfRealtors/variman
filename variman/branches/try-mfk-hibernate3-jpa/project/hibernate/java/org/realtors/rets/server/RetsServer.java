/*
 * Variman RETS Server
 *
 * Author: Dave Dribin, Mark Klein
 * Copyright (c) 2004-2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import java.util.Map;

import javax.persistence.*;

import org.apache.log4j.Logger;

import org.realtors.rets.server.config.SecurityConstraints;
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
    public static void setEntityManagerFactory(EntityManagerFactory emFactory)
    {
        setEntityManagerFactory(emFactory, null);
    }
    
    public static void setEntityManagerFactory(EntityManagerFactory emFactory, Map properties)
    {
        sEntityManager = emFactory;
        sEntityManagerProperties = properties;
    }

    public static EntityManagerFactory getEntityManagerFactory()
    {
        return sEntityManager;
    }

    public static ConnectionHelper createHelper()
    {
        return new EntityManagerHelper(sEntityManager);
    }

    public static EntityManagerHelper createEntityManagerHelper()
    {
        return new EntityManagerHelper(sEntityManager);
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

    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException
    {
        sApplicationContext = applicationContext;
    }

    public static SearchTransaction createSearchTransaction()
    {
        return (SearchTransaction)
            sApplicationContext.getBean("SearchTransaction");
    }
    
    public static ObjectSet createCustomObjectSet()
    {
        try
        {
            return (ObjectSet)
                sApplicationContext.getBean("CustomObjectSet");
        }
        catch (NoSuchBeanDefinitionException e)
        {
            LOG.debug("CustomObjectSet bean not found.");
            return null;
        }
    }

    private static final Logger LOG =
        Logger.getLogger(RetsServer.class);
    private static EntityManagerFactory sEntityManager;
    private static Map sEntityManagerProperties;
    
    private static TableGroupFilter sTableGroupFilter;
    private static ConditionRuleSet sConditionRuleSet;
    private static SecurityConstraints sSecurityConstraints;
    private static QueryCountTable sQueryCountTable;
    private static ApplicationContext sApplicationContext;
}
