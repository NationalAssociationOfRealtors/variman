/*
 */
package org.realtors.rets.server.dmql;

import org.realtors.rets.server.dmql.DmqlCompiler.ParserResults;

import antlr.ANTLRException;

public class Dmql2CompilerTest extends AbstractDmqlCompilerTest
{
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

    public void testDotEmptyWithString() throws ANTLRException
    {
        ParserResults results;
        
        results = parse("(OWNER=.EMPTY.)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );
        EmptyClause emptyClause = new EmptyClause("r_OWNER", DmqlFieldType.CHARACTER);
        SqlConverter expectedSql = emptyClause;
        assertEquals(expectedSql, sql);
    }

    public void testStringLiteral() throws ANTLRException
    {
        ParserResults results;

        results = parse("(OWNER=\"\")");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );
        DmqlStringList list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString(""));
        assertEquals(list, sql);

        results = parse("(OWNER=\"foo\")");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );
        list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("foo"));
        assertEquals(list, sql);

        results = parse("(OWNER=\"Foo Bar\")");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );
        list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("Foo Bar"));
        assertEquals(list, sql);

        results = parse("(OWNER=\"Vito \"\"The Don\"\" Corleone\")");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );
        list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("Vito \"The Don\" Corleone"));
        assertEquals(list, sql);
    }

    public void testNumber() throws ANTLRException
    {
        ParserResults results = parse("(LP=5)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LP"}, results );
        EqualClause equal =
            new EqualClause("r_LP", new StringSqlConverter("5"));
        assertEquals(equal, sql);
        
        results = parse("(LP=-5)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LP"}, results );
        equal =
            new EqualClause("r_LP", new StringSqlConverter("-5"));
        assertEquals(equal, sql);

        results = parse("(LP=5.)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LP"}, results );
        equal = new EqualClause("r_LP", new StringSqlConverter("5."));
        assertEquals(equal, sql);

        results = parse("(LP=5.0)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LP"}, results );
        equal = new EqualClause("r_LP", new StringSqlConverter("5.0"));
        assertEquals(equal, sql);

        results = parse("(LP=5-10)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LP"}, results );
        StringSqlConverter left = new StringSqlConverter("5");
        StringSqlConverter right = new StringSqlConverter("10");
        BetweenClause between = new BetweenClause("r_LP", left, right);
        OrClause or = new OrClause();
        or.add(between);
        assertEquals(or, sql);
        
        results = parse("(LP=-5-10)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LP"}, results );
        left = new StringSqlConverter("-5");
        right = new StringSqlConverter("10");
        between = new BetweenClause("r_LP", left, right);
        or = new OrClause();
        or.add(between);
        assertEquals(or, sql);
        
        results = parse("(LP=-5--10)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LP"}, results );
        left = new StringSqlConverter("-5");
        right = new StringSqlConverter("-10");
        between = new BetweenClause("r_LP", left, right);
        or = new OrClause();
        or.add(between);
        assertEquals(or, sql);
    }

    public void testPeriod() throws ANTLRException
    {
        ParserResults results = parse("(LDATE=1970-01-01)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );
        EqualClause equal =
            new EqualClause("r_LDATE", new DateSqlConverter("1970-01-01"));
        assertEquals(equal, sql);

        results = parse("(LDATE=01:02:03)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );
        equal = new EqualClause("r_LDATE", new TimeSqlConverter("01:02:03"));
        assertEquals(equal, sql);

        results = parse("(LDATE=1970-01-01T05:06:01.3)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );
        equal = new EqualClause(
            "r_LDATE", new DateTimeSqlConverter("1970-01-01T05:06:01.3"));
        assertEquals(equal, sql);

        results = parse("(LDATE=TODAY)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );
        equal = new EqualClause("r_LDATE", new DateSqlConverter());
        assertEquals(equal, sql);

        results = parse("(LDATE=NOW)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );
        equal = new EqualClause("r_LDATE", new DateTimeSqlConverter());
        assertEquals(equal, sql);

        results = parse("(OWNER=TODAY)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );
        DmqlStringList list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("TODAY"));
        assertEquals(list, sql);

        results = parse("(OWNER=NOW)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );
        list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("NOW"));
        assertEquals(list, sql);
    }

    public void testBetweenStrings() throws ANTLRException
    {
        ParserResults results = parse("(OWNER=abc-xyz)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );
        QuotedSqlConverter left = new QuotedSqlConverter("abc");
        QuotedSqlConverter right = new QuotedSqlConverter("xyz");
        BetweenClause between = new BetweenClause("r_OWNER", left, right);
        OrClause or = new OrClause();
        or.add(between);
        assertEquals(or, sql);
    }

    public void testLessThanString() throws ANTLRException
    {
        ParserResults results = parse("(OWNER=xyz-)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );
        LessThanClause lessThan =
            new LessThanClause("r_OWNER", new QuotedSqlConverter("xyz"));
        OrClause or = new OrClause();
        or.add(lessThan);
        assertEquals(or, sql);
    }
    
    public void testLessThanNumber() throws ANTLRException
    {
        ParserResults results = parse("(LP=-5-)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LP"}, results );
        LessThanClause lessThan =
            new LessThanClause("r_LP", new StringSqlConverter("-5"));
        OrClause or = new OrClause();
        or.add(lessThan);
        assertEquals(or, sql);       
    }

    public void testGreaterThanString() throws ANTLRException
    {
        ParserResults results = parse("(OWNER=xyz+)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );
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
