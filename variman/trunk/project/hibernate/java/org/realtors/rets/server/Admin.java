/*
 */
package org.realtors.rets.server;

import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.tool.hbm2ddl.SchemaExport;

public class Admin
{
    public Admin() throws HibernateException
    {
        initHibernate();
    }

    private void initHibernate() throws HibernateException
    {
        mCfg = new Configuration();
        mCfg.addJar("retsdb2-hbm-xml.jar");
        mSessions = mCfg.buildSessionFactory();
    }

    public void generateSchema() throws HibernateException
    {
        new SchemaExport(mCfg).create(true, true);
    }

    public void doIt()
    {
        try
        {
            Session session = mSessions.openSession();
            List users = session.find("from User u where u.username = 'Joe'");
            if (users.size() == 0)
            {
                Transaction tx = session.beginTransaction();
                User joe = new User();
                joe.setFirstName("Joe T.");
                joe.setLastName("Schmoe");
                joe.setUsername("Joe");
                joe.changePassword("Schmoe");
                Long id = (Long) session.save(joe);
                System.out.println("New id: " + id);
                tx.commit();
            }

            users = session.find("from User u where u.username = 'aphex'");
            if (users.size() == 0)
            {
                Transaction tx = session.beginTransaction();
                User aphex = new User();
                aphex.setFirstName("Aphex");
                aphex.setLastName("Twin");
                aphex.setUsername("aphex");
                aphex.changePassword("warp");
                Long id = (Long) session.save(aphex);
                System.out.println("New id: " + id);
                tx.commit();
//
//                session.close();
//                session = mSessions.openSession();
            }

            users = session.find("from User");
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
        PasswordMethod.setDefaultMethod(PasswordMethod.DIGEST_A1,
                                        PasswordMethod.RETS_REALM);
        Admin admin = new Admin();
        if ("sch".equals(args[0]))
        {
            admin.generateSchema();
        }
        admin.doIt();
    }

    private SessionFactory mSessions;
    private Configuration mCfg;
}
