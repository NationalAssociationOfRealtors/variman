/*
 */
package org.realtors.rets.server.dmql;

import junit.framework.TestCase;
import antlr.ANTLRException;

public class DmqlCompilerTest extends TestCase
{
    public void testSimple() throws ANTLRException
    {
        String sql = DmqlCompiler.dmqlToSql("(STATUS=A)");
        assertEquals("(STATUS = 'A')", sql);
    }
}
