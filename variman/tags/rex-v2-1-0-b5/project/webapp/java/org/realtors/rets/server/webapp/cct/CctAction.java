/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.webapp.cct;

import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;

/**
 * Created by IntelliJ IDEA.  Obliterated by Eclipse.
 * User: dbt
 * Date: Aug 26, 2003
 * Time: 2:05:42 PM
 * To change this template use Options | File Templates.
 */
public abstract class CctAction extends Action implements CctConstants
{
    protected TestRunner getTestRunner(HttpSession session)
    {
        return (TestRunner) session.getAttribute(TESTRUNNER_KEY);
    }
    
    protected static final UserUtils UTILS = new UserUtils();
}
