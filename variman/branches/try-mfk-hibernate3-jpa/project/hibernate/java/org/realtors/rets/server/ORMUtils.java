/*
 * Variman RETS Server
 *
 * Author: Dave Dribin, Mark Klein
 * Copyright (c) 2004-2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.realtors.rets.server.config.DatabaseConfig;
import org.realtors.rets.server.config.RetsConfig;

public class ORMUtils
{
    private static EntityManagerFactory entityManagerFactory;
    private static DatabaseConfig       sDatabaseConfig;
    
    static
    {
        try
        {
            entityManagerFactory = Persistence.createEntityManagerFactory("Variman");
        }
        catch (Throwable ex)
        {
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    /**
     * Create a new JDBC connection to the database.
     * @return a java.sql.Connection
     * @throws RetsServerException
     */
    public static Connection getConnection() throws RetsServerException 
    {
        try
        {
            if (sDatabaseConfig == null)
                sDatabaseConfig = RetsConfig.getInstance().getDatabase();
            Class.forName(sDatabaseConfig.getDriver());
            String url = sDatabaseConfig.getUrl();
            return DriverManager.getConnection(
                                    sDatabaseConfig.getUrl(), 
                                    sDatabaseConfig.getUsername(), 
                                    sDatabaseConfig.getPassword());      
        }
        catch(Exception e)
        {
            throw new RetsServerException(e);
        }
    }
    
    public static EntityManagerFactory getEntityManagerFactory()
    {
        return entityManagerFactory;
    }
    
    public static void shutdown()
    {
        getEntityManagerFactory().close();
    }
    
    public static Object save(Object object)
        throws RetsServerException
    {
        EntityManagerHelper helper = RetsServer.createEntityManagerHelper();
        try
        {
            EntityManager entityManager = helper.beginTransaction();
            object = entityManager.merge(object);
            helper.commit();
            return object;
        }
        finally
        {
            helper.close(LOG);
        }
    }

    public static void update(Object object)
        throws RetsServerException
    {
        EntityManagerHelper helper = RetsServer.createEntityManagerHelper();
        try
        {
            EntityManager entityManager = helper.beginTransaction();
            entityManager.merge(object);
            helper.commit();
        }
        finally
        {
            helper.close(LOG);
        }
    }

    public static void delete(Object object)
        throws RetsServerException
    {
        EntityManagerHelper helper = RetsServer.createEntityManagerHelper();
        try
        {
            EntityManager entityManager = helper.beginTransaction();
            entityManager.remove(object);
            helper.commit();
        }
        finally
        {
            helper.close(LOG);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(ORMUtils.class);
}
