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

import java.io.PrintStream;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import java.sql.Connection;

import org.apache.log4j.Logger;

public class SessionHelper implements ConnectionHelper
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
    
    public Connection getConnection() throws RetsServerException 
    {
    	try
    	{
    		beginSession();
	    	return mSession.connection();
    	}
    	catch(HibernateException e)
    	{
    		throw new RetsServerException(e);
    	}
    }

    public Session beginTransaction() throws HibernateException
    {
        LOG.debug("beginTransaction");
        mSession = mFactory.openSession();
        mTx = mSession.beginTransaction();
        return mSession;
    }


    public Connection getConnectionTransaction() throws RetsServerException
    {
    	try
    	{
			beginTransaction();
    		return mSession.connection();
    	}
    	catch(HibernateException e)
    	{
    		throw new RetsServerException(e);
    	}
    }
    
    public Query createQuery(String queryString) throws HibernateException
    {
        LOG.debug("createQuery");
        if (mSession == null)
        {
            beginSession();
        }
        return mSession.createQuery(queryString);
    }

    public void commit() throws RetsServerException
    {
        LOG.debug("commit");
        if (mTx != null)
        {
        	try
        	{
        		mTx.commit();
        		mTx = null;
        	}
        	catch(HibernateException e)
        	{
        		throw new RetsServerException(e);
        	}
        }
    }

    public void rollback() throws RetsServerException
    {
        LOG.debug("rollback");
        if (mTx != null)
        {
        	try
        	{
        		Transaction tx = mTx;
        		mTx = null;
        		tx.rollback();
        	}
        	catch(HibernateException e)
        	{
        		throw new RetsServerException(e);
        	}
        }
    }

    public void rollback(Logger log)
    {
        try
        {
            rollback();
        }
        catch (RetsServerException e)
        {
            log.warn("Exception", e);
        }
    }

    public void rollback(PrintStream stream)
    {
        try
        {
            rollback();
        }
        catch (RetsServerException e)
        {
            e.printStackTrace(stream);
        }
    }

    public void close() throws RetsServerException
    {
        LOG.debug("close");
        if (mSession != null)
        {
        	try
        	{
        		if (mTx != null)
        		{
        			rollback();
        		}
        		mSession.close();
        	}
        	catch(HibernateException e)
        	{
        		throw new RetsServerException(e);
        	}
        }
    }

    public void close(Logger log)
    {
        try
        {
            close();
        }
        catch (RetsServerException e)
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
        catch (RetsServerException e)
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
