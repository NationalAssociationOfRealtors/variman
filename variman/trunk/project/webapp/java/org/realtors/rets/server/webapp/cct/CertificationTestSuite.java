/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp.cct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;

import org.realtors.rets.server.webapp.cct.tests.NormalLogin;
import org.realtors.rets.server.webapp.cct.tests.RelativeUrlLogin;
import org.realtors.rets.server.webapp.cct.tests.MinimumUrlLogin;
import org.realtors.rets.server.webapp.cct.tests.MaximumUrlLogin;
import org.realtors.rets.server.webapp.cct.tests.BasicSearch;
import org.realtors.rets.server.webapp.cct.tests.LoginUrlLogin;

public class CertificationTestSuite
{
    public CertificationTestSuite(String testContext)
    {
        mTests = new ArrayList();
        mTestsByName = new HashMap();
        
        addTest(new NormalLogin());
        addTest(new RelativeUrlLogin());
        addTest(new MinimumUrlLogin());
        addTest(new MaximumUrlLogin());
        addTest(new LoginUrlLogin());
        addTest(new BasicSearch());

        for (int i = 0; i < mTests.size(); i++)
        {
            CertificationTest test = (CertificationTest) mTests.get(i);
            test.init(testContext);
        }
    }
    
    private void addTest(CertificationTest test)
    {
        mTests.add(test);
        mTestsByName.put(test.getName(), test);
    }

    public Iterator getTests()
    {
        return mTests.iterator();
    }

    public CertificationTest getTest(int index)
    {
        return (CertificationTest) mTests.get(index);
    }
    
    public CertificationTest getTest(String name)
    {
        return (CertificationTest) mTestsByName.get(name);
    }

    public int getCount()
    {
        return mTests.size();
    }

    private List mTests;
    private Map mTestsByName;
}
