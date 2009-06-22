/*
 */
package org.realtors.rets.server.dmql;

import java.util.Set;

import org.realtors.rets.server.dmql.DmqlCompiler.ParserResults;

import antlr.ANTLRException;
import antlr.RecognitionException;
import junit.framework.TestCase;

public abstract class AbstractDmqlCompilerTest extends TestCase
{
    public AbstractDmqlCompilerTest()
    {
        mMetadata = new SimpleDmqlMetadata();
        mMetadata.addLookup("AR", new String[]{"GENVA", "BATV"});
        mMetadata.addLookup("STATUS",
                            new String[]{"A", "O", "S", "U", "C", "W", "P"});
        mMetadata.addLookup("OR",
                            new String[]{"OR", "AND", "NOT", "TODAY", "NOW"});
        mMetadata.addLookupMulti("IF",
                                 new String[]{"FP", "HEAT", "COOL"});
        mMetadata.addString("AND");
        mMetadata.addString("NOT");

        mMetadata.addLookup("AREA", new String[]{"1", "2"});
        mMetadata.addString("CITY");

        mMetadata.addLookup("ST", new String[]{"ACT", "SOLD"});
        mMetadata.addLookup("EXT", new String[]{"WTRFRNT", "DOCK"});
        mMetadata.addLookup("TYPE", new String[]{"CONDO", "TWNHME"});
        mMetadata.addNumeric("LP");
        mMetadata.addString("STR");
        mMetadata.addString("STYLE");
        mMetadata.addTemporal("LDATE");
        mMetadata.addString("REM");
        mMetadata.addString("OWNER");
    }

    protected abstract ParserResults parse(String dmql, boolean traceParser,
                                          boolean traceLexer)
        throws ANTLRException;

    protected abstract ParserResults parse(String dmql) throws ANTLRException;

    protected void assertInvalidParse(String dmql) throws ANTLRException
    {
        assertInvalidParse(dmql, false, false);
    }

    private void assertInvalidParse(String dmql, boolean traceParser,
                                    boolean traceLexer)
        throws ANTLRException
    {
        try
        {
            parse(dmql, traceParser, traceLexer);
            fail("Should have thrown an exception");
        }
        catch (RecognitionException e)
        {
            // Expected
        }
    }

    public void testBetweenDates() throws ANTLRException
    {
        ParserResults parserResults = parse("(LDATE=1970-01-01-1980-01-01)");
        SqlConverter sql = parserResults.getSqlConverter();
        SqlConverter left = new DateSqlConverter("1970-01-01");
        SqlConverter right = new DateSqlConverter("1980-01-01");
        BetweenClause between = new BetweenClause("r_LDATE", left, right);
        OrClause or = new OrClause();
        or.add(between);
        assertEquals(or, sql);

        parserResults = parse("(LDATE=1990-01-01-TODAY)");
        sql = parserResults.getSqlConverter();
        left = new DateSqlConverter("1990-01-01");
        right = new DateSqlConverter();
        between = new BetweenClause("r_LDATE", left, right);
        or = new OrClause();
        or.add(between);
        assertEquals(or, sql);
    }

    public void testBetweenDateTimes() throws ANTLRException
    {
        ParserResults parserResults = parse(
                "(LDATE=1990-01-01T05:06:07-1991-01-01T05:06:07.000)");
        SqlConverter sql = parserResults.getSqlConverter();
        // The milliseconds are deliberately different as an additional test
        SqlConverter left = new DateTimeSqlConverter("1990-01-01T05:06:07.000");
        SqlConverter right = new DateTimeSqlConverter("1991-01-01T05:06:07");
        BetweenClause between = new BetweenClause("r_LDATE", left, right);
        OrClause or = new OrClause();
        or.add(between);
        assertEquals(or, sql);

        parserResults = parse("(LDATE=1990-01-01T05:06:07.000-NOW)");
        sql = parserResults.getSqlConverter();
        left = new DateTimeSqlConverter("1990-01-01T05:06:07.000");
        right = new DateTimeSqlConverter();
        between = new BetweenClause("r_LDATE", left, right);
        or = new OrClause();
        or.add(between);
        assertEquals(or, sql);
    }

    public void testBetweenTimes() throws ANTLRException
    {
        ParserResults parserResults = parse("(LDATE=05:06:07-13:57:02.468)");
        SqlConverter sql = parserResults.getSqlConverter();
        SqlConverter left = new TimeSqlConverter("05:06:07");
        SqlConverter right = new TimeSqlConverter("13:57:02.468");
        BetweenClause between = new BetweenClause("r_LDATE", left, right);
        OrClause or = new OrClause();
        or.add(between);
        assertEquals(or, sql);
    }

    public void testBetweenNubmers() throws ANTLRException
    {
        ParserResults parserResults = parse("(LP=50-100)");
        SqlConverter sql = parserResults.getSqlConverter();
        StringSqlConverter left = new StringSqlConverter("50");
        StringSqlConverter right = new StringSqlConverter("100");
        BetweenClause between = new BetweenClause("r_LP", left, right);
        OrClause or = new OrClause();
        or.add(between);
        assertEquals(or, sql);
    }

    public void testLessThanNumber() throws ANTLRException
    {
        ParserResults parserResults = parse("(LP=50-)");
        SqlConverter sql = parserResults.getSqlConverter();
        LessThanClause lessThan =
            new LessThanClause("r_LP", new StringSqlConverter("50"));
        OrClause or = new OrClause();
        or.add(lessThan);
        assertEquals(or, sql);
    }

    public void testLessThanDate() throws ANTLRException
    {
        ParserResults parserResults = parse("(LDATE=1970-01-01-)");
        SqlConverter sql = parserResults.getSqlConverter();
        LessThanClause lessThan =
            new LessThanClause("r_LDATE", new DateSqlConverter("1970-01-01"));
        OrClause or = new OrClause();
        or.add(lessThan);
        assertEquals(or, sql);

        parserResults = parse("(LDATE=TODAY-)");
        sql = parserResults.getSqlConverter();
        lessThan =
            new LessThanClause("r_LDATE", new DateSqlConverter());
        or = new OrClause();
        or.add(lessThan);
        assertEquals(or, sql);
    }

    public void testLessThanDateTime() throws ANTLRException
    {
        ParserResults parserResults = parse("(LDATE=1990-01-01T05:06:07-)");
        SqlConverter sql = parserResults.getSqlConverter();
        LessThanClause lessThan =
            new LessThanClause(
                "r_LDATE", new DateTimeSqlConverter("1990-01-01T05:06:07"));
        OrClause or = new OrClause();
        or.add(lessThan);
        assertEquals(or, sql);

        parserResults = parse("(LDATE=NOW-)");
        sql = parserResults.getSqlConverter();
        lessThan = new LessThanClause("r_LDATE", new DateTimeSqlConverter());
        or = new OrClause();
        or.add(lessThan);
        assertEquals(or, sql);
    }

    public void testLessThanTime() throws ANTLRException
    {
        ParserResults parserResults = parse("(LDATE=13:57:02.468-)");
        SqlConverter sql = parserResults.getSqlConverter();
        LessThanClause lessThan =
            new LessThanClause("r_LDATE", new TimeSqlConverter("13:57:02.468"));
        OrClause or = new OrClause();
        or.add(lessThan);
        assertEquals(or, sql);
    }

    public void testGreaterThanNumber() throws ANTLRException
    {
        ParserResults parserResults = parse("(LP=50+)");
        SqlConverter sql = parserResults.getSqlConverter();
        GreaterThanClause greaterThan =
            new GreaterThanClause("r_LP", new StringSqlConverter("50"));
        OrClause or = new OrClause();
        or.add(greaterThan);
        assertEquals(or, sql);
    }

    public void testGreaterThanDate() throws ANTLRException
    {
        ParserResults parserResults = parse("(LDATE=1970-01-01+)");
        SqlConverter sql = parserResults.getSqlConverter();
        GreaterThanClause greaterThan =
            new GreaterThanClause("r_LDATE",
                                  new DateSqlConverter("1970-01-01"));
        OrClause or = new OrClause();
        or.add(greaterThan);
        assertEquals(or, sql);

        parserResults = parse("(LDATE=TODAY+)");
        sql = parserResults.getSqlConverter();
        greaterThan =
            new GreaterThanClause("r_LDATE", new DateSqlConverter());
        or = new OrClause();
        or.add(greaterThan);
        assertEquals(or, sql);
    }

    public void testGreaterThanDateTime() throws ANTLRException
    {
        ParserResults parserResults = parse("(LDATE=1990-01-01T05:06:07+)");
        SqlConverter sql = parserResults.getSqlConverter();
        GreaterThanClause greaterThan =
            new GreaterThanClause(
                "r_LDATE", new DateTimeSqlConverter("1990-01-01T05:06:07"));
        OrClause or = new OrClause();
        or.add(greaterThan);
        assertEquals(or, sql);

        parserResults = parse("(LDATE=NOW+)");
        sql = parserResults.getSqlConverter();
        greaterThan =
            new GreaterThanClause("r_LDATE", new DateTimeSqlConverter());
        or = new OrClause();
        or.add(greaterThan);
        assertEquals(or, sql);
    }

    public void testGreaterThanTime() throws ANTLRException
    {
        ParserResults parserResults = parse("(LDATE=13:57:02.468+)");
        SqlConverter sql = parserResults.getSqlConverter();
        GreaterThanClause greaterThan =
            new GreaterThanClause(
                "r_LDATE", new TimeSqlConverter("13:57:02.468"));
        OrClause or = new OrClause();
        or.add(greaterThan);
        assertEquals(or, sql);
    }

    public void testStringEquals() throws ANTLRException
    {
        ParserResults parserResults = parse("(OWNER=foo)");
        SqlConverter sql = parserResults.getSqlConverter();
        DmqlStringList list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("foo"));
        assertEquals(list, sql);
    }

    public void testStringEqualsDigitsOnly() throws ANTLRException
    {
        ParserResults parserResults = parse("(OWNER=12345)");
        SqlConverter sql = parserResults.getSqlConverter();
        DmqlStringList list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("12345"));
        assertEquals(list, sql);
    }

    public void testStringEqualsDigitFirst() throws ANTLRException
    {
        ParserResults parserResults = parse("(OWNER=0TRLH)");
        SqlConverter sqlConverter = parserResults.getSqlConverter();
        DmqlStringList list = new DmqlStringList("r_OWNER");
        DmqlString string = new DmqlString();
        string.add("0");
        string.add("TRLH");
        list.add(string);
        assertEquals(list, sqlConverter);
    }

    public void testStringStart() throws ANTLRException
    {
        ParserResults parserResults = parse("(OWNER=f*)");
        SqlConverter sql = parserResults.getSqlConverter();
        DmqlStringList list = new DmqlStringList("r_OWNER");
        DmqlString string = new DmqlString();
        string.add("f");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        list.add(string);
        assertEquals(list, sql);
    }

    public void testStringContains() throws ANTLRException
    {
        ParserResults parserResults = parse("(OWNER=*foo*)");
        SqlConverter sql = parserResults.getSqlConverter();
        DmqlStringList list = new DmqlStringList("r_OWNER");
        DmqlString string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        string.add("foo");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        list.add(string);
        assertEquals(list, sql);
    }

    public void testStringContainsDigitsFirst() throws ANTLRException
    {
        ParserResults parserResults = parse("(OWNER=*100th*)");
        SqlConverter sql = parserResults.getSqlConverter();
        DmqlStringList list = new DmqlStringList("r_OWNER");
        DmqlString string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        string.add("100");
        string.add("th");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        list.add(string);
        assertEquals(list, sql);
    }

    public void testStringStartsWithDigitFirst() throws ANTLRException
    {
        ParserResults parserResults = parse("(OWNER=3*)");
        SqlConverter sqlConverter = parserResults.getSqlConverter();
        DmqlStringList list = new DmqlStringList("r_OWNER");
        DmqlString string = new DmqlString();
        string.add("3");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        list.add(string);
        assertEquals(list, sqlConverter);
    }

    public void testInvalidStringContains() throws ANTLRException
    {
        assertInvalidParse("OWNER=**");
    }

    public void testStringContainsWithSpaces() throws ANTLRException
    {
        ParserResults parserResults = parse("(OWNER=*Foo%20Bar*)");
        SqlConverter sql = parserResults.getSqlConverter();
        DmqlStringList list = new DmqlStringList("r_OWNER");
        DmqlString string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        string.add("Foo Bar");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        list.add(string);
        assertEquals(list, sql);
    }

    public void testStringChar() throws ANTLRException
    {
        ParserResults parserResults = parse("(OWNER=f?o)");
        SqlConverter sql = parserResults.getSqlConverter();
        DmqlStringList list = new DmqlStringList("r_OWNER");
        DmqlString string = new DmqlString();
        string.add("f");
        string.add(DmqlString.MATCH_ZERO_OR_ONE);
        string.add("o");
        list.add(string);
        assertEquals(list, sql);

        parserResults = parse("(OWNER=?oo)");
        sql = parserResults.getSqlConverter();
        list = new DmqlStringList("r_OWNER");
        string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_ONE);
        string.add("oo");
        list.add(string);
        assertEquals(list, sql);

        parserResults = parse("(OWNER=fo?)");
        sql = parserResults.getSqlConverter();
        list = new DmqlStringList("r_OWNER");
        string = new DmqlString();
        string.add("fo");
        string.add(DmqlString.MATCH_ZERO_OR_ONE);
        list.add(string);
        assertEquals(list, sql);

        parserResults = parse("(OWNER=?)");
        sql = parserResults.getSqlConverter();
        list = new DmqlStringList("r_OWNER");
        string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_ONE);
        list.add(string);
        assertEquals(list, sql);
    }

    public void testStringList() throws ANTLRException
    {
        ParserResults parserResults = parse("(OWNER=foo,f*,*foo*,f?o,50)");
        SqlConverter sql = parserResults.getSqlConverter();
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

        string = new DmqlString();
        string.add("50");
        list.add(string);

        assertEquals(list, sql);
    }

    public void testLookupOr() throws ANTLRException
    {
        ParserResults parserResults = parse("(AR=|GENVA,BATV)");
        SqlConverter sql = parserResults.getSqlConverter();
        LookupList lookup = new LookupList(LookupListType.OR, "r_AR");
        lookup.addLookup("GENVA");
        lookup.addLookup("BATV");
        assertEquals(lookup, sql);
    }

    public void testImpliedLookupOr() throws ANTLRException
    {
        ParserResults parserResults = parse("(STATUS=A)");
        SqlConverter sql = parserResults.getSqlConverter();
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
        ParserResults parserResults = parse("(STATUS=+A,S)");
        SqlConverter sql = parserResults.getSqlConverter();
        LookupList lookup = new LookupList(LookupListType.AND, "r_STATUS");
        lookup.addLookup("A");
        lookup.addLookup("S");
        assertEquals(lookup, sql);
    }

    public void testLookupNot() throws ANTLRException
    {
        ParserResults parserResults = parse("(STATUS=~A,S)");
        SqlConverter sql = parserResults.getSqlConverter();
        LookupList lookup = new LookupList(LookupListType.NOT, "r_STATUS");
        lookup.addLookup("A");
        lookup.addLookup("S");
        assertEquals(lookup, sql);
    }

    public void testLookupMultiOr() throws ANTLRException
    {
        ParserResults parserResults = parse("(IF=|FP,COOL)");
        SqlConverter sql = parserResults.getSqlConverter();
        LookupList lookup = new LookupList(LookupListType.OR, "r_IF");
        lookup.setLookupMulti(true);
        lookup.addLookup("FP");
        lookup.addLookup("COOL");
        assertEquals(lookup, sql);
    }

    public void testLookupMultiAnd() throws ANTLRException
    {
        ParserResults parserResults = parse("(IF=+FP,COOL)");
        SqlConverter sql = parserResults.getSqlConverter();
        LookupList lookup = new LookupList(LookupListType.AND, "r_IF");
        lookup.setLookupMulti(true);
        lookup.addLookup("FP");
        lookup.addLookup("COOL");
        assertEquals(lookup, sql);
    }

    public void testLookupMultiNot() throws ANTLRException
    {
        ParserResults parserResults = parse("(IF=~FP,COOL)");
        SqlConverter sql = parserResults.getSqlConverter();
        LookupList lookup = new LookupList(LookupListType.NOT, "r_IF");
        lookup.setLookupMulti(true);
        lookup.addLookup("FP");
        lookup.addLookup("COOL");
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

    public void testFoundFields() throws ANTLRException
    {
        ParserResults parserResults = parse("(ST=|ACT,SOLD),\n" +
                "(LP=200000-350000),\n" +
                "(STR=RIVER),\n" +
                "(STYLE=RANCH),\n" +
                "(EXT=+WTRFRNT,DOCK),\n" +
                "(LDATE=2000-03-01+),\n" +
                "(REM=*FORECLOSE*),\n" +
                "(TYPE=~CONDO,TWNHME),\n" +
                "(OWNER=P?LE)");
        
        Set/*String*/ foundFields = parserResults.getFoundFields();
        assertTrue(foundFields.contains("ST"));
        assertTrue(foundFields.contains("LP"));
        assertTrue(foundFields.contains("STR"));
        assertTrue(foundFields.contains("STYLE"));
        assertTrue(foundFields.contains("EXT"));
        assertTrue(foundFields.contains("LDATE"));
        assertTrue(foundFields.contains("REM"));
        assertTrue(foundFields.contains("TYPE"));
        assertTrue(foundFields.contains("OWNER"));
        assertTrue(foundFields.size() == 9);
    }
    
    protected SimpleDmqlMetadata mMetadata;
}
