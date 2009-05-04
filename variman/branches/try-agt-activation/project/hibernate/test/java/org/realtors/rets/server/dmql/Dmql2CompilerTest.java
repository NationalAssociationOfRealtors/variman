/*
 */
package org.realtors.rets.server.dmql;

import antlr.ANTLRException;

public class Dmql2CompilerTest extends AbstractDmqlCompilerTest
{
    public Dmql2CompilerTest()
    {
        super();
    }

    protected SqlConverter parse(String dmql, boolean traceParser,
                                 boolean traceLexer)
        throws ANTLRException
    {
        return DmqlCompiler.parseDmql2(dmql, mMetadata, traceParser,
                                       traceLexer);
    }

    protected SqlConverter parse(String dmql) throws ANTLRException
    {
        return parse(dmql, false, false);
    }

    public void testStringLiteral() throws ANTLRException
    {
        SqlConverter sql;

        sql = parse("(OWNER=\"\")");
        DmqlStringList list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString(""));
        assertEquals(list, sql);

        sql = parse("(OWNER=\"foo\")");
        list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("foo"));
        assertEquals(list, sql);

        sql = parse("(OWNER=\"Foo Bar\")");
        list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("Foo Bar"));
        assertEquals(list, sql);

        sql = parse("(OWNER=\"Vito \"\"The Don\"\" Corleone\")");
        list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("Vito \"The Don\" Corleone"));
        assertEquals(list, sql);
    }

    public void testNumber() throws ANTLRException
    {
        SqlConverter sql = parse("(LP=5)");
        EqualClause equal =
            new EqualClause("r_LP", new StringSqlConverter("5"));
        assertEquals(equal, sql);

        sql = parse("(LP=5.)");
        equal = new EqualClause("r_LP", new StringSqlConverter("5."));
        assertEquals(equal, sql);

        sql = parse("(LP=5.0)");
        equal = new EqualClause("r_LP", new StringSqlConverter("5.0"));
        assertEquals(equal, sql);

        sql = parse("(LP=5-10)");
        StringSqlConverter left = new StringSqlConverter("5");
        StringSqlConverter right = new StringSqlConverter("10");
        BetweenClause between = new BetweenClause("r_LP", left, right);
        OrClause or = new OrClause();
        or.add(between);
        assertEquals(or, sql);
    }

    public void testPeriod() throws ANTLRException
    {
        SqlConverter sql = parse("(LDATE=1970-01-01)");
        EqualClause equal =
            new EqualClause("r_LDATE", new DateSqlConverter("1970-01-01"));
        assertEquals(equal, sql);

        sql = parse("(LDATE=01:02:03)");
        equal = new EqualClause("r_LDATE", new TimeSqlConverter("01:02:03"));
        assertEquals(equal, sql);

        sql = parse("(LDATE=1970-01-01T05:06:01.33)");
        equal = new EqualClause(
            "r_LDATE", new DateTimeSqlConverter("1970-01-01T05:06:01.33"));
        assertEquals(equal, sql);

        sql = parse("(LDATE=TODAY)");
        equal = new EqualClause("r_LDATE", new DateSqlConverter());
        assertEquals(equal, sql);

        sql = parse("(LDATE=NOW)");
        equal = new EqualClause("r_LDATE", new DateTimeSqlConverter());
        assertEquals(equal, sql);

        sql = parse("(OWNER=TODAY)");
        DmqlStringList list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("TODAY"));
        assertEquals(list, sql);

        sql = parse("(OWNER=NOW)");
        list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("NOW"));
        assertEquals(list, sql);
    }

    public void testBetweenStrings() throws ANTLRException
    {
        SqlConverter sql = parse("(OWNER=abc-xyz)");
        QuotedSqlConverter left = new QuotedSqlConverter("abc");
        QuotedSqlConverter right = new QuotedSqlConverter("xyz");
        BetweenClause between = new BetweenClause("r_OWNER", left, right);
        OrClause or = new OrClause();
        or.add(between);
        assertEquals(or, sql);
    }

    public void testLessThanString() throws ANTLRException
    {
        SqlConverter sql = parse("(OWNER=xyz-)");
        LessThanClause lessThan =
            new LessThanClause("r_OWNER", new QuotedSqlConverter("xyz"));
        OrClause or = new OrClause();
        or.add(lessThan);
        assertEquals(or, sql);
    }

    public void testGreaterThanString() throws ANTLRException
    {
        SqlConverter sql = parse("(OWNER=xyz+)");
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
