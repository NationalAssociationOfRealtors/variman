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
import org.apache.commons.lang.SystemUtils;

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

    public void testRelativizeFiles()
    {
        File base = new File("/root/directory/");
        File subDir = new File("/root/directory/sub/directory");
        File other = new File("/some/other/directory");

        File relative = IOUtils.relativize(base, subDir);
        File expected = new File("sub/directory");
        assertEquals(expected, relative);

        relative = IOUtils.relativize(base, other);
        expected = new File("/some/other/directory");
        assertEquals(expected, relative);
    }

    public void testRelativizeStrings()
    {
        String base = "/root/directory/";
        String subDir = "/root/directory/sub/directory";
        String other = "/some/other/directory";

        String relative = IOUtils.relativize(base, subDir);
        String expected = "sub/directory";
        assertEquals(expected, relative);

        relative = IOUtils.relativize(base, other);
        expected = "/some/other/directory";
        assertEquals(expected, relative);
    }

    public void testResolveStrings()
    {
        String base = SystemUtils.USER_DIR;
        String subDir = new File("sub/directory").getPath();
        String other = new File("/some/other/directory").getPath();

        String resolved = IOUtils.resolve(base, subDir);
        String expected = new File(base, "sub/directory").getPath();
        assertEquals(expected, resolved);

        resolved = IOUtils.resolve(base, other);
        expected = other;
        assertEquals(expected, resolved);
    }

    public void testResolveFiles()
    {
        File base = new File(SystemUtils.USER_DIR);
        File subDir = new File("sub/directory");
        File other = new File("/some/other/directory");

        File resolved = IOUtils.resolve(base, subDir);
        File expected = new File(base, "sub/directory");
        assertEquals(expected, resolved);

        resolved = IOUtils.resolve(base, other);
        expected = other;
        assertEquals(expected, resolved);
    }
}
