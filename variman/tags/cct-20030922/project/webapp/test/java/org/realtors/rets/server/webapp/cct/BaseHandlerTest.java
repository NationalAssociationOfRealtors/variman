/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;

import javax.servlet.ServletException;

import org.realtors.rets.server.cct.ValidationResult;

import com.meterware.servletunit.InvocationContext;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import org.xml.sax.SAXException;

public class BaseHandlerTest extends LocalTestCase
{
    protected void setUp() throws IOException, ServletException
    {
        ServletRunner servletRunner = new ServletRunner();
        servletRunner.registerServlet("/test",
                                      TestHandlerServlet.class.getName());
        mClient = servletRunner.newClient();
        InvocationContext invocation = mClient.newInvocation(TEST_URL);
        TestHandlerServlet servlet =
            (TestHandlerServlet) invocation.getServlet();
        mTest = new TestHandler();
        servlet.setHandler(mTest);
    }

    public void testValidateOneInvokeCount() throws IOException, SAXException
    {
        mTest.reset();
        mTest.setGetInvokeCount(InvokeCount.ONE);
        String expected = InvokeCount.ONE.getName();

        ValidationResult result = mTest.validate();
        assertFalse(result.wasSuccessful());
        assertEquals("test get invoke count was 0, expected " + expected,
                     result.getMessage());

        mClient.getResponse(TEST_URL);
        result = mTest.validate();
        assertTrue(result.wasSuccessful());
        assertEquals("", result.getMessage());

        mClient.getResponse(TEST_URL);
        result = mTest.validate();
        assertFalse(result.wasSuccessful());
        assertEquals("test get invoke count was 2, expected " + expected,
                     result.getMessage());
    }

    public void testValidatateZeroInvokeCount()
        throws IOException, SAXException
    {
        mTest.reset();
        mTest.setGetInvokeCount(InvokeCount.ZERO);
        String expected = InvokeCount.ZERO.getName();

        ValidationResult result = mTest.validate();
        assertTrue(result.wasSuccessful());
        assertEquals("", result.getMessage());

        mClient.getResponse(TEST_URL);
        result = mTest.validate();
        assertFalse(result.wasSuccessful());
        assertEquals("test get invoke count was 1, expected " + expected,
                     result.getMessage());

        mClient.getResponse(TEST_URL);
        result = mTest.validate();
        assertFalse(result.wasSuccessful());
        assertEquals("test get invoke count was 2, expected " + expected,
                     result.getMessage());
    }

    public void testValidatateZeroOrOneInvokeCount()
        throws IOException, SAXException
    {
        mTest.reset();
        mTest.setGetInvokeCount(InvokeCount.ZERO_OR_ONE);
        String expected = InvokeCount.ZERO_OR_ONE.getName();

        ValidationResult result = mTest.validate();
        assertTrue(result.wasSuccessful());
        assertEquals("", result.getMessage());

        mClient.getResponse(TEST_URL);
        result = mTest.validate();
        assertTrue(result.wasSuccessful());
        assertEquals("", result.getMessage());

        mClient.getResponse(TEST_URL);
        result = mTest.validate();
        assertFalse(result.wasSuccessful());
        assertEquals("test get invoke count was 2, expected " + expected,
                     result.getMessage());
    }

    public void testRequiredHeader() throws IOException, SAXException
    {
        mTest.reset();
        mTest.addRequiredHeader("Accept", "^\\*/\\*$");
        mTest.setGetInvokeCount(InvokeCount.ANY);

        mClient.getResponse(TEST_URL);
        ValidationResult result = mTest.validate();
        assertFalse(result.wasSuccessful());
        assertEquals("test HTTP header [Accept] was null, expected ^\\*/\\*$",
                     result.getMessage());

        mClient.setHeaderField("Accept", "*/*");
        mClient.getResponse(TEST_URL);
        result = mTest.validate();
        assertTrue(result.wasSuccessful());
        assertEquals("", result.getMessage());
    }

    public void testCaseInsensitiveHeaders() throws IOException, SAXException
    {
        mTest.reset();
        mTest.addRequiredHeader("Accept", "^\\*/\\*$");
        mTest.setGetInvokeCount(InvokeCount.ANY);

        mClient.getResponse(TEST_URL);
        ValidationResult result = mTest.validate();
        assertFalse(result.wasSuccessful());
        assertEquals("test HTTP header [Accept] was null, expected ^\\*/\\*$",
                     result.getMessage());

        mClient.setHeaderField("accept", "*/*");
        mClient.getResponse(TEST_URL);
        result = mTest.validate();
        assertTrue(result.wasSuccessful());
        assertEquals("", result.getMessage());
    }

    public void testCookie() throws SAXException, IOException
    {
        mTest.reset();
        mTest.addCookie("RETS-Session-ID", "^testCookie$");
        mTest.setGetInvokeCount(InvokeCount.ANY);

        mClient.getResponse(TEST_URL);
        ValidationResult result = mTest.validate();
        assertFalse(result.wasSuccessful());
        assertEquals("test HTTP cookie [RETS-Session-ID] was null, expected " +
                     "^testCookie$", result.getMessage());

        mClient.addCookie("RETS-Session-ID", "testCookie");
        mClient.getResponse(TEST_URL);
        result = mTest.validate();
        assertTrue(result.wasSuccessful());
        assertEquals("", result.getMessage());
    }

    public void testCaseInsensitiveCookie() throws SAXException, IOException
    {
        mTest.reset();
        mTest.addCookie("RETS-Session-ID", "^testCookie$");
        mTest.setGetInvokeCount(InvokeCount.ANY);

        mClient.getResponse(TEST_URL);
        ValidationResult result = mTest.validate();
        assertFalse(result.wasSuccessful());
        assertEquals("test HTTP cookie [RETS-Session-ID] was null, expected " +
                     "^testCookie$", result.getMessage());

        mClient.addCookie("rets-session-id", "testCookie");
        mClient.getResponse(TEST_URL);
        result = mTest.validate();
        assertTrue(result.wasSuccessful());
        assertEquals("", result.getMessage());
    }

    public void testHeadersWithZeroInvokeCount()
    {
        mTest.reset();
        mTest.addRequiredHeader("Accept", "^\\*/\\*$");
        mTest.setGetInvokeCount(InvokeCount.ANY);

        ValidationResult result = mTest.validate();
        assertTrue(result.wasSuccessful());
        assertEquals("", result.getMessage());
    }

    public void testCookiesWithZeroInvokeCount()
    {
        mTest.reset();
        mTest.addCookie("RETS-Session-ID", "^testCookie$");
        mTest.setGetInvokeCount(InvokeCount.ANY);

        ValidationResult result = mTest.validate();
        assertTrue(result.wasSuccessful());
        assertEquals("", result.getMessage());
    }

    public static final String TEST_URL = "http://localhost/test";

    static class TestHandler extends BaseServletHandler
    {
        public static final String NAME = "test";

        public String getName()
        {
            return NAME;
        }
    }

    private ServletUnitClient mClient;
    private TestHandler mTest;
}
