/*
 */
package org.realtors.rets.server.protocol;

import junit.framework.TestCase;

public class GetObjectPatternParserTest extends TestCase
{
    private String format(String pattern, GetObjectPatternContext context)
    {
        GetObjectPatternParser parser =
            new GetObjectPatternParser(pattern);
        GetObjectPatternFormatter formatter = parser.parse();
        StringBuffer buffer = new StringBuffer();
        formatter.format(buffer, context);
        String formatted = buffer.toString();
        return formatted;
    }

    private GetObjectPatternContext createContext()
    {
        GetObjectPatternContext context =
            new GetObjectPatternContext("Property", "Picture", "abcde12345", 1);
        return context;
    }

    public void testNoPatterns()
    {
        GetObjectPatternContext context = createContext();
        assertEquals("foo bar", format("foo bar", context));
    }

    public void testListingIdPattern()
    {
        GetObjectPatternContext context = createContext();
        assertEquals("foo abcde12345 bar", format("foo %k bar", context));
    }

    public void testObjectIdPattern()
    {
        GetObjectPatternContext context = createContext();
        assertEquals("foo 1 bar", format("foo %i bar", context));
    }

    public void testKeyWidth()
    {
        GetObjectPatternContext context = createContext();
        assertEquals("foo abc bar", format("foo %3k bar", context));
        assertEquals("foo 345 bar", format("foo %-3k bar", context));
    }

    public void testEmptyObjectIdPattern()
    {
        GetObjectPatternContext context = createContext();
        assertEquals("foo _1 bar", format("foo %I bar", context));
        context = new GetObjectPatternContext(null, null, context.getKey(), 0);
        assertEquals("foo  bar", format("foo %I bar", context));
    }

    public void testMultiplePatterns()
    {
        GetObjectPatternContext context = createContext();
        assertEquals("pictures/abcde12345-1.jpg",
                     format("pictures/%k-%i.jpg", context));
    }
}
