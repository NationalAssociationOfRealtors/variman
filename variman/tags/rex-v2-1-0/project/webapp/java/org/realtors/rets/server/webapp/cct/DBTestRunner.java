/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 * Created on Sep 9, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.realtors.rets.server.SessionHelper;
import org.realtors.rets.server.cct.DBValidationResult;
import org.realtors.rets.server.cct.DBValidationResults;
import org.realtors.rets.server.cct.ValidationResults;
import org.realtors.rets.server.webapp.WebApp;

import org.apache.log4j.Logger;

/**
 * @author kgarner
 */
public class DBTestRunner extends TestRunner
{

    /**
     * @param context test context.  Assumed to be UserName
     */
    public DBTestRunner(String context)
    {
        super(context, new CertificationTestSuite(context), null);
        DBValidationResults results = new DBValidationResults();
        results.setContext(context);
        mResults = results;
        restoreResults(context, mResults);
    }

    private static void restoreResults(String context,
                                       ValidationResults results)
    {
        SessionHelper sessionHelper = WebApp.createHelper();
        try
        {
            Session session = sessionHelper.beginSession();
            Iterator i = session.iterate(
                "SELECT results" +
                "  FROM DBValidationResult results" +
                " WHERE results.username = ?",
                context, Hibernate.STRING);
            while (i.hasNext())
            {
                DBValidationResult result = (DBValidationResult) i.next();
                results.addResult(result);
            }
        }
        catch (HibernateException e)
        {
            LOG.error("Error getting results", e);
        }
        finally
        {
            sessionHelper.close(LOG);
        }
    }
    
    public void stopRunningTest()
    {
        if (mRunningTest != null)
        {
            String testName = mRunningTest.getName();
            super.stopRunningTest();
            DBValidationResult result =
                (DBValidationResult) mResults.getResultByName(testName);
            saveResult(result);
        }
        else
        {
            LOG.warn("No running test to stop");
        }
    }
    
    private static void saveResult(DBValidationResult result)
    {
        SessionHelper sessionHelper = WebApp.createHelper();
        try
        {
            Session session = sessionHelper.beginTransaction();
            session.saveOrUpdate(result);
            sessionHelper.commit();
        }
        catch (HibernateException e)
        {
            LOG.error(e);
            sessionHelper.rollback(LOG);
        }
        finally
        {
            sessionHelper.close(LOG);
        }
    }
    
    private static void saveResults(List results)
    {
        SessionHelper sessionHelper = WebApp.createHelper();
        try
        {
            Session session = sessionHelper.beginTransaction();
            Iterator i = results.iterator();
            while (i.hasNext())
            {
                DBValidationResult result = (DBValidationResult) i.next();
                session.saveOrUpdate(result);
            }
            sessionHelper.commit();
        }
        catch (HibernateException e)
        {
            LOG.error(e);
            sessionHelper.rollback(LOG);
        }
        finally
        {
            sessionHelper.close(LOG);
        }
    }

    public void resetAllResults()
    {
        if (mRunningTest != null)
        {
            stopRunningTest();
        }

        List saveList = new ArrayList();
        Iterator i = mResults.iterator();
        while (i.hasNext())
        {
            DBValidationResult result = (DBValidationResult) i.next();
            result.reset();
            saveList.add(result);
        }
        saveResults(saveList);
    }

    public final static Logger LOG = Logger.getLogger(DBTestRunner.class);
}
