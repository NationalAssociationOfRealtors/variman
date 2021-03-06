/*
 */
package org.realtors.rets.server;

import java.io.PrintStream;

import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.HibernateException;

import org.apache.log4j.Logger;

public class SessionHelper
{
    public SessionHelper(SessionFactory factory)
    {
        mFactory = factory;
        mSession = null;
        mTx = null;
    }

    public Session beginSession() throws HibernateException
    {
        LOG.debug("beginSession");
        mSession = mFactory.openSession();
        mTx = null;
        return mSession;
    }

    public Session beginTransaction() throws HibernateException
    {
        LOG.debug("beginTransaction");
        mSession = mFactory.openSession();
        mTx = mSession.beginTransaction();
        return mSession;
    }

    public void commit() throws HibernateException
    {
        LOG.debug("commit");
        mTx.commit();
    }

    public void rollback() throws HibernateException
    {
        LOG.debug("rollback");
        if (mTx != null)
        {
            mTx.rollback();
        }
    }

    public void rollback(Logger log)
    {
        try
        {
            rollback();
        }
        catch (HibernateException e)
        {
            mTx = null;
            log.warn("Exception", e);
        }
    }

    public void rollback(PrintStream stream)
    {
        try
        {
            rollback();
        }
        catch (HibernateException e)
        {
            mTx = null;
            e.printStackTrace(stream);
        }
    }

    public void close() throws HibernateException
    {
        LOG.debug("close");
        if (mSession != null)
        {
            mSession.close();
        }
    }

    public void close(Logger log)
    {
        try
        {
            close();
        }
        catch (HibernateException e)
        {
            mSession = null;
            log.warn("Exception", e);
        }
    }

    public void close(PrintStream stream)
    {
        try
        {
            close();
        }
        catch (HibernateException e)
        {
            mSession = null;
            e.printStackTrace(stream);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(SessionHelper.class);
    private SessionFactory mFactory;
    private Session mSession;
    private Transaction mTx;
}
