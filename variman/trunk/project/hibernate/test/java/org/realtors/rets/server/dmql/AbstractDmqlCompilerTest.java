/*
 */
package org.realtors.rets.server.dmql;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.Collection;

import junit.framework.TestCase;
import antlr.ANTLRException;
import antlr.RecognitionException;

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
        mMetadata.addString("LP");
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
        try
        {
            parse(dmql);
            fail("Should have thrown an exception");
        }
        catch (RecognitionException e)
        {
            // Expected
        }
    }

    protected SimpleDmqlMetadata mMetadata;
}
