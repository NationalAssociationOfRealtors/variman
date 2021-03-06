/*
 */
package org.realtors.rets.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

import junit.framework.TestCase;

public class IOUtilsTest extends TestCase
{
    public void testFillByteBuffer() throws IOException
    {
        URL resource = getClass().getResource("large-file.txt");
        File file = new File(resource.getFile());
        int fileSize = (int) file.length();
        // Make sure file is larger than the number of bytes read per read()
        assertTrue(fileSize > SputteringInputStream.SPUTTERING_SIZE);

        // Using this special input stream filter, we ensure that the read()
        // loop is tested.
        SputteringInputStream stream =
            new SputteringInputStream(new FileInputStream(file));
        // Make buffer 1 byte larger than file to test premature EOF
        byte[] buffer = new byte[fileSize + 1];
        int bytesRead = IOUtils.fillByteBuffer(stream, buffer);
        assertEquals(fileSize, bytesRead);

        // There should be more than 2 calls to read(), to make sure the read()
        // loop is working.  Why two?  The best case requires 2 reads, one to
        // read all the data, the second to return -1.
        assertTrue(stream.getReadCount() > 2);

        // Check contents
        byte[] expected = new byte[fileSize + 1];
        for (int i = 0; i < fileSize; i++)
        {
            expected[i] = (byte) ((i % 10) + '0');
        }
        assertTrue(Arrays.equals(buffer, expected));
    }

    /**
     * A stream that only reads 10 bytes at a time.
     */
    private static class SputteringInputStream extends FilterInputStream
    {
        public SputteringInputStream(InputStream stream)
        {
            super(stream);
            mReadCount = 0;
        }

        public int read(byte[] bytes, int off, int len) throws IOException
        {
            mReadCount++;
            if (len > SPUTTERING_SIZE)
            {
                len = SPUTTERING_SIZE;
            }
            return super.read(bytes, off, len);
        }

        public int getReadCount()
        {
            return mReadCount;
        }

        public static final int SPUTTERING_SIZE = 10;
        private int mReadCount;
    }

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
        expected.add(new File(file, "a/bar.html"));
        expected.add(new File(file, "b"));
        expected.add(new File(file, "b/baz.txt"));
        expected.add(new File(file, "foo.xml"));
        assertEquals(expected, files);
    }

    public void testListRecursiveFilenameFilter() throws IOException
    {
        String file = getClass().getResource("foo.txt").getFile();
        file = StringUtils.replace(file, "foo.txt", "dirTest");
        List files = IOUtils.listFilesRecursive(
            new File(file), new IOUtils.ExtensionFilter(".html"));
        Collections.sort(files);
        List expected = new ArrayList();
        expected.add(new File(file, "a/bar.html"));
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
        expected.add(new File(file, "a/bar.html"));
        expected.add(new File(file, "b/baz.txt"));
        expected.add(new File(file, "foo.xml"));
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
        String base = SystemUtils.JAVA_IO_TMPDIR;
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
        File base = new File(SystemUtils.JAVA_IO_TMPDIR);
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
