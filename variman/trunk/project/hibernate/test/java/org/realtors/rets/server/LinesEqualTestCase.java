/*
 */
package org.realtors.rets.server;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;

import junit.framework.TestCase;

import org.apache.commons.lang.SystemUtils;

public abstract class LinesEqualTestCase extends TestCase
{
    public static String NL = SystemUtils.LINE_SEPARATOR;

    /**
     * Asserts that two line-based strings are equal. The error message in the
     * assertion contains the line number for better error messages.
     *
     * @param expected Expected string
     * @param actual Actual string
     */
    public static void assertLinesEqual(String expected, String actual)
    {
        try
        {
            LineNumberReader expectedReader =
                new LineNumberReader(new StringReader(expected));
            LineNumberReader actualReader =
                new LineNumberReader(new StringReader(actual));
            int lineNumber = 1;
            while (true)
            {
                String expectedLine = expectedReader.readLine();
                String actualLine = actualReader.readLine();
                assertEquals("Line " + lineNumber, expectedLine, actualLine);
                if ((expectedLine == null) || (actualLine == null))
                {
                    break;
                }
                lineNumber++;
            }
            // Check the whole string so that the line separator gets checked
            assertEquals("Line separator", expected, actual);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }
}
