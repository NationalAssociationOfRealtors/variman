/*
 * Created on Sep 9, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import java.util.Iterator;

import org.apache.log4j.Logger;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.realtors.rets.server.SessionHelper;
import org.realtors.rets.server.cct.DBValidationResult;
import org.realtors.rets.server.cct.DBValidationResults;
import org.realtors.rets.server.cct.ValidationResults;
import org.realtors.rets.server.webapp.InitServlet;

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
        SessionHelper sessionHelper = InitServlet.createHelper();
        Session session = null;
        
        try
        {
            session = sessionHelper.beginSession();
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
        catch(HibernateException e)
        {
            LOG.error("Error getting results", e);
        }
        catch(NullPointerException e)
        {
            LOG.error("Damn, boy", e);
            throw e;
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
    
    public static void saveResult(DBValidationResult result)
    {
        SessionHelper sessionHelper = InitServlet.createHelper();
        Session session = null;
        LOG.info(result);
        try
        {
            session = sessionHelper.beginTransaction();
            session.saveOrUpdate(result);
            sessionHelper.commit();
        }
        catch (HibernateException e)
        {
            LOG.error(e);
            try
            {
                sessionHelper.rollback();
            }
            catch (HibernateException e1)
            {
                LOG.error(e1);
            }
        }
        finally
        {
            sessionHelper.close(LOG);
        }
    }

    public final static Logger LOG = Logger.getLogger(DBTestRunner.class);
}
