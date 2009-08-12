/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 * Created on Jul 24, 2003
 *
 */
package org.realtors.rets.server.testing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.Table;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * @author kgarner
 *
 */
public abstract class DataGenBase
{
    protected DataGenBase() throws HibernateException
    {
        initHibernate();
        mClasses = new HashMap();
        mTables = new HashMap();
    }

    private void initHibernate() throws HibernateException
    {
        Configuration cfg = new Configuration();
        cfg.addJar("rex-hbm-xml.jar");
        mSessions = cfg.buildSessionFactory();
    }

    public void loadMetadata() throws HibernateException
    {
        Session session = mSessions.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery(
                "SELECT system" +
                "  FROM org.realtors.rets.server.metadata.MSystem system");
        Iterator i = query.iterate();
        while (i.hasNext())
        {
            MSystem system = (MSystem) i.next();
            System.out.println("Got system" + system.getId());
            Iterator j = system.getResources().iterator();
            while (j.hasNext())
            {
                Resource res = (Resource) j.next();
                Iterator k = res.getClasses().iterator();
                while (k.hasNext())
                {
                    MClass clazz = (MClass) k.next();
                    mClasses.put(clazz.getPath(), clazz);
                    Iterator l = clazz.getTables().iterator();
                    while (l.hasNext())
                    {
                        Table table = (Table) l.next();
                        mTables.put(table.getPath(), table);
                    }
                }
            }
        }
        tx.commit();
        session.close();
    }

    protected Map mClasses;
    protected SessionFactory mSessions;
    protected Map mTables;
}
