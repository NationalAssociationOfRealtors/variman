/*
 */
package org.realtors.rets.server.webapp.cct;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.realtors.rets.server.webapp.cct.tests.NormalLogin;
import org.realtors.rets.server.webapp.cct.tests.RelativeUrlLogin;

public class CertificationTestSuite
{
    public CertificationTestSuite(String testContext)
    {
        mTests = new ArrayList();
        mTests.add(new NormalLogin());
        mTests.add(new RelativeUrlLogin());

        for (int i = 0; i < mTests.size(); i++)
        {
            CertificationTest test = (CertificationTest) mTests.get(i);
            test.init(testContext);
        }
    }

    public Iterator getTests()
    {
        return mTests.iterator();
    }

    public CertificationTest getTest(int index)
    {
        return (CertificationTest) mTests.get(index);
    }

    public int getCount()
    {
        return mTests.size();
    }

    private List mTests;
}
