/*
 */
package org.realtors.rets.server.dmql;

import antlr.ANTLRException;

public class DmqlCompilerTest extends AbstractDmqlCompilerTest
{
    public DmqlCompilerTest()
    {
        super();
    }

    protected SqlConverter parse(String dmql, boolean traceParser,
                               boolean traceLexer)
        throws ANTLRException
    {
        return DmqlCompiler.parseDmql(dmql, mMetadata, traceParser, traceLexer);
    }

    protected SqlConverter parse(String dmql) throws ANTLRException
    {
        return parse(dmql, false, false);
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

    public void testImpliedLookupOrError() throws ANTLRException
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

    public void testInvalidLookupName() throws ANTLRException
    {
        assertInvalidParse("(XX=|A,S)");
    }

    public void testInvalidLookupValue() throws ANTLRException
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
        assertInvalidParse("(OWNER=\"\")");
        assertInvalidParse("(OWNER=\"foo\")");
        assertInvalidParse("(OWNER=\"Foo Bar\")");
        assertInvalidParse("(OWNER=\"Vito \"\"The Don\"\" Corleone\")");
    }

    public void testNumber() throws ANTLRException
    {
        assertInvalidParse("(OWNER=5)");
        assertInvalidParse("(OWNER=5.)");
        assertInvalidParse("(OWNER=5.0)");
    }

    public void testPeriod() throws ANTLRException
    {
        assertInvalidParse("(OWNER=1970-01-01)");
        parse("(OWNER=TODAY)");
        assertInvalidParse("(OWNER=01:02:03)");
        assertInvalidParse("(OWNER=1970-01-01T05:06:01.33)");
        parse("(OWNER=NOW)");
    }

    public void testBetween() throws ANTLRException
    {
        parse("(OWNER=1970-01-01-1980-01-01)");
        parse("(OWNER=50-100)");
        assertInvalidParse("(OWNER=abc-xyz)");
        parse("(OWNER=1990-01-01-TODAY)");
        parse("(OWNER=1990-01-01T05:06:07.000-NOW)");
    }

    public void testLess() throws ANTLRException
    {
        parse("(OWNER=1970-01-01-)");
        parse("(OWNER=50-)");
        assertInvalidParse("(OWNER=xyz-)");
        parse("(OWNER=TODAY-)");
        parse("(OWNER=NOW-)");
    }

    public void testGreater() throws ANTLRException
    {
        parse("(OWNER=1970-01-01+)");
        parse("(OWNER=50+)");
        assertInvalidParse("(OWNER=xyz+)");
        parse("(OWNER=TODAY+)");
        parse("(OWNER=NOW+)");
    }

    public void testRangeList() throws ANTLRException
    {
        parse("(OWNER=1970-01-01-1980-01-01,1985-01-01-1995-01-01)");
        parse("(OWNER=50-100,150-200)");
        assertInvalidParse("(OWNER=abc-bar,foo-xyz)");

        parse("(OWNER=1970-01-01+,1980-01-01-)");
        parse("(OWNER=50+,60-)");
        assertInvalidParse("(OWNER=abc+,xyz-)");
    }

    public void testCompoundQueries() throws ANTLRException
    {
        parse("(AR=|BATV,GENVA)|(OWNER=foo)");
        assertInvalidParse("(AR=|BATV,GENVA) OR (OWNER=foo)");
        parse("(AR=|BATV,GENVA),(OWNER=foo)");
        assertInvalidParse("(AR=|BATV,GENVA) AND (OWNER=foo)");
        assertInvalidParse("~(AR=|BATV,GENVA)");
        assertInvalidParse("NOT (AR=|BATV,GENVA)");
        assertInvalidParse("(AR=|BATV,GENVA) AND NOT (OWNER=foo)");
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
        assertInvalidParse(
            "~(OR=|OR,AND,NOT,TODAY)AND~(AND=NOW*,OR?AND,*NOT*)");
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
}
