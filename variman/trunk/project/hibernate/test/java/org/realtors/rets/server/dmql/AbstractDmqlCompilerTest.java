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

    protected void verifyFoundFieldsMatches( String[] expectedFields, ParserResults results )
    {
        Set<String> foundFields = results.getFoundFields();
        if (expectedFields == null || expectedFields.length == 0)
        {
            assertTrue(foundFields == null || foundFields.size() == 0);
        }
        else
        {
            assertNotNull( foundFields );
            assertTrue( foundFields.size() == expectedFields.length );
            for( String field : expectedFields ) {
                assertTrue(foundFields.contains(field));
            }
        }
    }

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
        ParserResults results = parse("(LDATE=1970-01-01-1980-01-01)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );

        SqlConverter left = new DateSqlConverter("1970-01-01");
        SqlConverter right = new DateSqlConverter("1980-01-01");
        BetweenClause between = new BetweenClause("r_LDATE", left, right);
        OrClause or = new OrClause();
        or.add(between);
        assertEquals(or, sql);

        results = parse("(LDATE=1990-01-01-TODAY)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );

        left = new DateSqlConverter("1990-01-01");
        right = new DateSqlConverter();
        between = new BetweenClause("r_LDATE", left, right);
        or = new OrClause();
        or.add(between);
        assertEquals(or, sql);
    }

    public void testBetweenDateTimes() throws ANTLRException
    {
        ParserResults results = parse(
            "(LDATE=1990-01-01T05:06:07-00:00-1991-01-01T05:06:07.0Z)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );

        // The milliseconds are deliberately different as an additional test
        SqlConverter left = new DateTimeSqlConverter("1990-01-01T05:06:07.0Z");
        SqlConverter right = new DateTimeSqlConverter("1991-01-01T05:06:07-00:00");
        BetweenClause between = new BetweenClause("r_LDATE", left, right);
        OrClause or = new OrClause();
        or.add(between);
        assertEquals(or, sql);

        results = parse("(LDATE=1990-01-01T05:06:07.0Z-NOW)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );

        left = new DateTimeSqlConverter("1990-01-01T05:06:07.0Z");
        right = new DateTimeSqlConverter();
        between = new BetweenClause("r_LDATE", left, right);
        or = new OrClause();
        or.add(between);
        assertEquals(or, sql);
    }

    public void testBetweenTimes() throws ANTLRException
    {
        ParserResults results = parse("(LDATE=05:06:07-03:00-13:57:02.4Z)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );

        SqlConverter left = new TimeSqlConverter("05:06:07-03:00");
        SqlConverter right = new TimeSqlConverter("13:57:02.4Z");
        BetweenClause between = new BetweenClause("r_LDATE", left, right);
        OrClause or = new OrClause();
        or.add(between);
        assertEquals(or, sql);
    }

    public void testBetweenNumbers() throws ANTLRException
    {
        ParserResults results = parse("(LP=50-100)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LP"}, results );

        StringSqlConverter left = new StringSqlConverter("50");
        StringSqlConverter right = new StringSqlConverter("100");
        BetweenClause between = new BetweenClause("r_LP", left, right);
        OrClause or = new OrClause();
        or.add(between);
        assertEquals(or, sql);
    }

    public void testLessThanNumber() throws ANTLRException
    {
        ParserResults results = parse("(LP=50-)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LP"}, results );

        LessThanClause lessThan =
            new LessThanClause("r_LP", new StringSqlConverter("50"));
        OrClause or = new OrClause();
        or.add(lessThan);
        assertEquals(or, sql);
    }

    public void testLessThanDate() throws ANTLRException
    {
        ParserResults results = parse("(LDATE=1970-01-01-)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );

        LessThanClause lessThan =
            new LessThanClause("r_LDATE", new DateSqlConverter("1970-01-01"));
        OrClause or = new OrClause();
        or.add(lessThan);
        assertEquals(or, sql);

        results = parse("(LDATE=TODAY-)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );

        lessThan =
            new LessThanClause("r_LDATE", new DateSqlConverter());
        or = new OrClause();
        or.add(lessThan);
        assertEquals(or, sql);
    }

    public void testLessThanDateTime() throws ANTLRException
    {
        ParserResults results = parse("(LDATE=1990-01-01T05:06:07Z-)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );

        LessThanClause lessThan =
            new LessThanClause(
                "r_LDATE", new DateTimeSqlConverter("1990-01-01T05:06:07Z"));
        OrClause or = new OrClause();
        or.add(lessThan);
        assertEquals(or, sql);

        results = parse("(LDATE=NOW-)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );

        lessThan = new LessThanClause("r_LDATE", new DateTimeSqlConverter());
        or = new OrClause();
        or.add(lessThan);
        assertEquals(or, sql);
    }

    public void testLessThanTime() throws ANTLRException
    {
        ParserResults results = parse("(LDATE=13:57:02.4Z-)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );

        LessThanClause lessThan =
            new LessThanClause("r_LDATE", new TimeSqlConverter("13:57:02.4Z"));
        OrClause or = new OrClause();
        or.add(lessThan);
        assertEquals(or, sql);
    }

    public void testGreaterThanNumber() throws ANTLRException
    {
        ParserResults results = parse("(LP=50+)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LP"}, results );

        GreaterThanClause greaterThan =
            new GreaterThanClause("r_LP", new StringSqlConverter("50"));
        OrClause or = new OrClause();
        or.add(greaterThan);
        assertEquals(or, sql);
    }

    public void testGreaterThanDate() throws ANTLRException
    {
        ParserResults results = parse("(LDATE=1970-01-01+)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );

        GreaterThanClause greaterThan =
            new GreaterThanClause("r_LDATE",
                                  new DateSqlConverter("1970-01-01"));
        OrClause or = new OrClause();
        or.add(greaterThan);
        assertEquals(or, sql);

        results = parse("(LDATE=TODAY+)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );

        greaterThan =
            new GreaterThanClause("r_LDATE", new DateSqlConverter());
        or = new OrClause();
        or.add(greaterThan);
        assertEquals(or, sql);
    }

    public void testGreaterThanDateTime() throws ANTLRException
    {
        ParserResults results = parse("(LDATE=1990-01-01T05:06:07Z+)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );

        GreaterThanClause greaterThan =
            new GreaterThanClause(
                "r_LDATE", new DateTimeSqlConverter("1990-01-01T05:06:07Z"));
        OrClause or = new OrClause();
        or.add(greaterThan);
        assertEquals(or, sql);

        results = parse("(LDATE=NOW+)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );

        greaterThan =
            new GreaterThanClause("r_LDATE", new DateTimeSqlConverter());
        or = new OrClause();
        or.add(greaterThan);
        assertEquals(or, sql);
    }

    public void testGreaterThanTime() throws ANTLRException
    {
        ParserResults results = parse("(LDATE=13:57:02.4Z+)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"LDATE"}, results );

        GreaterThanClause greaterThan =
            new GreaterThanClause(
                "r_LDATE", new TimeSqlConverter("13:57:02.4Z"));
        OrClause or = new OrClause();
        or.add(greaterThan);
        assertEquals(or, sql);
    }

    public void testStringEquals() throws ANTLRException
    {
        ParserResults results = parse("(OWNER=foo)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );

        DmqlStringList list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("foo"));
        assertEquals(list, sql);
    }

    public void testStringEqualsDigitsOnly() throws ANTLRException
    {
        ParserResults results = parse("(OWNER=12345)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );
        DmqlStringList list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("12345"));
        assertEquals(list, sql);

        results = parse("(OWNER=01234)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );
        list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("01234"));
        assertEquals(list, sql);
    }

    public void testStringBeginsWithDigits() throws ANTLRException
    {
        ParserResults results = parse("(OWNER=123ABC)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );
        DmqlStringList list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("123ABC"));
        assertEquals(list, sql);

        results = parse("(OWNER=01234)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );
        list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("01234"));
        assertEquals(list, sql);

        results = parse("(OWNER=0ABCD)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );
        list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("0ABCD"));
        assertEquals(list, sql);

        results = parse("(OWNER=123ABC456)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );
        list = new DmqlStringList("r_OWNER");
        list.add(new DmqlString("123ABC456"));
        assertEquals(list, sql);
    }

    public void testStringStart() throws ANTLRException
    {
        ParserResults results = parse("(OWNER=f*)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );

        DmqlStringList list = new DmqlStringList("r_OWNER");
        DmqlString string = new DmqlString();
        string.add("f");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        list.add(string);
        assertEquals(list, sql);
    }

    public void testStringContains() throws ANTLRException
    {
        ParserResults results = parse("(OWNER=*foo*)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );

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
        ParserResults results = parse("(OWNER=*100th*)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );

        DmqlStringList list = new DmqlStringList("r_OWNER");
        DmqlString string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        string.add("100th");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        list.add(string);
        assertEquals(list, sql);
    }

    public void testInvalidStringContains() throws ANTLRException
    {
        assertInvalidParse("OWNER=**");
    }

    public void testStringContainsWithSpaces() throws ANTLRException
    {
        ParserResults results = parse("(OWNER=*Foo%20Bar*)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );

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
        ParserResults results = parse("(OWNER=f?o)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );

        DmqlStringList list = new DmqlStringList("r_OWNER");
        DmqlString string = new DmqlString();
        string.add("f");
        string.add(DmqlString.MATCH_ZERO_OR_ONE);
        string.add("o");
        list.add(string);
        assertEquals(list, sql);

        results = parse("(OWNER=?oo)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );

        list = new DmqlStringList("r_OWNER");
        string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_ONE);
        string.add("oo");
        list.add(string);
        assertEquals(list, sql);

        results = parse("(OWNER=fo?)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );

        list = new DmqlStringList("r_OWNER");
        string = new DmqlString();
        string.add("fo");
        string.add(DmqlString.MATCH_ZERO_OR_ONE);
        list.add(string);
        assertEquals(list, sql);

        results = parse("(OWNER=?)");
        assertNotNull(results);
        sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );

        list = new DmqlStringList("r_OWNER");
        string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_ONE);
        list.add(string);
        assertEquals(list, sql);
    }

    public void testStringList() throws ANTLRException
    {
        ParserResults results = parse("(OWNER=foo,f*,*foo*,f?o,50)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );

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

    public void testStringListWithDigits() throws ANTLRException
    {
        ParserResults results = parse("(OWNER=098foo,f*,*6foo5*,4f?o,50)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"OWNER"}, results );

        DmqlStringList list = new DmqlStringList("r_OWNER");
        DmqlString string = new DmqlString();
        string.add("098foo");
        list.add(string);

        string = new DmqlString();
        string.add("f");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        list.add(string);

        string = new DmqlString();
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        string.add("6foo5");
        string.add(DmqlString.MATCH_ZERO_OR_MORE);
        list.add(string);

        string = new DmqlString();
        string.add("4f");
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
        ParserResults results = parse("(AR=|GENVA,BATV)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"AR"}, results );

        LookupList lookup = new LookupList(LookupListType.OR, "r_AR");
        lookup.addLookup("GENVA");
        lookup.addLookup("BATV");
        assertEquals(lookup, sql);
    }

    public void testImpliedLookupOr() throws ANTLRException
    {
        ParserResults results = parse("(STATUS=A)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"STATUS"}, results );

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
        ParserResults results = parse("(STATUS=+A,S)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"STATUS"}, results );

        LookupList lookup = new LookupList(LookupListType.AND, "r_STATUS");
        lookup.addLookup("A");
        lookup.addLookup("S");
        assertEquals(lookup, sql);
    }

    public void testLookupNot() throws ANTLRException
    {
        ParserResults results = parse("(STATUS=~A,S)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"STATUS"}, results );

        LookupList lookup = new LookupList(LookupListType.NOT, "r_STATUS");
        lookup.addLookup("A");
        lookup.addLookup("S");
        assertEquals(lookup, sql);
    }

    public void testLookupMultiOr() throws ANTLRException
    {
        ParserResults results = parse("(IF=|FP,COOL)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"IF"}, results );

        LookupList lookup = new LookupList(LookupListType.OR, "r_IF");
        lookup.setLookupMulti(true);
        lookup.addLookup("FP");
        lookup.addLookup("COOL");
        assertEquals(lookup, sql);
    }

    public void testLookupMultiAnd() throws ANTLRException
    {
        ParserResults results = parse("(IF=+FP,COOL)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"IF"}, results );

        LookupList lookup = new LookupList(LookupListType.AND, "r_IF");
        lookup.setLookupMulti(true);
        lookup.addLookup("FP");
        lookup.addLookup("COOL");
        assertEquals(lookup, sql);
    }

    public void testLookupMultiNot() throws ANTLRException
    {
        ParserResults results = parse("(IF=~FP,COOL)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"IF"}, results );

        LookupList lookup = new LookupList(LookupListType.NOT, "r_IF");
        lookup.setLookupMulti(true);
        lookup.addLookup("FP");
        lookup.addLookup("COOL");
        assertEquals(lookup, sql);
    }

    public void testMultipleFoundFields() throws ANTLRException
    {
        ParserResults results = parse("(IF=COOL),(STATUS=A),(AR=BATV)");
        assertNotNull(results);
        SqlConverter sql = results.getSqlConverter();
        verifyFoundFieldsMatches( new String[] {"IF","STATUS","AR"}, results );
    }

    public void testInvalidLookupName() throws ANTLRException
    {
        assertInvalidParse("(XX=|A,S)");
    }

    public void testInvalidLookupValue() throws ANTLRException
    {
        assertInvalidParse("(AR=|A,S)");
    }

    protected SimpleDmqlMetadata mMetadata;
}
