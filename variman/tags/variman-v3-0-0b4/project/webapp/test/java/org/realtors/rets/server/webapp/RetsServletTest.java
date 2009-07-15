/*
 */
package org.realtors.rets.server.webapp;

import java.io.IOException;

import javax.servlet.ServletException;

import org.realtors.rets.server.RetsServerException;

import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import junit.framework.TestCase;
import org.xml.sax.SAXException;

public class RetsServletTest extends TestCase
{
    protected void setUp() throws IOException, ServletException
    {
        ServletRunner servletRunner = new ServletRunner();
        servletRunner.registerServlet("/test",
                                      TestRetsServlet.class.getName());
        mClient = servletRunner.newClient();
    }

    public void testRetsServerException() throws IOException, SAXException
    {
        WebResponse response = mClient.getResponse(TEST_URL);
        String expected = "<RETS ReplyCode=\"20513\" " +
            "ReplyText=\"Miscellaneous Error\"/>\n";
        assertEquals(expected, response.getText());
    }

    public static class TestRetsServlet extends RetsServlet
    {
        protected void doRets(RetsServletRequest request,
                              RetsServletResponse response)
            throws RetsServerException, IOException
        {
            throw new RetsServerException("Error");
        }
    }

    public static final String TEST_URL = "http://localhost/test";
    private ServletUnitClient mClient;
}
