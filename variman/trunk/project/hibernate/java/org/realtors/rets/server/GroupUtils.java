package org.realtors.rets.server;

import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import org.apache.log4j.Logger;

public class GroupUtils
{
    public static List findAll() throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            return findAll(helper);
        }
        finally
        {
            helper.close(LOG);
        }
    }

    public static List findAll(SessionHelper helper) throws HibernateException
    {
        Query query = helper.createQuery(
            "  FROM Group aGroup " +
            "ORDER BY aGroup.name");
        return query.list();
    }

    public static Group findByName(String name) throws HibernateException
    {
        SessionHelper helper = RetsServer.createSessionHelper();
        try
        {
            return findByName(name, helper);
        }
        finally
        {
            helper.close(LOG);
        }
    }

    public static Group findByName(String name, SessionHelper helper)
        throws HibernateException
    {
        Query query = helper.createQuery(
            " From Group aGroup " +
            "WHERE aGroup.name = :name");
        query.setString("name", name);
        return (Group) query.uniqueResult();
    }

    private static final Logger LOG =
        Logger.getLogger(GroupUtils.class);
}
