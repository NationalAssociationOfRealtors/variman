/*
 */
package org.realtors.rets.server.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.RetsReplyException;
import org.realtors.rets.server.RetsServerException;

import junit.framework.TestCase;

public class GetObjectTransactionTest extends TestCase
{
    public void testSingleGif() throws IOException, RetsServerException
    {
        GetObjectTransaction transaction = createTransaction();
        transaction.setPattern("%k-%i.gif");
        TestResponse response = new TestResponse();
        transaction.execute(response);
        assertEquals("image/gif", response.getContentType());
        assertEquals("1.0", response.getHeader("MIME-Version"));

        byte[] expected =
            IOUtils.readBytes(getClass().getResourceAsStream(GIF_FILE));
        byte[] actual = response.getByteArray();
        assertTrue(Arrays.equals(expected, actual));
    }

    public void testSingleJpeg() throws IOException, RetsServerException
    {
        GetObjectTransaction transaction = createTransaction();
        transaction.setPattern("%k-%i.jpg");
        TestResponse response = new TestResponse();
        transaction.execute(response);
        assertEquals("image/jpeg", response.getContentType());
        assertEquals("1.0", response.getHeader("MIME-Version"));

        byte[] expected =
            IOUtils.readBytes(getClass().getResourceAsStream(JPEG_FILE));
        byte[] actual = response.getByteArray();
        assertTrue(Arrays.equals(expected, actual));
    }

    public void testNoObjectFound() throws RetsServerException
    {
        try
        {
            GetObjectTransaction transaction = createTransaction();
            transaction.setPattern("%k-%i.foo");
            transaction.execute(new TestResponse());
            fail("Should throw an exception");
        }
        catch (RetsReplyException e)
        {
            assertEquals(20403, e.getReplyCode());
            assertEquals("No Object Found", e.getMeaning());
        }
    }

    private GetObjectTransaction createTransaction()
    {
        GetObjectParameters parameters =
            new GetObjectParameters("Property", "Photo", "abc123:1");
        GetObjectTransaction transaction = new GetObjectTransaction(parameters);
        // The file name will get stripped off, so all that's import is that it
        // exists.
        String imageDirectory = directoryOfResource(GIF_FILE);
        transaction.setRootDirectory(imageDirectory);
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

    public static final String GIF_FILE = "abc123-1.gif";
    public static final String JPEG_FILE = "abc123-1.jpg";
}
