/*
 */
package org.realtors.rets.server;

import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.tool.hbm2ddl.SchemaExport;
import net.sf.hibernate.cfg.Configuration;

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
            List joes = session.find("from User u where u.username = 'Joe'");
            if (joes.size() == 0)
            {
                Transaction tx = session.beginTransaction();

                User joe = new User();
                joe.setUsername("Joe");
                joe.changePassword("Schmoe");
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
