/*
 */
package org.realtors.rets.server;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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
        IOUtils.writeString(expected, file);
        assertEquals(expected, IOUtils.readString(file));
    }

    public void testListRecursive() throws IOException
    {
        String file = getClass().getResource("foo.txt").getFile();
        file = StringUtils.replace(file, "foo.txt", "dirTest");
        List files = IOUtils.listFilesRecursive(new File(file));
        Collections.sort(files);
        List expected = new ArrayList();
        expected.add(new File(file, "a"));
        expected.add(new File(file, "a/bar.ex2"));
        expected.add(new File(file, "b"));
        expected.add(new File(file, "b/baz.ex3"));
        expected.add(new File(file, "foo.ex1"));
        assertEquals(expected, files);
    }

    public void testListRecursiveFilenameFilter() throws IOException
    {
        String file = getClass().getResource("foo.txt").getFile();
        file = StringUtils.replace(file, "foo.txt", "dirTest");
        List files = IOUtils.listFilesRecursive(
            new File(file), new IOUtils.ExtensionFilter(".ex2"));
        Collections.sort(files);
        List expected = new ArrayList();
        expected.add(new File(file, "a/bar.ex2"));
        assertEquals(expected, files);
    }

    public void testListRecursiveFileFilter() throws IOException
    {
        String file = getClass().getResource("foo.txt").getFile();
        file = StringUtils.replace(file, "foo.txt", "dirTest");
        List files = IOUtils.listFilesRecursive(
            new File(file), new IOUtils.NotDirectoryFilter());
        Collections.sort(files);
        List expected = new ArrayList();
        expected.add(new File(file, "a/bar.ex2"));
        expected.add(new File(file, "b/baz.ex3"));
        expected.add(new File(file, "foo.ex1"));
        assertEquals(expected, files);
    }
}
