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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
// import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

// import org.realtors.rets.server.metadata.Table;

import org.apache.commons.lang.StringUtils;

/**
 * @author kgarner
 *
 */
public class DataBenchmarks extends DataGenBase
{
    /**
     * 
     */
    public DataBenchmarks() throws HibernateException
    {
        super();
    }
 
    public void getData() throws HibernateException
    {
        Session session = mSessions.openSession();
        Transaction tx = session.beginTransaction();

        Iterator i = session.iterate("SELECT data.id" +
                                     "  FROM RetsData data");

        int count = 0;
        int batchCount = 0;
        List idList = new ArrayList();
        while (i.hasNext())
        {
            Long id = (Long) i.next();
            idList.add(id);
            count++;
            if (count == 100)
            {
                long start = System.currentTimeMillis();
                getFromIds(session, idList);
                System.out.println("Get ids: " +
                                   (System.currentTimeMillis() - start));
                count = 0;
                idList.clear();
                batchCount++;
            }
        }
        System.out.println("Batch count = " + batchCount);

        tx.commit();
        session.close();
    }

    private void getFromIds(Session session, List idList)
        throws HibernateException
    {
        String ids = StringUtils.join(idList.iterator(), ", ");
        Transaction tx = session.beginTransaction();
        List l = session.find("SELECT data FROM RetsData data " +
                              "WHERE data.id in (" + ids + ")");
        for (int i = 0; i < l.size(); i++)
        {
//            RetsData element = (RetsData) l.get(i);
//            doSomething(element);
        }
        tx.commit();
    }

    public void getData3() throws HibernateException
    {
        Session session = mSessions.openSession();
        Transaction tx = session.beginTransaction();

        long start = System.currentTimeMillis();
        List l = session.find("SELECT data" +
                                  "  FROM RetsData data");
        System.out.println("Time: " + (System.currentTimeMillis() - start));

        tx.commit();
        session.close();
    }

    public void getData2() throws HibernateException
    {
        Session session = mSessions.openSession();
        Transaction tx = session.beginTransaction();

        Query q =
            session.createQuery(
                "SELECT data" +
                "  FROM org.realtors.rets.server.data.RetsData data");

        Iterator i = q.iterate();
        while (i.hasNext())
        {
//            RetsData element = (RetsData) i.next();
//            doSomething(element);
        }

        tx.commit();
        session.close();
    }

//    private void doSomething(RetsData element)
//    {
//        Map elements = element.getDataElements();
//        RetsDataElement rde =
//            (RetsDataElement) elements.get(
//                (Table) mTables.get("Property:RES:LP"));
//    }

    public static void main(String[] args) throws HibernateException
    {
        DataBenchmarks db = new DataBenchmarks();
        db.loadMetadata();
        
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long total = 0;
        
        String tmp = System.getProperty("test.iterations", "10");
        int iterations = Integer.parseInt(tmp);
        for (int i = 0; i < iterations; i++)
        {
            long before = System.currentTimeMillis();
            db.getData();
            long after = System.currentTimeMillis();
            long diff = after - before;
            total += diff;
            if (diff < min)
            {
                min = diff;
            }
            if (diff > max)
            {
                max = diff;
            }
            System.out.println("Time to read data: " + diff + "ms");
        }
        System.out.println("min time: " + min);
        System.out.println("max time: " + max);
        long avg = total / iterations;
        System.out.println("avg time: " + avg);
    }
}
