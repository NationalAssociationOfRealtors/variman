/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import org.realtors.rets.server.config.SecurityConstraints;
import org.realtors.rets.server.protocol.ConditionRuleSet;
import org.realtors.rets.server.protocol.SearchTransaction;
import org.realtors.rets.server.protocol.TableGroupFilter;
import org.springframework.beans.BeansException;
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
        return sSessions;
    }

    public static SessionHelper createHelper()
    {
        return new SessionHelper(sSessions);
    }

    public static Session openSession() throws HibernateException
    {
        return sSessions.openSession();
    }

    public static SessionHelper createSessionHelper()
    {
        return new SessionHelper(sSessions);
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

    private static SessionFactory sSessions;
    private static TableGroupFilter sTableGroupFilter;
    private static ConditionRuleSet sConditionRuleSet;
    private static SecurityConstraints sSecurityConstraints;
    private static QueryCountTable sQueryCountTable;
    private static ApplicationContext sApplicationContext;
}
