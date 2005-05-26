/*
 * Variman RETS Server
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

import org.apache.log4j.Logger;

/**
 * @author kgarner
 */
public class TestRunnerFactory
{
    public static TestRunner getTestRunner(String context)
    {
        TestRunner runner = null;
        
        if (sTestRunnerClass != null &&
            sTestRunnerClass.equals("DBTestRunner"))
        {
            LOG.info("Creating a DBTestRunner for " + context);
            runner = new DBTestRunner(context);
        }
        else
        {
            LOG.info("Creating a TestRunner for " + context);
            runner = new TestRunner(context);
        }

        return runner;
    }
    
    public static void setTestRunnerClass(String clazz)
    {
        sTestRunnerClass = clazz;
    }
    
    private static String sTestRunnerClass = null;
    private static final Logger LOG =
        Logger.getLogger(TestRunnerFactory.class);
}
