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
        mMetadata.addLookup("OR",
                            new String[] {"OR", "AND", "NOT", "TODAY","NOW"});
        mMetadata.addString("AND");
        mMetadata.addString("NOT");

        mMetadata.addLookup("AREA", new String[]{"1", "2"});
        mMetadata.addString("CITY");

        mMetadata.addLookup("ST", new String[]{"ACT", "SOLD"});
        mMetadata.addLookup("EXT", new String[]{"WTRFRNT", "DOCK"});
        mMetadata.addLookup("TYPE", new String[]{"CONDO", "TWNHME"});
        mMetadata.addString("LP");
        mMetadata.addString("STR");
        mMetadata.addString("STYLE");
        mMetadata.addString("LDATE");
        mMetadata.addString("REM");
        mMetadata.addString("OWNER");
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
        parse("(OWNER=foo)");
    }

    public void testStringStart() throws ANTLRException
    {
        parse("(OWNER=f*)");
    }

    public void testStringContains() throws ANTLRException
    {
        parse("(OWNER=*foo*)");
    }

    public void testStringChar() throws ANTLRException
    {
        parse("(OWNER=f?o)");
        parse("(OWNER=?oo)");
        parse("(OWNER=fo?)");
        parse("(OWNER=?)");
    }

    public void testStringList() throws ANTLRException
    {
        parse("(OWNER=foo,f*,*foo*,f?o)");
    }

    public void testStringLiteral() throws ANTLRException
    {
        parse("(OWNER=\"\")");
        parse("(OWNER=\"foo\")");
        parse("(OWNER=\"Foo Bar\")");
        parse("(OWNER=\"Vito \"\"The Don\"\" Corleone\")");
    }

    public void testNumber() throws ANTLRException
    {
        parse("(OWNER=5)");
        parse("(OWNER=5.)");
        parse("(OWNER=5.0)");
    }

    public void testPeriod() throws ANTLRException
    {
        parse("(OWNER=1970-01-01)");
        parse("(OWNER=TODAY)");
        parse("(OWNER=01:02:03)");
        parse("(OWNER=1970-01-01T05:06:01.33)");
        parse("(OWNER=NOW)");
    }

    public void testBetween() throws ANTLRException
    {
        parse("(OWNER=1970-01-01-1980-01-01)");
        parse("(OWNER=50-100)");
        parse("(OWNER=abc-xyz)");
    }

    public void testLess() throws ANTLRException
    {
        parse("(OWNER=1970-01-01-)");
        parse("(OWNER=50-)");
        parse("(OWNER=xyz-)");
    }

    public void testGreater() throws ANTLRException
    {
        parse("(OWNER=1970-01-01+)");
        parse("(OWNER=50+)");
        parse("(OWNER=xyz+)");
    }

    public void testRangeList() throws ANTLRException
    {
        parse("(OWNER=1970-01-01-1980-01-01,1985-01-01-1995-01-01)");
        parse("(OWNER=50-100,150-200)");
        parse("(OWNER=abc-bar,foo-xyz)");

        parse("(OWNER=1970-01-01+,1980-01-01-)");
        parse("(OWNER=50+,60-)");
        parse("(OWNER=abc+,xyz-)");
    }

    public void testCompoundQueries() throws ANTLRException
    {
        parse("(AR=|BATV,GENVA)|(OWNER=foo)");
        parse("(AR=|BATV,GENVA) OR (OWNER=foo)");
        parse("(AR=|BATV,GENVA),(OWNER=foo)");
        parse("(AR=|BATV,GENVA) AND (OWNER=foo)");
        parse("~(AR=|BATV,GENVA)");
        parse("NOT (AR=|BATV,GENVA)");
        parse("(AR=|BATV,GENVA) AND NOT (OWNER=foo)");
        parse("((OWNER=foo),(OWNER=bar))");
        parse("(AR=|BATV,GENVA)|((OWNER=foo),(AND=bar))");
    }

    public void testTokensInStrings() throws ANTLRException
    {
        parse("(OWNER=OR)");
        parse("(OWNER=*OR*)");
        parse("(OWNER=OR*)");
        parse("(OWNER=OR?AND)");
        parse("(OWNER=AND)");
        parse("(OR=OR)");
        parse("(AND=foo)");
        parse("(NOT=foo)");
        parse("(OR=|OR,AND,NOT,TODAY)");
        parse("~(OR=|OR,AND,NOT,TODAY)AND~(AND=NOW*,OR?AND,*NOT*)");
    }

    public void testSampleSubQuery() throws ANTLRException
    {
        parse("((AREA=|1,2)|(CITY=ACTION)),(LP=200000+)");
        parse("(ST=|ACT,SOLD),\n" +
              "(LP=200000-350000),\n" +
              "(STR=RIVER),\n" +
              "(STYLE=RANCH),\n" +
              "(EXT=+WTRFRNT,DOCK),\n" +
              "(LDATE=2000-03-01+),\n" +
              "(REM=*FORECLOSE*),\n" +
              "(TYPE=~CONDO,TWNHME),\n" +
              "(OWNER=P?LE)");
    }

    private SqlConverter parse(String dmql, boolean traceParser,
                               boolean traceLexer)
        throws ANTLRException
    {
        return DmqlCompiler.parseDmql(dmql, mMetadata, traceParser, traceLexer);
    }

    private SqlConverter parse(String dmql) throws ANTLRException
    {
        return parse(dmql, false, false);
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
