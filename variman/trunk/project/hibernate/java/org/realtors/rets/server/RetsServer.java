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
import org.realtors.rets.server.protocol.TableGroupFilter;

public class RetsServer
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

    private static SessionFactory sSessions;
    private static TableGroupFilter sTableGroupFilter;
    private static ConditionRuleSet sConditionRuleSet;
    private static SecurityConstraints sSecurityConstraints;
}
