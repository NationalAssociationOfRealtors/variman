/*
 */
package org.realtors.rets.server;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import junit.framework.TestCase;

public class IOUtilsTest extends TestCase
{
    public void testReadString() throws IOException
    {
        URL file = getClass().getResource("foo.txt");
        String string = IOUtils.readString(file);
        assertEquals("a\nb\nc\n", string);
    }

    public void testReadLines() throws IOException
    {
        URL file = getClass().getResource("foo.txt");
        List lines = IOUtils.readLines(file);
        List expected = new ArrayList();
        expected.add("a");
        expected.add("b");
        expected.add("c");
        assertEquals(expected, lines);
    }
}
