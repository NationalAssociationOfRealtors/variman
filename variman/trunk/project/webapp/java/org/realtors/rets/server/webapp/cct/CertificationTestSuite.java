/*
 */
package org.realtors.rets.server.webapp.cct;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.realtors.rets.server.webapp.cct.tests.NormalLogin;
import org.realtors.rets.server.webapp.cct.tests.RelativeUrlLogin;
import org.realtors.rets.server.webapp.cct.tests.MinimumUrlLogin;
import org.realtors.rets.server.webapp.cct.tests.MaximumUrlLogin;

public class CertificationTestSuite
{
    public CertificationTestSuite(String testContext)
    {
        mTests = new ArrayList();
        mTests.add(new NormalLogin());
        mTests.add(new RelativeUrlLogin());
        mTests.add(new MinimumUrlLogin());
        mTests.add(new MaximumUrlLogin());

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
