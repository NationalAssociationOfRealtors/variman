/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;

import javax.servlet.ServletException;

import org.realtors.rets.server.cct.ValidationResults;

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

    public void testValidateInvokeCountIsOne() throws IOException, SAXException
    {
        mTest.reset();
        mTest.setGetInvokeCount(InvokeCount.ONE);
        String expected = InvokeCount.ONE.getName();

        ValidationResults result = mTest.validate();
        assertFalse(result.wasSuccessful());
        assertEquals("test get invoke count was 0, expected " + expected,
                     result.getMessage());

        mClient.getResponse(TEST_URL);
        result = mTest.validate();
        assertTrue(result.wasSuccessful());
        assertEquals("Success", result.getMessage());

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

        mClient.getResponse(TEST_URL);
        ValidationResults results = mTest.validate();
        assertFalse(results.wasSuccessful());
        assertEquals("test HTTP header [Accept] was null, expected ^\\*/\\*$",
                     results.getMessage());

        mClient.setHeaderField("Accept", "*/*");
        mClient.getResponse(TEST_URL);
        results = mTest.validate();
        assertTrue(results.wasSuccessful());
        assertEquals("Success", results.getMessage());
    }

    public void testCaseInsensitiveHeaders() throws IOException, SAXException
    {
        mTest.reset();
        mTest.addRequiredHeader("Accept", "^\\*/\\*$");

        mClient.getResponse(TEST_URL);
        ValidationResults results = mTest.validate();
        assertFalse(results.wasSuccessful());
        assertEquals("test HTTP header [Accept] was null, expected ^\\*/\\*$",
                     results.getMessage());

        mClient.setHeaderField("accept", "*/*");
        mClient.getResponse(TEST_URL);
        results = mTest.validate();
        assertTrue(results.wasSuccessful());
        assertEquals("Success", results.getMessage());
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
