/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;

import junit.framework.TestCase;

public abstract class LocalTestCase extends TestCase
{
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
            throw new AssertionError(e);
        }
    }
}
