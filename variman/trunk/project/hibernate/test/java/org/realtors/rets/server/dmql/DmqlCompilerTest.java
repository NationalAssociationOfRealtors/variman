/*
 */
package org.realtors.rets.server.dmql;

import antlr.ANTLRException;
import junit.framework.TestCase;

public class DmqlCompilerTest extends TestCase
{
    public void testSimple() throws ANTLRException
    {
//        String sql = DmqlCompiler.dmqlToSql("(STATUS=A)");
//        assertEquals("(STATUS = 'A')", sql);
    }

    public void testLookupOr() throws ANTLRException
    {
        SqlConverter sql = DmqlCompiler.parseDmql("(AR=|GENVA,BATV)");
        LookupList lookup = new LookupList(LookupListType.OR, "AR");
        lookup.addLookup("GENVA");
        lookup.addLookup("BATV");
        assertEquals(lookup, sql);

//        DmqlCompiler.parseDmql("(AR=+XXX,YYY)");
//        DmqlCompiler.parseDmql("(AR=~AAA,BBB)");
//        DmqlCompiler.parseDmql("(AR=GENVA)");
//        DmqlCompiler.parseDmql("(AR=GENVA,BATV)");
//        DmqlCompiler.parseDmql("(STATUS=A)");
//        DmqlCompiler.parseDmql("(STATUS=B,C)");
//        DmqlCompiler.parseDmql("(STATUS=|O,P)");
//        DmqlCompiler.parseDmql("(STATUS==Q)");
    }
}
