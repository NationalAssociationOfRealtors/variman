/*
 */
package org.realtors.rets.server.webapp.auth;

import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.realtors.rets.server.SessionHelper;
import org.realtors.rets.server.User;
import org.realtors.rets.server.webapp.InitServlet;
import org.realtors.rets.server.webapp.WebApp;

import org.apache.log4j.Logger;

public class HibernateUserMap implements UserMap
{
    public User findUser(String username)
    {
        if (username == null)
        {
            return null;
        }

        User user = null;

        SessionHelper sessionHelper = WebApp.createHelper();

        try
        {
            Session session = sessionHelper.beginSession();
            List users = session.find(
                "SELECT user " +
                "  FROM User user " +
                " WHERE user.username = ?",
                username, Hibernate.STRING);
            if (users.size() == 1)
            {
                user = (User) users.get(0);
                LOG.debug(user);
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
        finally
        {
            sessionHelper.close(LOG);
        }
        return user;
    }

    private static final Logger LOG =
        Logger.getLogger(HibernateUserMap.class);
}
