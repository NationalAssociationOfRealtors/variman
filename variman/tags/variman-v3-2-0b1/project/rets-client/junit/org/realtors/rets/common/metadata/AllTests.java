/*
 * cart:  CRT's Awesome RETS Tool
 *
 * Author: David Terrell
 * Copyright (c) 2003, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.common.metadata;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.realtors.rets.common.metadata.attrib.AttrAbstractTextTest;
import org.realtors.rets.common.metadata.attrib.AttrAlphanumTest;
import org.realtors.rets.common.metadata.attrib.AttrBooleanTest;
import org.realtors.rets.common.metadata.attrib.AttrDateTest;
import org.realtors.rets.common.metadata.attrib.AttrEnumTest;
import org.realtors.rets.common.metadata.attrib.AttrGenericTextTest;
import org.realtors.rets.common.metadata.attrib.AttrNumericTest;
import org.realtors.rets.common.metadata.attrib.AttrPlaintextTest;
import org.realtors.rets.common.metadata.attrib.AttrTextTest;
import org.realtors.rets.common.metadata.attrib.AttrVersionTest;

/**
 * dbt is lame and hasn't overridden the default
 * javadoc string.
 */
public class AllTests
{
    public static Test suite()
    {
        TestSuite suite;

        suite = new TestSuite();
        suite.addTestSuite(AttrAbstractTextTest.class);
        suite.addTestSuite(AttrAlphanumTest.class);
        suite.addTestSuite(AttrEnumTest.class);
        suite.addTestSuite(AttrGenericTextTest.class);
        suite.addTestSuite(AttrPlaintextTest.class);
        suite.addTestSuite(AttrTextTest.class);

        suite.addTestSuite(AttrBooleanTest.class);
        suite.addTestSuite(AttrDateTest.class);
        suite.addTestSuite(AttrNumericTest.class);
        suite.addTestSuite(AttrVersionTest.class);
        return suite;
    }
}
