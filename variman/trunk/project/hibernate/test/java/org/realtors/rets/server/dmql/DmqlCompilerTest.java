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
        LookupList lookup = new LookupList(LookupListType.OR, "r_AR");
        lookup.addLookup("GENVA");
        lookup.addLookup("BATV");
        assertEquals(lookup, sql);
    }

    public void testImpliedLookupOr() throws ANTLRException
    {
        SqlConverter sql = parse("(STATUS=A)");
        LookupList lookup = new LookupList(LookupListType.OR, "r_STATUS");
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
        LookupList lookup = new LookupList(LookupListType.AND, "r_STATUS");
        lookup.addLookup("A");
        lookup.addLookup("S");
        assertEquals(lookup, sql);
    }

    public void testLookupNot() throws ANTLRException
    {
        SqlConverter sql = parse("(STATUS=~A,S)");
        LookupList lookup = new LookupList(LookupListType.NOT, "r_STATUS");
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
        SqlConverter sql = parse("(OWNER=foo)");
        DmqlStringList list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("foo"));
        assertEquals(list, sql);
    }

    public void testStringStart() throws ANTLRException
    {
        SqlConverter sql = parse("(OWNER=f*)");
        DmqlStringList list = new DmqlStringList("r_OWNER");
        DmqlString string = new DmqlString();
        string.add("f");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        list.add(string);
        assertEquals(list, sql);
    }

    public void testStringContains() throws ANTLRException
    {
        SqlConverter sql = parse("(OWNER=*foo*)");
        DmqlStringList list = new DmqlStringList("r_OWNER");
        DmqlString string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        string.add("foo");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        list.add(string);
        assertEquals(list, sql);
    }

    public void testStringChar() throws ANTLRException
    {
        SqlConverter sql = parse("(OWNER=f?o)");
        DmqlStringList list = new DmqlStringList("r_OWNER");
        DmqlString string = new DmqlString();
        string.add("f");
        string.add(DmqlString.MATCH_ZERO_OR_ONE);
        string.add("o");
        list.add(string);
        assertEquals(list, sql);

        sql = parse("(OWNER=?oo)");
        list = new DmqlStringList("r_OWNER");
        string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_ONE);
        string.add("oo");
        list.add(string);
        assertEquals(list, sql);

        sql = parse("(OWNER=fo?)");
        list = new DmqlStringList("r_OWNER");
        string = new DmqlString();
        string.add("fo");
        string.add(DmqlString.MATCH_ZERO_OR_ONE);
        list.add(string);
        assertEquals(list, sql);

        sql = parse("(OWNER=?)");
        list = new DmqlStringList("r_OWNER");
        string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_ONE);
        list.add(string);
        assertEquals(list, sql);
    }

    public void testStringList() throws ANTLRException
    {
        SqlConverter sql = parse("(OWNER=foo,f*,*foo*,f?o)");
        DmqlStringList list = new DmqlStringList("r_OWNER");
        DmqlString string = new DmqlString();
        string.add("foo");
        list.add(string);

        string = new DmqlString();
        string.add("f");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        list.add(string);

        string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        string.add("foo");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        list.add(string);

        string = new DmqlString();
        string.add("f");
        string.add(DmqlString.MATCH_ZERO_OR_ONE);
        string.add("o");
        list.add(string);

        assertEquals(list, sql);
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

    public void testRangeList() throws ANTLRException
    {
        parse("(OWNER=1970-01-01-1980-01-01,1985-01-01-1995-01-01)");
        parse("(OWNER=50-100,150-200)");
        assertInvalidParse("(OWNER=abc-bar,foo-xyz)");

        parse("(OWNER=1970-01-01+,1980-01-01-)");
        parse("(OWNER=50+,60-)");
        assertInvalidParse("(OWNER=abc+,xyz-)");
    }

    public void testCompoundOr() throws ANTLRException
    {
        SqlConverter sql = parse("(AR=|BATV,GENVA)|(OWNER=foo)");
        LookupList lookupList = new LookupList(LookupListType.OR, "r_AR");
        lookupList.addLookup("BATV");
        lookupList.addLookup("GENVA");
        DmqlStringList stringList = new DmqlStringList("r_OWNER");
        stringList.add(new DmqlString("foo"));
        OrClause orClause = new OrClause();
        orClause.add(lookupList);
        orClause.add(stringList);
        assertEquals(orClause, sql);

        assertInvalidParse("(AR=|BATV,GENVA) OR (OWNER=foo)");
    }

    public void testCompoundAnd() throws ANTLRException
    {
        SqlConverter sql = parse("(AR=|BATV,GENVA),(OWNER=foo)");
        LookupList lookupList = new LookupList(LookupListType.OR, "r_AR");
        lookupList.addLookup("BATV");
        lookupList.addLookup("GENVA");
        DmqlStringList stringList = new DmqlStringList("r_OWNER");
        stringList.add(new DmqlString("foo"));
        AndClause andClause = new AndClause();
        andClause.add(lookupList);
        andClause.add(stringList);
        assertEquals(andClause, sql);

        assertInvalidParse("(AR=|BATV,GENVA) AND (OWNER=foo)");
    }

    public void testCompoundNot() throws ANTLRException
    {
        assertInvalidParse("~(AR=|BATV,GENVA)");
        assertInvalidParse("NOT (AR=|BATV,GENVA)");
    }

    public void testCompoundQueries() throws ANTLRException
    {
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
