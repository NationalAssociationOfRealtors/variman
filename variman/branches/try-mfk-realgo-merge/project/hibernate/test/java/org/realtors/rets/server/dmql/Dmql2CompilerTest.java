/*
 */
package org.realtors.rets.server.dmql;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.realtors.rets.server.dmql.DmqlCompiler.ParserResults;

import antlr.ANTLRException;

public class Dmql2CompilerTest extends AbstractDmqlCompilerTest
{
    /**
     * This method is expected and recognized by JUnit 2.0 or newer.
     *
     * @return the test to be run by test runners expecting this method to
     *         exist.
     */
    public static Test suite() { 
        return new TestSuite(Dmql2CompilerTest.class); 
    }

    public Dmql2CompilerTest()
    {
        super();
    }

    protected ParserResults parse(String dmql, boolean traceParser,
                                 boolean traceLexer)
        throws ANTLRException
    {
        return DmqlCompiler.parseDmql2(dmql, mMetadata, traceParser,
                                       traceLexer);
    }

    protected ParserResults parse(String dmql) throws ANTLRException
    {
        return parse(dmql, false, false);
    }

    public void testStringLiteral() throws ANTLRException
    {
        ParserResults parserResults = parse("(OWNER=\"\")");
        SqlConverter sql = parserResults.getSqlConverter();
        DmqlStringList list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString(""));
        assertEquals(list, sql);

        parserResults = parse("(OWNER=\"foo\")");
        sql = parserResults.getSqlConverter();
        list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("foo"));
        assertEquals(list, sql);

        parserResults = parse("(OWNER=\"Foo Bar\")");
        sql = parserResults.getSqlConverter();
        list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("Foo Bar"));
        assertEquals(list, sql);

        parserResults = parse("(OWNER=\"Vito \"\"The Don\"\" Corleone\")");
        sql = parserResults.getSqlConverter();
        list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("Vito \"The Don\" Corleone"));
        assertEquals(list, sql);
    }

    public void testNumber() throws ANTLRException
    {
        ParserResults parserResults = parse("(LP=5)");
        SqlConverter sql = parserResults.getSqlConverter();
        EqualClause equal =
            new EqualClause("r_LP", new StringSqlConverter("5"));
        assertEquals(equal, sql);

        parserResults = parse("(LP=5.)");
        sql = parserResults.getSqlConverter();
        equal = new EqualClause("r_LP", new StringSqlConverter("5."));
        assertEquals(equal, sql);

        parserResults = parse("(LP=5.0)");
        sql = parserResults.getSqlConverter();
        equal = new EqualClause("r_LP", new StringSqlConverter("5.0"));
        assertEquals(equal, sql);

        parserResults = parse("(LP=5-10)");
        sql = parserResults.getSqlConverter();
        StringSqlConverter left = new StringSqlConverter("5");
        StringSqlConverter right = new StringSqlConverter("10");
        BetweenClause between = new BetweenClause("r_LP", left, right);
        OrClause or = new OrClause();
        or.add(between);
        assertEquals(or, sql);
    }

    public void testPeriod() throws ANTLRException
    {
        ParserResults parserResults = parse("(LDATE=1970-01-01)");
        SqlConverter sql = parserResults.getSqlConverter();
        EqualClause equal =
            new EqualClause("r_LDATE", new DateSqlConverter("1970-01-01"));
        assertEquals(equal, sql);

        parserResults = parse("(LDATE=01:02:03)");
        sql = parserResults.getSqlConverter();
        equal = new EqualClause("r_LDATE", new TimeSqlConverter("01:02:03"));
        assertEquals(equal, sql);

        parserResults = parse("(LDATE=1970-01-01T05:06:01.33)");
        sql = parserResults.getSqlConverter();
        equal = new EqualClause(
            "r_LDATE", new DateTimeSqlConverter("1970-01-01T05:06:01.33"));
        assertEquals(equal, sql);

        parserResults = parse("(LDATE=TODAY)");
        sql = parserResults.getSqlConverter();
        equal = new EqualClause("r_LDATE", new DateSqlConverter());
        assertEquals(equal, sql);

        parserResults = parse("(LDATE=NOW)");
        sql = parserResults.getSqlConverter();
        equal = new EqualClause("r_LDATE", new DateTimeSqlConverter());
        assertEquals(equal, sql);

        parserResults = parse("(OWNER=TODAY)");
        sql = parserResults.getSqlConverter();
        DmqlStringList list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("TODAY"));
        assertEquals(list, sql);

        parserResults = parse("(OWNER=NOW)");
        sql = parserResults.getSqlConverter();
        list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("NOW"));
        assertEquals(list, sql);
    }

    public void testBetweenStrings() throws ANTLRException
    {
        ParserResults parserResults = parse("(OWNER=abc-xyz)");
        SqlConverter sql = parserResults.getSqlConverter();
        QuotedSqlConverter left = new QuotedSqlConverter("abc");
        QuotedSqlConverter right = new QuotedSqlConverter("xyz");
        BetweenClause between = new BetweenClause("r_OWNER", left, right);
        OrClause or = new OrClause();
        or.add(between);
        assertEquals(or, sql);
    }

    public void testLessThanString() throws ANTLRException
    {
        ParserResults parserResults = parse("(OWNER=xyz-)");
        SqlConverter sql = parserResults.getSqlConverter();
        LessThanClause lessThan =
            new LessThanClause("r_OWNER", new QuotedSqlConverter("xyz"));
        OrClause or = new OrClause();
        or.add(lessThan);
        assertEquals(or, sql);
    }

    public void testGreaterThanString() throws ANTLRException
    {
        ParserResults parserResults = parse("(OWNER=xyz+)");
        SqlConverter sql = parserResults.getSqlConverter();
        GreaterThanClause greaterThan =
            new GreaterThanClause("r_OWNER", new QuotedSqlConverter("xyz"));
        OrClause or = new OrClause();
        or.add(greaterThan);
        assertEquals(or, sql);
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
    
}
