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

    public void testNoPatterns()
    {
        GetObjectPatternContext context = new GetObjectPatternContext(KEY, 1);
        assertEquals("foo bar", format("foo bar", context));
    }

    public void testListingIdPattern()
    {
        GetObjectPatternContext context = new GetObjectPatternContext(KEY, 1);
        assertEquals("foo abcde12345 bar", format("foo %k bar", context));
    }

    public void testObjectIdPattern()
    {
        GetObjectPatternContext context = new GetObjectPatternContext(KEY, 1);
        assertEquals("foo 1 bar", format("foo %i bar", context));
    }

    public void testKeyWidth()
    {
        GetObjectPatternContext context = new GetObjectPatternContext(KEY, 1);
        assertEquals("foo abc bar", format("foo %3k bar", context));
        assertEquals("foo 345 bar", format("foo %-3k bar", context));
    }

    public void testEmptyObjectIdPattern()
    {
        GetObjectPatternContext context = new GetObjectPatternContext(KEY, 1);
        assertEquals("foo  bar", format("foo %I bar", context));
        context = new GetObjectPatternContext(KEY, 2);
        assertEquals("foo _1 bar", format("foo %I bar", context));
    }

    public void testMultiplePatterns()
    {
        GetObjectPatternContext context = new GetObjectPatternContext(KEY, 1);
        assertEquals("pictures/abcde12345-1.jpg",
                     format("pictures/%k-%i.jpg", context));
    }

    private static final String KEY = "abcde12345";
}
