/*
 */
package org.realtors.rets.server.protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.RetsServerException;

public class GetObjectTransactionTest extends TestCase
{
    public void testSingleJpeg() throws IOException, RetsServerException
    {
        GetObjectTransaction transaction = createTransaction();
        TestResponse response = new TestResponse();
        transaction.execute(response);
        assertEquals("image/jpeg", response.getContentType());
        assertEquals("1.0", response.getHeader("MIME-Version"));
        assertEquals("abc123", response.getHeader("Content-ID"));
        assertEquals("1", response.getHeader("Object-ID"));

        byte[] expected =
            IOUtils.readBytes(getClass().getResourceAsStream(JPEG_FILE_1));
        byte[] actual = response.getByteArray();
        assertTrue(Arrays.equals(expected, actual));
    }

    public void testDefaultImage() throws IOException, RetsServerException
    {
        GetObjectTransaction transaction = createTransaction("abc123:0");
        TestResponse response = new TestResponse();
        transaction.execute(response);
        assertEquals("image/jpeg", response.getContentType());
        assertEquals("1.0", response.getHeader("MIME-Version"));
        assertEquals("abc123", response.getHeader("Content-ID"));
        assertEquals("1", response.getHeader("Object-ID"));

        byte[] expected =
            IOUtils.readBytes(getClass().getResourceAsStream(JPEG_FILE_1));
        byte[] actual = response.getByteArray();
        assertTrue(Arrays.equals(expected, actual));
    }

    public void testNoObjectFound() throws RetsServerException
    {
        try
        {
            GetObjectTransaction transaction = createTransaction("xyz:1");
            transaction.execute(new TestResponse());
            fail("Should throw an exception");
        }
        catch (RetsReplyException e)
        {
            assertEquals(20403, e.getReplyCode());
            assertEquals("No Object Found", e.getMeaning());
        }
    }

    public void testEmptyPattern() throws RetsServerException
    {
        try
        {
            GetObjectTransaction transaction = createTransaction("xyz:1");
            transaction.setPattern("");
            transaction.execute(new TestResponse());
            fail("Should throw an exception");
        }
        catch (RetsReplyException e)
        {
            assertEquals(20403, e.getReplyCode());
            assertEquals("No Object Found", e.getMeaning());
        }
    }

    public void testSingleGif() throws IOException, RetsServerException
    {
        GetObjectTransaction transaction = createTransaction();
        transaction.setPattern("%k-%i.gif");
        TestResponse response = new TestResponse();
        transaction.execute(response);
        assertEquals("image/gif", response.getContentType());
        assertEquals("1.0", response.getHeader("MIME-Version"));
        assertEquals("abc123", response.getHeader("Content-ID"));
        assertEquals("1", response.getHeader("Object-ID"));

        byte[] expected =
            IOUtils.readBytes(getClass().getResourceAsStream(GIF_FILE));
        byte[] actual = response.getByteArray();
        assertTrue(Arrays.equals(expected, actual));
    }

    public void testMultipart() throws RetsServerException, IOException
    {
        GetObjectTransaction transaction =
            createTransaction("abc123:*,abc124:*,abc125:*");
        transaction.setBoundaryGenerator(new TestBoundaryGenerator());
        TestResponse response = new TestResponse();
        transaction.execute(response);
        assertEquals("multipart/parallel; boundary=\"" + BOUNDARY + "\"",
                     response.getContentType());
        assertEquals("1.0", response.getHeader("MIME-Version"));
        assertNull(response.getHeader("Content-ID"));
        assertNull(response.getHeader("Object-ID"));

        ByteArrayOutputStream expectedStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(expectedStream);
        out.writeBytes(CRLF + "--" + BOUNDARY + CRLF);
        out.writeBytes("Content-Type: image/jpeg" + CRLF);
        out.writeBytes("Content-ID: abc123" + CRLF);
        out.writeBytes("Object-ID: 1" + CRLF);
        InputStream stream = getClass().getResourceAsStream(JPEG_FILE_1);
        IOUtils.copyStream(stream, out);

        out.writeBytes(CRLF + "--" + BOUNDARY + CRLF);
        out.writeBytes("Content-Type: image/jpeg" + CRLF);
        out.writeBytes("Content-ID: abc123" + CRLF);
        out.writeBytes("Object-ID: 2" + CRLF);
        stream = getClass().getResourceAsStream(JPEG_FILE_2);
        IOUtils.copyStream(stream, out);

        out.writeBytes(CRLF + "--" + BOUNDARY + CRLF);
        out.writeBytes("Content-Type: image/jpeg" + CRLF);
        out.writeBytes("Content-ID: abc124" + CRLF);
        out.writeBytes("Object-ID: 1" + CRLF);
        stream = getClass().getResourceAsStream(JPEG_FILE_3);
        IOUtils.copyStream(stream, out);

        out.writeBytes(CRLF + "--" + BOUNDARY + "--" + CRLF);
        out.flush();

        byte[] expected = expectedStream.toByteArray();
        byte[] actual = response.getByteArray();
        assertTrue(Arrays.equals(expected, actual));
    }

    private GetObjectTransaction createTransaction()
    {
        return createTransaction("abc123:1");
    }

    private GetObjectTransaction createTransaction(String id)
    {
        GetObjectParameters parameters =
            new GetObjectParameters("Property", "Photo", id);
        GetObjectTransaction transaction = new GetObjectTransaction(parameters);
        // The file name will get stripped off, so all that's import is that it
        // exists.
        String imageDirectory = directoryOfResource(GIF_FILE);
        transaction.setRootDirectory(imageDirectory);
        transaction.setPattern("%k-%i.jpg");
        return transaction;

    }

    private String directoryOfResource(String resourceName)
    {
        URL imageUrl = getClass().getResource(resourceName);
        String imagePath = imageUrl.getPath();
        String imageDirectory =
            imagePath.substring(0, imagePath.lastIndexOf(resourceName) - 1);
        return imageDirectory;
    }

    private static class TestResponse implements GetObjectResponse
    {
        public OutputStream getOutputStream()
        {
            return mOutputStream;
        }

        public byte[] getByteArray()
        {
            return mOutputStream.toByteArray();
        }

        public void setContentType(String contentType)
        {
            mContentType = contentType;
        }

        public String getContentType()
        {
            return mContentType;
        }

        public void setHeader(String name, String value)
        {
            mHeaders.put(name, value);
        }

        public String getHeader(String name)
        {
            return (String) mHeaders.get(name);
        }

        private ByteArrayOutputStream mOutputStream =
            new ByteArrayOutputStream();
        private String mContentType;
        private Map mHeaders = new HashMap();
    }

    private static class TestBoundaryGenerator
        implements MultipartBoundaryGenerator
    {
        public String generateBoundary()
        {
            return BOUNDARY;
        }
    }

    public static final String GIF_FILE = "abc123-1.gif";
    public static final String JPEG_FILE_1 = "abc123-1.jpg";
    public static final String JPEG_FILE_2 = "abc123-2.jpg";
    public static final String JPEG_FILE_3 = "abc124-1.jpg";
    public static final String CRLF = "\r\n";
    private static final String BOUNDARY = "simple boundary";
}
