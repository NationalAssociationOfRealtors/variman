/*
 */
package org.realtors.rets.server.dmql;

import org.realtors.rets.server.dmql.DmqlCompiler.ParserResults;

import antlr.ANTLRException;

public class DmqlCompilerTest extends AbstractDmqlCompilerTest
{
    public DmqlCompilerTest()
    {
        super();
    }

    protected ParserResults parse(String dmql, boolean traceParser,
                               boolean traceLexer)
        throws ANTLRException
    {
        return DmqlCompiler.parseDmql(dmql, mMetadata, traceParser, traceLexer);
    }

    protected ParserResults parse(String dmql) throws ANTLRException
    {
        return parse(dmql, false, false);
    }

    public void testStringLiteral() throws ANTLRException
    {
        assertInvalidParse("(OWNER=\"\")");
        assertInvalidParse("(OWNER=\"foo\")");
        assertInvalidParse("(OWNER=\"Foo Bar\")");
        assertInvalidParse("(OWNER=\"Vito \"\"The Don\"\" Corleone\")");
    }

    public void testBetweenStrings() throws ANTLRException
    {
        assertInvalidParse("(OWNER=abc-xyz)");
    }

    public void testLessThanString() throws ANTLRException
    {
        assertInvalidParse("(OWNER=xyz-)");
    }

    public void testGreaterThanString() throws ANTLRException
    {
        assertInvalidParse("(OWNER=xyz+)");
    }

    public void testNumber() throws ANTLRException
    {
        assertInvalidParse("(LP=5)");
        assertInvalidParse("(LP=5.)");
        assertInvalidParse("(LP=5.0)");
    }

    public void testPeriod() throws ANTLRException
    {
        assertInvalidParse("(LDATE=1970-01-01)");
        assertInvalidParse("(LDATE=01:02:03)");
        assertInvalidParse("(LDATE=1970-01-01T05:06:01.33)");
        assertInvalidParse("(LDATE=TODAY)");
        assertInvalidParse("(LDATE=NOW)");

        ParserResults results = parse("(OWNER=TODAY)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );

        DmqlStringList list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("TODAY"));
        assertEquals(list, sql);

        results = parse("(OWNER=NOW)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );

        assertNotNull(results);
        sql = results.getSqlConverter();
        list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("NOW"));
        assertEquals(list, sql);
    }

    public void testRangeList() throws ANTLRException
    {
        parse("(OWNER=1970-01-01-1980-01-01,1985-01-01-1995-01-01)");
        parse("(LP=50-100,150-200)");
        assertInvalidParse("(OWNER=abc-bar,foo-xyz)");

        parse("(OWNER=1970-01-01+,1980-01-01-)");
        parse("(LP=50+,60-)");
        assertInvalidParse("(OWNER=abc+,xyz-)");
    }

    public void testCompoundOr() throws ANTLRException
    {
        ParserResults results = parse("(AR=|BATV,GENVA)|(OWNER=foo)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"AR","OWNER"}, results );

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
        ParserResults results = parse("(AR=|BATV,GENVA),(OWNER=foo)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"AR","OWNER"}, results );

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
