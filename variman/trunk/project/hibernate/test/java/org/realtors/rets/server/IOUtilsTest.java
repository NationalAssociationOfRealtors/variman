/*
 */
package org.realtors.rets.server;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;

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

    public void testWriteString() throws IOException
    {
        String file = getClass().getResource("foo.txt").getFile();
        file = StringUtils.replace(file, "foo.txt", "bar.txt");
        String expected = "a\nb\nc\n" + System.currentTimeMillis() + "\n";
        IOUtils.writeString(file, expected);
        assertEquals(expected, IOUtils.readString(file));
    }
}
