/*
 * Created on Aug 25, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import java.util.List;

import org.apache.log4j.Logger;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.realtors.rets.server.User;
import org.realtors.rets.server.cct.UserInfo;
import org.realtors.rets.server.webapp.InitServlet;

/**
 * @author kgarner
 */
public class UserUtils
{
    public void createUser(User user, UserInfo userInfo)
    {
        Session session = null;
        Transaction tx = null;
        try
        {
            session = InitServlet.getSessions().openSession();
            tx = session.beginTransaction();

            session.save(user);
            session.save(userInfo);
            
            tx.commit();
            session.close();
        }
        catch (HibernateException e)
        {
            LOG.warn("Exception", e);
            try
            {
                if (tx != null)
                {
                    tx.rollback();
                }
                if (session != null)
                {
                    session.close();
                }
            }
            catch (HibernateException e1)
            {
                LOG.warn("Exception", e);
            }
        }
    }
    
    public UserInfo getUserInfo(String username)
    {
        Session session = null;
        UserInfo userInfo = null;
        try
        {
            session = InitServlet.getSessions().openSession();
            Query query = session.createQuery(
                "SELECT userInfo" +
                "  FROM org.realtors.rets.server.cct.UserInfo" +
                " WHERE userInfo.user.username = ?");
            query.setString(1, username);
            List list = query.list();
            userInfo = (UserInfo) list.get(0);
            session.close();
        }
        catch (HibernateException e)
        {
            LOG.error(e);
            //todo fill this in
        }
        return null;
    }

    private static final Logger LOG = Logger.getLogger(UserUtils.class);
}
