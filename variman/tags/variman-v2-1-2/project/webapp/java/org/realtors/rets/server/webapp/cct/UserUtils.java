/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 * Created on Aug 25, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.realtors.rets.server.PasswordMethod;
import org.realtors.rets.server.SessionHelper;
import org.realtors.rets.server.User;
import org.realtors.rets.server.cct.UserInfo;
import org.realtors.rets.server.webapp.WebApp;

import org.apache.log4j.Logger;

/**
 * @author kgarner
 */
public class UserUtils
{
    public boolean authenticateUser(User user, String password)
    {
        if (user == null)
        {
            return false;
        } 
        PasswordMethod pm = user.getPasswordMethod();
        String hash = pm.hash(user.getUsername(), password);
        return pm.verifyPassword(user.getPassword(), hash);
    }

    public void createUser(User user, UserInfo userInfo)
    {
        SessionHelper helper = WebApp.createHelper();
        try
        {
            Session session = helper.beginTransaction();

            session.save(user);
            session.save(userInfo);

            helper.commit();
        }
        catch (HibernateException e)
        {
            LOG.error("An exception occured", e);
            helper.rollback(LOG);
        }
        finally
        {
            helper.close(LOG);
        }
    }

    public UserInfo getUserInfo(String username)
    {
        if (username == null)
        {
            LOG.warn("You dumbass, you gave me a null username");
            return null;
        }
        SessionHelper helper = WebApp.createHelper();
        UserInfo userInfo = null;
        try
        {
            Session session = helper.beginSession();
            Query query = session.createQuery(
                "SELECT userInfo FROM UserInfo userInfo" +
                " WHERE userInfo.user.username = ?");
            query.setString(0, username);
            List list = query.list();
            LOG.debug(list);
            if (list != null && list.size() > 0)
            {
                userInfo = (UserInfo) list.get(0);
            }
        }
        catch (HibernateException e)
        {
            LOG.error("An exception occured", e);
        }
        finally
        {
            helper.close(LOG);
        }
        return userInfo;
    }
    
    public List getAllUserInfos()
    {
        SessionHelper helper = WebApp.createHelper();
        List userInfos = null;
        try
        {
            Session session = helper.beginSession();
            userInfos = session.find(
                "SELECT userInfo" +
                "  FROM UserInfo userInfo"); // +
 //               "ORDER BY userInfo.user.username");
        }
        catch (HibernateException e)
        {
            LOG.error("Can't get userInfos", e);
        }
        finally
        {
            helper.close(LOG);
        }
        
        return userInfos;
    }

    private static final Logger LOG = Logger.getLogger(UserUtils.class);
}
