/*
 */
package org.realtors.rets.server.webapp.auth;

import java.util.List;

import net.sf.hibernate.Session;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Hibernate;

import org.realtors.rets.server.webapp.InitServlet;
import org.realtors.rets.server.User;
import org.apache.log4j.Logger;

public class HibernatePasswordMap implements PasswordMap
{
    public String getPassword(String username)
    {
        String password = null;
        try
        {
            Session session = InitServlet.getSessions().openSession();
            List users = session.find("from User user where user.username = ?",
                                      username, Hibernate.STRING);
            if (users.size() == 1)
            {
                User user = (User) users.get(0);
                password = user.getPassword();
                LOG.debug(user);
            }
        }
        catch (HibernateException e)
        {
            LOG.warn("Exception", e);
        }
        return password;
    }

    private static final Logger LOG =
        Logger.getLogger(HibernatePasswordMap.class);
}
