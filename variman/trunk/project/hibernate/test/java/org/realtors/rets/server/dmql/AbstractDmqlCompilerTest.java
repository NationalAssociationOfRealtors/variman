/*
 */
package org.realtors.rets.server.dmql;

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
        mMetadata.addString("LDATE");
        mMetadata.addString("REM");
        mMetadata.addString("OWNER");
    }

    protected abstract SqlConverter parse(String dmql, boolean traceParser,
                                          boolean traceLexer)
        throws ANTLRException;

    protected abstract SqlConverter parse(String dmql) throws ANTLRException;

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

    public void testBetweenPeriods() throws ANTLRException
    {
        SqlConverter sql = parse("(LDATE=1970-01-01-1980-01-01)");
        StringSqlConverter left = new StringSqlConverter("1970-01-01");
        StringSqlConverter right = new StringSqlConverter("1980-01-01");
        BetweenClause between = new BetweenClause("r_LDATE", left, right);
        OrClause or = new OrClause();
        or.add(between);
        assertEquals(or, sql);

        sql = parse("(LDATE=1990-01-01-TODAY)");
        left = new StringSqlConverter("1990-01-01");
        right = new StringSqlConverter("TODAY");
        between = new BetweenClause("r_LDATE", left, right);
        or = new OrClause();
        or.add(between);
        assertEquals(or, sql);

        sql = parse("(LDATE=1990-01-01T05:06:07.000-NOW)");
        left = new StringSqlConverter("1990-01-01T05:06:07.000");
        right = new StringSqlConverter("NOW");
        between = new BetweenClause("r_LDATE", left, right);
        or = new OrClause();
        or.add(between);
        assertEquals(or, sql);
    }

    public void testBetweenNubmers() throws ANTLRException
    {
        SqlConverter sql = parse("(LP=50-100)");
        StringSqlConverter left = new StringSqlConverter("50");
        StringSqlConverter right = new StringSqlConverter("100");
        BetweenClause between = new BetweenClause("r_LP", left, right);
        OrClause or = new OrClause();
        or.add(between);
        assertEquals(or, sql);
    }

    public void testBetweenStrings() throws ANTLRException
    {
        assertInvalidParse("(OWNER=abc-xyz)");
    }

    public void testLessThanNumber() throws ANTLRException
    {
        SqlConverter sql = parse("(LP=50-)");
        LessThanClause lessThan =
            new LessThanClause("r_LP", new StringSqlConverter("50"));
        OrClause or = new OrClause();
        or.add(lessThan);
        assertEquals(or, sql);
    }

    public void testLessThanPeriod() throws ANTLRException
    {
        parse("(OWNER=1970-01-01-)");
        parse("(OWNER=TODAY-)");
        parse("(OWNER=NOW-)");
    }

    public void testLessThanString() throws ANTLRException
    {
        assertInvalidParse("(OWNER=xyz-)");
    }

    public void testGreaterThanNumber() throws ANTLRException
    {
        SqlConverter sql = parse("(LP=50+)");
        GreaterThanClause greaterThan =
            new GreaterThanClause("r_LP", new StringSqlConverter("50"));
        OrClause or = new OrClause();
        or.add(greaterThan);
        assertEquals(or, sql);
    }

    public void testGreaterThanPeriod() throws ANTLRException
    {
        parse("(OWNER=1970-01-01+)");
        parse("(OWNER=TODAY+)");
        parse("(OWNER=NOW+)");
    }

    public void testGreaterThanString() throws ANTLRException
    {
        assertInvalidParse("(OWNER=xyz+)");
    }

    protected SimpleDmqlMetadata mMetadata;
}
