//$Id: AllTests.java,v 1.3 2004/06/04 01:27:35 steveebersole Exp $
package org.hibernate.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * @author Gavin King
 */
public class AllTests {
	
	public static Test suite() {

		TestSuite suite = new TestSuite();

		suite.addTest( FooBarTest.suite() );
		suite.addTest( FumTest.suite() );
		suite.addTest( MasterDetailTest.suite() );
		suite.addTest( ParentChildTest.suite() );
		suite.addTest( ABCTest.suite() );
		suite.addTest( ABCProxyTest.suite() );
		suite.addTest( SQLFunctionsTest.suite() );
		suite.addTest( SQLLoaderTest.suite() );
		suite.addTest( ODMGTest.suite() );
		suite.addTest( MultiTableTest.suite() );
		suite.addTest(QueryByExampleTest.suite());
		
		return suite;
	}

	public static void main(String args[]) {
		TestRunner.run( suite() );
	}
}
