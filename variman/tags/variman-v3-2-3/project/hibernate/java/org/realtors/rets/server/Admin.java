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

import java.io.File;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class Admin
{
    public Admin() throws HibernateException
    {
        initHibernate();
    }

    private void initHibernate() throws HibernateException
    {
        mCfg = new Configuration();
        File mappingJar = new File("variman-hbm-xml.jar");
        mCfg.addJar(mappingJar);
        mSessions = mCfg.buildSessionFactory();
    }

    public void generateSchema() throws HibernateException
    {
        new SchemaExport(mCfg).create(true, true);
    }

    public void doIt()
    {
        SessionHelper helper = new SessionHelper(mSessions);
        try
        {
            Session session = helper.beginTransaction();
            String hql = "from User u where u.username = 'Joe'";
            Query query = session.createQuery(hql);
            List users = query.list();
            if (users.size() == 0)
            {
                User joe = new User();
                joe.setFirstName("Joe T.");
                joe.setLastName("Schmoe");
                joe.setUsername("Joe");
                joe.changePassword("Schmoe");
                Long id = (Long) session.save(joe);
                System.out.println("New id: " + id);
            }

            hql = "from User u where u.username = 'aphex'";
            query = session.createQuery(hql);
            users = query.list();
            if (users.size() == 0)
            {
                User aphex = new User();
                aphex.setFirstName("Aphex");
                aphex.setLastName("Twin");
                aphex.setUsername("aphex");
                aphex.changePassword("warp");
                Long id = (Long) session.save(aphex);
                System.out.println("New id: " + id);
            }

            hql = "from User";
            query = session.createQuery(hql);
            users = query.list();
            for (int i = 0; i < users.size(); i++)
            {
                User user = (User) users.get(i);
                System.out.println(user);
            }
            helper.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            helper.rollback(System.err);
        }
        finally
        {
            helper.close(System.err);
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
