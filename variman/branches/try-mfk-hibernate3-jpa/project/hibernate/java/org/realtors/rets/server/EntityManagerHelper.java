/*
 * Variman RETS Server
 *
 * Author: Dave Dribin, Mark Klein
 * Copyright (c) 2004-2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

import java.io.PrintStream;

import java.sql.Connection;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.log4j.Logger;


public class EntityManagerHelper implements ConnectionHelper
{
    public EntityManagerHelper(EntityManagerFactory factory)
    {
        mFactory = factory;
        mEntityManager = null;
        mTx = null;
    }

    public EntityManager beginSession()
    {
        LOG.debug("beginSession");
        mEntityManager = mFactory.createEntityManager();
        mTx = null;
        return mEntityManager;
    }
    
    public Connection getConnection() throws RetsServerException 
    {
        return ORMUtils.getConnection();
    }

    public EntityManager beginTransaction()
    {
        LOG.debug("beginTransaction");
        mEntityManager = mFactory.createEntityManager();
        mTx = mEntityManager.getTransaction();
        return mEntityManager;
    }


    public Connection getConnectionTransaction() throws RetsServerException
    {
    	try
    	{
			beginTransaction();
    		return getConnection();
    	}
    	catch(Exception e)
    	{
    		throw new RetsServerException(e);
    	}
    }
    
    public Query createQuery(String queryString)
    {
        LOG.debug("createQuery");
        if (mEntityManager == null)
        {
            beginSession();
        }
        return mEntityManager.createQuery(queryString);
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
        	catch(Exception e)
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
        		EntityTransaction tx = mTx;
        		mTx = null;
        		tx.rollback();
        	}
        	catch(Exception e)
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
        if (mEntityManager != null)
        {
        	try
        	{
        		if (mTx != null)
        		{
        			rollback();
        		}
        		mEntityManager.close();
        	}
        	catch(Exception e)
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
            mEntityManager = null;
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
            mEntityManager = null;
            e.printStackTrace(stream);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(EntityManagerHelper.class);
    private EntityManagerFactory mFactory;
    private EntityManager mEntityManager;
    private EntityTransaction mTx;
}
