/*
 * Created on Jul 24, 2003
 *
 */
package org.realtors.rets.server.testing;

import java.util.Iterator;
import java.util.Map;

import org.realtors.rets.server.data.RetsData;
import org.realtors.rets.server.data.RetsDataElement;
import org.realtors.rets.server.metadata.Table;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

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

        Query q =
            session.createQuery(
                "SELECT data"
                    + "  FROM org.realtors.rets.server.data.RetsData data");

        Iterator i = q.iterate();
        while (i.hasNext())
        {
            RetsData element = (RetsData) i.next();
            Map elements = element.getDataElements();
            RetsDataElement rde =
                (RetsDataElement) elements.get(
                    (Table) mTables.get("Property:RES:LP"));
        }

        tx.commit();
        session.close();
    }

    public static void main(String[] args) throws HibernateException
    {
        DataBenchmarks db= new DataBenchmarks();
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
