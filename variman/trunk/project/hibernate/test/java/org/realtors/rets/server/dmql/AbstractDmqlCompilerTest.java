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
        mMetadata = new SimpleDmqlMetadataValidator();
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

    static class SimpleDmqlMetadataValidator implements DmqlParserMetadata
    {
        public SimpleDmqlMetadataValidator()
        {
            mFields = new HashSet();
            mLookups = new HashMap();
            mStrings = new HashSet();
        }

        public void addLookup(String name, String[] values)
        {
            Set valueSet = new HashSet();
            for (int i = 0; i < values.length; i++)
            {
                valueSet.add(values[i]);
            }
            mFields.add(name);
            mLookups.put(name, valueSet);
        }

        public void addString(String fieldName)
        {
            mFields.add(fieldName);
            mStrings.add(fieldName);
        }

        public boolean isValidFieldName(String fieldName)
        {
            return mFields.contains(fieldName);
        }

        public boolean isValidStringName(String fieldName)
        {
            return mStrings.contains(fieldName);
        }

        public boolean isValidLookupName(String lookupName)
        {
            return mLookups.containsKey(lookupName);
        }

        public boolean isValidLookupValue(String lookupName, String lookupValue)
        {
            Set values = (Set) mLookups.get(lookupName);
            return values.contains(lookupValue);
        }

        public String fieldToColumn(String fieldName)
        {
            return isValidFieldName(fieldName) ? fieldName : null;
        }

        public String columnToField(String columnName)
        {
            return isValidFieldName(columnName) ? columnName : null;
        }

        public String getLookupDbValue(String lookupName, String lookupValue)
        {
            return isValidLookupValue(lookupName, lookupValue) ?
                lookupValue : null;
        }

        public Collection getAllColumns()
        {
            return mFields;
        }

        private Set mFields;
        private Map mLookups;
        private Set mStrings;
    }

    protected SimpleDmqlMetadataValidator mMetadata;
}
