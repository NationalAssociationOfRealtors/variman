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

import org.apache.commons.lang.ArrayUtils;

import junit.framework.TestCase;
import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.GlobalTestSetup;

public class GetObjectTransactionTest extends TestCase
{
    public void setUp()
    {
        GlobalTestSetup.globalSetup();
    }

    public void testSingleJpeg() throws IOException, RetsServerException
    {
        GetObjectTransaction transaction = createTransaction("abc123:1");
        TestResponse response = new TestResponse();
        transaction.execute(response);
        assertEquals("image/jpeg", response.getContentType());
        assertEquals("1.0", response.getHeader("MIME-Version"));
        assertEquals("abc123", response.getHeader("Content-ID"));
        assertEquals("1", response.getHeader("Object-ID"));
        assertNull(response.getHeader("Location"));
        assertNull(response.getHeader("Content-Description"));

        byte[] expected =
            IOUtils.readBytes(getClass().getResourceAsStream(JPEG_FILE_1));
        byte[] actual = response.getByteArray();
        assertTrue(Arrays.equals(expected, actual));
    }

    public void testSingleJpegLocation()
        throws IOException, RetsServerException
    {
        GetObjectTransaction transaction = createTransaction("abc123:1", true);
        TestResponse response = new TestResponse();
        transaction.execute(response);
        assertEquals("application/octet-stream", response.getContentType());
        assertEquals("1.0", response.getHeader("MIME-Version"));
        assertEquals("abc123", response.getHeader("Content-ID"));
        assertEquals("1", response.getHeader("Object-ID"));
        assertEquals(createLocationUrl("abc123", "1"),
                     response.getHeader("Location"));
        assertNull(response.getHeader("Content-Description"));

        byte[] expected = ArrayUtils.EMPTY_BYTE_ARRAY;
        byte[] actual = response.getByteArray();
        assertTrue(Arrays.equals(expected, actual));
    }

    public void testBlockedLocation()
        throws IOException, RetsServerException
    {
        GetObjectTransaction transaction = createTransaction("abc123:1", true);
        transaction.setBlockLocation(true);
        TestResponse response = new TestResponse();
        transaction.execute(response);
        assertEquals("image/jpeg", response.getContentType());
        assertEquals("1.0", response.getHeader("MIME-Version"));
        assertEquals("abc123", response.getHeader("Content-ID"));
        assertEquals("1", response.getHeader("Object-ID"));
        assertNull(response.getHeader("Location"));
        assertNull(response.getHeader("Content-Description"));

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
        assertNull(response.getHeader("Location"));
        assertNull(response.getHeader("Content-Description"));

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
            transaction.setPhotoPattern("");
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
        GetObjectTransaction transaction = createTransaction("abc123:1");
        transaction.setPhotoPattern("%k-%i.gif");
        TestResponse response = new TestResponse();
        transaction.execute(response);
        assertEquals("image/gif", response.getContentType());
        assertEquals("1.0", response.getHeader("MIME-Version"));
        assertEquals("abc123", response.getHeader("Content-ID"));
        assertEquals("1", response.getHeader("Object-ID"));
        assertNull(response.getHeader("Location"));
        assertNull(response.getHeader("Content-Description"));

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
        assertNull(response.getHeader("Location"));
        assertNull(response.getHeader("Content-Description"));

        ByteArrayOutputStream expectedStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(expectedStream);
        out.writeBytes(CRLF + "--" + BOUNDARY + CRLF);
        out.writeBytes("Content-ID: abc123" + CRLF);
        out.writeBytes("Object-ID: 1" + CRLF);
        out.writeBytes("Content-Type: image/jpeg" + CRLF);
        out.writeBytes(CRLF);
        InputStream stream = getClass().getResourceAsStream(JPEG_FILE_1);
        IOUtils.copyStream(stream, out);

        out.writeBytes(CRLF + "--" + BOUNDARY + CRLF);
        out.writeBytes("Content-ID: abc123" + CRLF);
        out.writeBytes("Object-ID: 2" + CRLF);
        out.writeBytes("Content-Type: image/jpeg" + CRLF);
        out.writeBytes(CRLF);
        stream = getClass().getResourceAsStream(JPEG_FILE_2);
        IOUtils.copyStream(stream, out);

        out.writeBytes(CRLF + "--" + BOUNDARY + CRLF);
        out.writeBytes("Content-ID: abc124" + CRLF);
        out.writeBytes("Object-ID: 1" + CRLF);
        out.writeBytes("Content-Type: image/jpeg" + CRLF);
        out.writeBytes(CRLF);
        stream = getClass().getResourceAsStream(JPEG_FILE_3);
        IOUtils.copyStream(stream, out);
        
        out.writeBytes(CRLF + "--" + BOUNDARY + CRLF);
        out.writeBytes("Content-ID: abc125" + CRLF);
        out.writeBytes("Object-ID: 1" + CRLF);
        out.writeBytes("Content-Type: text/xml" + CRLF);
        out.writeBytes(CRLF);
        out.writeBytes("<RETS ReplyCode=\"20403\" ReplyText=\"No Object Found\"/>" + CRLF);
        
        out.writeBytes(CRLF + "--" + BOUNDARY + "--" + CRLF);
        out.flush();

        byte[] expected = expectedStream.toByteArray();
        byte[] actual = response.getByteArray();
        assertTrue(Arrays.equals(expected, actual));
    }

    public void testMultipartLocation() throws RetsServerException, IOException
    {
        GetObjectTransaction transaction =
            createTransaction("abc123:*,abc124:*,abc125:*", true);
        transaction.setBoundaryGenerator(new TestBoundaryGenerator());
        TestResponse response = new TestResponse();
        transaction.execute(response);
        assertEquals("multipart/parallel; boundary=\"" + BOUNDARY + "\"",
                     response.getContentType());
        assertEquals("1.0", response.getHeader("MIME-Version"));
        assertNull(response.getHeader("Content-ID"));
        assertNull(response.getHeader("Object-ID"));
        assertNull(response.getHeader("Location"));

        ByteArrayOutputStream expectedStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(expectedStream);
        out.writeBytes(CRLF + "--" + BOUNDARY + CRLF);
        out.writeBytes("Content-ID: abc123" + CRLF);
        out.writeBytes("Object-ID: 1" + CRLF);
        out.writeBytes("Content-Type: text/xml" + CRLF);
        out.writeBytes("Location: " + createLocationUrl("abc123", "1") + CRLF);
        out.writeBytes(CRLF);
        out.writeBytes("<RETS ReplyCode=\"0\" ReplyText=\"Operation Successful\"/>" +
            				CRLF);

        out.writeBytes(CRLF + "--" + BOUNDARY + CRLF);
        out.writeBytes("Content-ID: abc123" + CRLF);
        out.writeBytes("Object-ID: 2" + CRLF);
        out.writeBytes("Content-Type: text/xml" + CRLF);
        out.writeBytes("Location: " + createLocationUrl("abc123", "2") + CRLF);
        out.writeBytes(CRLF);
        out.writeBytes("<RETS ReplyCode=\"0\" ReplyText=\"Operation Successful\"/>" +
							CRLF);

        out.writeBytes(CRLF + "--" + BOUNDARY + CRLF);
        out.writeBytes("Content-ID: abc124" + CRLF);
        out.writeBytes("Object-ID: 1" + CRLF);
        out.writeBytes("Content-Type: text/xml" + CRLF);
        out.writeBytes("Location: " + createLocationUrl("abc124", "1") + CRLF);
        out.writeBytes(CRLF);
        out.writeBytes("<RETS ReplyCode=\"0\" ReplyText=\"Operation Successful\"/>" +
							CRLF);

        out.writeBytes(CRLF + "--" + BOUNDARY + CRLF);
        out.writeBytes("Content-ID: abc125" + CRLF);
        out.writeBytes("Object-ID: 1" + CRLF);
        out.writeBytes("Content-Type: text/xml" + CRLF);
        out.writeBytes("Location: " + CRLF);
        out.writeBytes(CRLF);
        out.writeBytes("<RETS ReplyCode=\"20403\" ReplyText=\"No Object Found\"/>" +
							CRLF);

        out.writeBytes(CRLF + "--" + BOUNDARY + "--" + CRLF);
        out.flush();

        byte[] expected = expectedStream.toByteArray();
        byte[] actual = response.getByteArray();
        assertTrue(Arrays.equals(expected, actual));
    }

    public void testMultipartForSingleImage()
        throws RetsServerException, IOException
    {
        GetObjectTransaction transaction = createTransaction("abc124:*");
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
        out.writeBytes("Content-ID: abc124" + CRLF);
        out.writeBytes("Object-ID: 1" + CRLF);
        out.writeBytes("Content-Type: image/jpeg" + CRLF);
        out.writeBytes(CRLF);
        InputStream stream = getClass().getResourceAsStream(JPEG_FILE_3);
        IOUtils.copyStream(stream, out);

        out.writeBytes(CRLF + "--" + BOUNDARY + "--" + CRLF);
        out.flush();

        byte[] expected = expectedStream.toByteArray();
        byte[] actual = response.getByteArray();
        assertTrue(Arrays.equals(expected, actual));
    }

    public void testObjectSetImage() throws RetsServerException, IOException
    {
        GetObjectTransaction transaction = createTransaction("abc126:1");
        TestResponse response = new TestResponse();
        transaction.execute(response);
        assertEquals("image/jpeg", response.getContentType());
        assertEquals("1.0", response.getHeader("MIME-Version"));
        assertEquals("abc126", response.getHeader("Content-ID"));
        assertEquals("1", response.getHeader("Object-ID"));
        assertEquals("Beautiful frontal view of home.",
                     response.getHeader("Content-Description"));
        assertNull(response.getHeader("Location"));

        byte[] expected =
            IOUtils.readBytes(getClass().getResourceAsStream(JPEG_FILE_1));
        byte[] actual = response.getByteArray();
        assertTrue(Arrays.equals(expected, actual));
    }


    public void testObjectSetDefaultImage()
        throws RetsServerException, IOException
    {
        GetObjectTransaction transaction = createTransaction("abc126:0");
        TestResponse response = new TestResponse();
        transaction.execute(response);
        assertEquals("image/jpeg", response.getContentType());
        assertEquals("1.0", response.getHeader("MIME-Version"));
        assertEquals("abc126", response.getHeader("Content-ID"));
        assertEquals("2", response.getHeader("Object-ID"));
        assertNull(response.getHeader("Location"));
        assertNull(response.getHeader("Content-Description"));

        byte[] expected =
            IOUtils.readBytes(getClass().getResourceAsStream(JPEG_FILE_2));
        byte[] actual = response.getByteArray();
        assertTrue(Arrays.equals(expected, actual));
    }

    public void testMultipartForSingleImageObjectSet()
        throws RetsServerException, IOException
    {
        GetObjectTransaction transaction =
            createTransaction("abc125:*,abc126:1");
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
        out.writeBytes("Content-ID: abc125" + CRLF);
        out.writeBytes("Object-ID: 1" + CRLF);
        out.writeBytes("Content-Type: text/xml" + CRLF);
        out.writeBytes(CRLF);
        out.writeBytes("<RETS ReplyCode=\"20403\" ReplyText=\"No Object Found\"/>" + CRLF);
        
        out.writeBytes(CRLF + "--" + BOUNDARY + CRLF);
        out.writeBytes("Content-ID: abc126" + CRLF);
        out.writeBytes("Content-Description: Beautiful frontal view of home." +
                       CRLF);
        out.writeBytes("Object-ID: 1" + CRLF);
        out.writeBytes("Content-Type: image/jpeg" + CRLF);
        out.writeBytes(CRLF);
        InputStream stream = getClass().getResourceAsStream(JPEG_FILE_1);
        IOUtils.copyStream(stream, out);

        out.writeBytes(CRLF + "--" + BOUNDARY + "--" + CRLF);
        out.flush();

        byte[] expected = expectedStream.toByteArray();
        byte[] actual = response.getByteArray();
        assertTrue(Arrays.equals(expected, actual));
    }

    public void testRemoteLocationObjectSet() throws RetsServerException
    {
        GetObjectTransaction transaction = createTransaction("abc126:3", true);
        TestResponse response = new TestResponse();
        transaction.execute(response);
        assertEquals("application/octet-stream", response.getContentType());
        assertEquals("1.0", response.getHeader("MIME-Version"));
        assertEquals("abc126", response.getHeader("Content-ID"));
        assertEquals("3", response.getHeader("Object-ID"));
        assertEquals("http://images.example.com/abc123-1.gif",
                     response.getHeader("Location"));
        assertNull(response.getHeader("Content-Description"));

        byte[] expected = ArrayUtils.EMPTY_BYTE_ARRAY;
        byte[] actual = response.getByteArray();
        assertTrue(Arrays.equals(expected, actual));
    }

    public void testFindObjectDescriptor() throws RetsServerException
    {
        GetObjectTransaction transaction = createTransaction();

        ObjectDescriptor expected = new ObjectDescriptor(
            "abc126", 1, localUrl(JPEG_FILE_1),
            "Beautiful frontal view of home.");
        expected.setRemoteLocationAllowable(true);

        ObjectDescriptor actual =
            transaction.findObjectDescriptor("abc126", 1);
        assertEquals(expected, actual);
    }

    public void testNullOrBlankObjectSetPattern()
        throws RetsServerException, IOException
    {
        GetObjectTransaction transaction = createTransaction();
        // This should skip the XML file, and fall back to abc126-1.jpg
        transaction.setObjectSetPattern(null);

        ObjectDescriptor expected = new ObjectDescriptor(
            "abc126", 1, localUrl(JPEG_FILE_4));

        assertEquals(expected, transaction.findObjectDescriptor("abc126", 1));

        // Now try it with a blank pattern
        transaction.setObjectSetPattern("");
        assertEquals(expected, transaction.findObjectDescriptor("abc126", 1));
    }

    public void testNullPatterns() throws RetsServerException
    {
        GetObjectTransaction transaction = createTransaction();
        transaction.setObjectSetPattern(null);
        transaction.setPhotoPattern(null);
        assertNull(transaction.findObjectDescriptor("abc126", 1));
        assertNull(transaction.findObjectDescriptor("abc123", 1));
    }

    public void testNullOrBlankRootDirectory() throws RetsServerException
    {
        GetObjectTransaction transaction = createTransaction();
        transaction.setRootDirectory(null);
        assertNull(transaction.findObjectDescriptor("abc123", 1));
        assertNull(transaction.findObjectDescriptor("abc126", 1));

        // Now try it with a blank pattern
        transaction.setObjectSetPattern("");
        assertNull(transaction.findObjectDescriptor("abc123", 1));
        assertNull(transaction.findObjectDescriptor("abc126", 1));
    }

    private GetObjectTransaction createTransaction()
    {
        GetObjectTransaction transaction =
            new GetObjectTransaction("Property", "Photo");
        setupTransaction(transaction);
        return transaction;
    }

    private GetObjectTransaction createTransaction(String id)
    {
        return createTransaction(id, false);
    }

    private GetObjectTransaction createTransaction(String id,
                                                   boolean useLocation)
    {
        GetObjectParameters parameters =
            new GetObjectParameters("Property", "Photo", id, useLocation);
        parameters.setUseLocation(useLocation);
        GetObjectTransaction transaction = new GetObjectTransaction(parameters);
        setupTransaction(transaction);
        return transaction;
    }

    private void setupTransaction(GetObjectTransaction transaction)
    {
        // The file name will get stripped off, so all that's import is that it
        // exists.
        String imageDirectory = directoryOfResource(GIF_FILE);
        transaction.setRootDirectory(imageDirectory);
        transaction.setPhotoPattern("%k-%i.jpg");
        transaction.setObjectSetPattern("%k.xml");
        transaction.setBaseLocationUrl(BASE_LOCATION_URL);
    }

    private String createLocationUrl(String key, String id)
    {
        StringBuffer location = new StringBuffer();
        location.append(BASE_LOCATION_URL).append("Property/Photo/");
        location.append(key).append("/");
        location.append(id);
        return location.toString();
    }

    private String directoryOfResource(String resourceName)
    {
        URL imageUrl = getClass().getResource(resourceName);
        return IOUtils.urlToFile(imageUrl).getParent();
    }

    private URL localUrl(String resourceName)
    {
        return getClass().getResource(resourceName);
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
    public static final String JPEG_FILE_4 = "abc126-1.jpg";
    public static final String CRLF = "\r\n";
    private static final String BOUNDARY = "simple boundary";
    private static final String BASE_LOCATION_URL =
        "http://www.example.com/rets/images/";
}
