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
    public boolean passwordIsA1()
    {
        return true;
    }

    public PasswordInfo getPassword(String username)
    {
        PasswordInfo info = new PasswordInfo();
        try
        {
            Session session = InitServlet.getSessions().openSession();
            List users = session.find(
                "SELECT user " +
                "  FROM User user " +
                " WHERE user.username = ?",
                username, Hibernate.STRING);
            if (users.size() == 1)
            {
                User user = (User) users.get(0);
                LOG.debug(user);
                info.setPassword(user.getPassword());
                info.setA1(user.getPasswordMethod().getMethod().equals("A1"));
            }
            else
            {
                LOG.debug("Expecting 1 user, found: " + users.size());
            }
        }
        catch (HibernateException e)
        {
            LOG.warn("Exception", e);
        }
        return info;
    }

    private static final Logger LOG =
        Logger.getLogger(HibernatePasswordMap.class);
}
