/*
 */
package org.realtors.rets.server;

import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.HibernateException;

import org.apache.log4j.Logger;

public class SessionHelper
{
    public SessionHelper(SessionFactory factory, Logger log)
    {
        mFactory = factory;
        mLog = log;
        mSession = null;
        mTx = null;
    }

    public SessionHelper(SessionFactory factory)
    {
        this(factory, LOG);
    }

    public Session beginTransaction() throws HibernateException
    {
        mSession = mFactory.openSession();
        mTx = mSession.beginTransaction();
        return mSession;
    }

    public void commit() throws HibernateException
    {
        mTx.commit();
    }

    public void loggedRollback()
    {
        try
        {
            rollback();
        }
        catch (HibernateException e)
        {
            mTx = null;
            mLog.warn("Exception", e);
        }
    }

    public void rollback() throws HibernateException
    {
        if (mTx != null)
        {
            mTx.rollback();
        }
    }

    public void loggedClose()
    {
        try
        {
            close();
        }
        catch (HibernateException e)
        {
            mSession = null;
            mLog.warn("Exception", e);
        }
    }

    public void close() throws HibernateException
    {
        if (mSession != null)
        {
            mSession.close();
        }
    }

    private static final Logger LOG =
        Logger.getLogger(SessionHelper.class);
    private SessionFactory mFactory;
    private Session mSession;
    private Transaction mTx;
    private Logger mLog;
}
