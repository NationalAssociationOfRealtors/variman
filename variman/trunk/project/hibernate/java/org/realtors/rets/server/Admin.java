/*
 */
package org.realtors.rets.server;

import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.cfg.Configuration;

public class Admin
{
    public Admin() throws HibernateException
    {
        initHibernate();
    }

    private void initHibernate() throws HibernateException
    {
        Configuration cfg = new Configuration();
        cfg.addJar("retsdb2-hbm-xml.jar");
        mSessions = cfg.buildSessionFactory();
    }

    public void doIt()
    {
        try
        {
            Session session = mSessions.openSession();
            List joes = session.find("from User u where u.username = 'Joe'");
            if (joes.size() == 0)
            {
                Transaction tx = session.beginTransaction();

                User joe = new User();
                joe.setUsername("Joe");
                joe.setPassword("Schmoe");
                Long id = (Long) session.save(joe);
                tx.commit();
                System.out.println("New id: " + id);
                session.close();
                session = mSessions.openSession();
            }

            List users = session.find("from User");
            for (int i = 0; i < users.size(); i++)
            {
                User user = (User) users.get(i);
                System.out.println(user);
            }
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
        throws Exception
    {
        Admin admin = new Admin();
        admin.doIt();
    }

    private SessionFactory mSessions;
}
