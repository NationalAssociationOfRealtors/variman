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
        mMetadata = new SimpleDmqlMetadataValidator();
        mMetadata.addLookup("AR", new String[] {"GENVA", "BATV"});
        mMetadata.addLookup("STATUS",
                            new String[]{"A", "O", "S", "U", "C", "W", "P"});
        mMetadata.addString("owner");
    }

    public void testLookupOr() throws ANTLRException
    {
        SqlConverter sql = parse("(AR=|GENVA,BATV)");
        LookupList lookup = new LookupList(LookupListType.OR, "AR");
        lookup.addLookup("GENVA");
        lookup.addLookup("BATV");
        assertEquals(lookup, sql);
    }

    public void testImpliedLookupOr() throws ANTLRException
    {
        SqlConverter sql = parse("(STATUS=A)");
        LookupList lookup = new LookupList(LookupListType.OR, "STATUS");
        lookup.addLookup("A");
        assertEquals(lookup, sql);
    }

    public void testImpliedLookupOrError()
    {
        assertInvalidParse("(AR=GENVA,BATV)");
    }

    public void testLookupAnd() throws ANTLRException
    {
        SqlConverter sql = parse("(STATUS=+A,S)");
        LookupList lookup = new LookupList(LookupListType.AND, "STATUS");
        lookup.addLookup("A");
        lookup.addLookup("S");
        assertEquals(lookup, sql);
    }

    public void testLookupNot() throws ANTLRException
    {
        SqlConverter sql = parse("(STATUS=~A,S)");
        LookupList lookup = new LookupList(LookupListType.NOT, "STATUS");
        lookup.addLookup("A");
        lookup.addLookup("S");
        assertEquals(lookup, sql);
    }

    public void testInvalidLookupName()
    {
        assertInvalidParse("(XX=|A,S)");
    }

    public void testInvalidLookupValue()
    {
        assertInvalidParse("(AR=|A,S)");
    }

    public void testStringEquals() throws ANTLRException
    {
        parse("(owner=foo)");
    }

    public void testStringStart() throws ANTLRException
    {
        parse("(owner=f*)");
    }

    public void testStringContains() throws ANTLRException
    {
        parse("(owner=*foo*)");
    }

    public void testStringChar() throws ANTLRException
    {
        parse("(owner=f?o)");
    }

    public void testStringList() throws ANTLRException
    {
        parse("(owner=foo,f*,*foo*,f?o)");
    }

    public void testStringLiteral() throws ANTLRException
    {
        parse("(owner=\"\")");
        parse("(owner=\"foo\")");
        parse("(owner=\"Foo Bar\")");
        parse("(owner=\"Vito \"\"The Don\"\" Corleone\")");
    }

    public void testNumber() throws ANTLRException
    {
        parse("(owner=5)");
        parse("(owner=5.)");
        parse("(owner=5.0)");
    }

    public void testPeriod() throws ANTLRException
    {
        parse("(owner=1970-01-01)");
        parse("(owner=TODAY)");
        parse("(owner=01:02:03)");
        parse("(owner=1970-01-01T05:06:01.33)");
        parse("(owner=NOW)");
    }

    public void testBetween() throws ANTLRException
    {
        parse("(owner=1970-01-01-1980-01-01)");
        parse("(owner=50-100)");
        parse("(owner=abc-xyz)");
    }

    public void testLess() throws ANTLRException
    {
        parse("(owner=1970-01-01-)");
        parse("(owner=50-)");
        parse("(owner=xyz-)");
    }

    public void testGreater() throws ANTLRException
    {
        parse("(owner=1970-01-01+)");
        parse("(owner=50+)");
        parse("(owner=xyz+)");
    }

    public void testRangeList() throws ANTLRException
    {
        parse("(owner=1970-01-01-1980-01-01,1985-01-01-1995-01-01)");
        parse("(owner=50-100,150-200)");
        parse("(owner=abc-bar,foo-xyz)");

        parse("(owner=1970-01-01+,1980-01-01-)");
        parse("(owner=50+,60-)");
        parse("(owner=abc+,xyz-)");
    }

    protected SqlConverter parse(String dmql, boolean traceParser,
                                 boolean traceLexer)
        throws ANTLRException
    {
        return DmqlCompiler.parseDmql(dmql, mMetadata, traceParser, traceLexer);
    }

    private SqlConverter parse(String dmql) throws ANTLRException
    {
        return DmqlCompiler.parseDmql(dmql, mMetadata);
    }

    private void assertInvalidParse(String dmql)
    {
        try
        {
            parse(dmql);
            fail("Should have thrown an exception");
        }
        catch (ANTLRException e)
        {
            // Expected
            assertTrue(e instanceof RecognitionException);
        }
    }

    static class SimpleDmqlMetadataValidator implements DmqlParserMetadata
    {
        public SimpleDmqlMetadataValidator()
        {
            mFields = new HashSet();
            mLookups = new HashMap();
            mStrings = new HashSet();
        }

        public void addLookup(String name, String[] values)
        {
            Set valueSet = new HashSet();
            for (int i = 0; i < values.length; i++)
            {
                valueSet.add(values[i]);
            }
            mFields.add(name);
            mLookups.put(name, valueSet);
        }

        public void addString(String fieldName)
        {
            mFields.add(fieldName);
            mStrings.add(fieldName);
        }

        public boolean isValidFieldName(String fieldName)
        {
            return mFields.contains(fieldName);
        }

        public boolean isValidStringName(String fieldName)
        {
            return mStrings.contains(fieldName);
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

        private Set mFields;
        private Map mLookups;
        private Set mStrings;
    }

    private SimpleDmqlMetadataValidator mMetadata;
}
