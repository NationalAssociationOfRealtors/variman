/*
 */
package org.realtors.rets.server.dmql;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import antlr.ANTLRException;
import antlr.RecognitionException;
import junit.framework.TestCase;

public class DmqlCompilerTest extends TestCase
{
    public DmqlCompilerTest()
    {
        mMetadata = new TestDmqlParserMetadata();
        mMetadata.addLookup("AR", new String[] {"GENVA", "BATV"});
        mMetadata.addLookup("STATUS",
                            new String[]{"A", "O", "S", "U", "C", "W", "P"});
    }

    public void testLookupOr() throws ANTLRException
    {
        SqlConverter sql = DmqlCompiler.parseDmql("(AR=|GENVA,BATV)",
                                                  mMetadata);
        LookupList lookup = new LookupList(LookupListType.OR, "AR");
        lookup.addLookup("GENVA");
        lookup.addLookup("BATV");
        assertEquals(lookup, sql);
    }

    public void testImpliedLookupOr() throws ANTLRException
    {
        SqlConverter sql = DmqlCompiler.parseDmql("(STATUS=A)", mMetadata);
        LookupList lookup = new LookupList(LookupListType.OR, "STATUS");
        lookup.addLookup("A");
        assertEquals(lookup, sql);
    }

    public void testImpliedLookupOrError()
    {
        try
        {
            DmqlCompiler.parseDmql("(AR=GENVA,BATV)", mMetadata);
            fail("Should have thrown an exception");
        }
        catch (ANTLRException e)
        {
            // Expected
            assertTrue(e instanceof RecognitionException);
        }
    }

    public void testLookupAnd() throws ANTLRException
    {
        SqlConverter sql = DmqlCompiler.parseDmql("(STATUS=+A,S)", mMetadata);
        LookupList lookup = new LookupList(LookupListType.AND, "STATUS");
        lookup.addLookup("A");
        lookup.addLookup("S");
        assertEquals(lookup, sql);
    }

    public void testLookupNot() throws ANTLRException
    {
        SqlConverter sql = DmqlCompiler.parseDmql("(STATUS=~A,S)", mMetadata);
        LookupList lookup = new LookupList(LookupListType.NOT, "STATUS");
        lookup.addLookup("A");
        lookup.addLookup("S");
        assertEquals(lookup, sql);
    }

    public void testInvalidLookupName()
    {
        try
        {
            DmqlCompiler.parseDmql("(XX=|A,S)", mMetadata);
            fail("Should have thrown exception");
        }
        catch (ANTLRException e)
        {
            // Expected
            assertTrue(e instanceof RecognitionException);
        }
    }

    public void testInvalidLookupValue()
    {
        try
        {
            DmqlCompiler.parseDmql("(AR=|A,S)", mMetadata);
            fail("Should have thrown exception");
        }
        catch (ANTLRException e)
        {
            // Expected
            assertTrue(e instanceof RecognitionException);
        }
    }

    static class TestDmqlParserMetadata implements DmqlParserMetadata
    {
        public TestDmqlParserMetadata()
        {
            mLookups = new HashMap();
        }

        public void addLookup(String name, String[] values)
        {
            Set valueSet = new HashSet();
            for (int i = 0; i < values.length; i++)
            {
                valueSet.add(values[i]);
            }
            mLookups.put(name, valueSet);
        }

        public boolean isValidLookupName(String lookupName)
        {
            return mLookups.containsKey(lookupName);
        }

        public boolean isValidLookupValue(String lookupName, String lookupValue)
        {
            Set values = (Set) mLookups.get(lookupName);
            return values.contains(lookupValue);
        }

        private Map mLookups;
    }

    private TestDmqlParserMetadata mMetadata;
}
