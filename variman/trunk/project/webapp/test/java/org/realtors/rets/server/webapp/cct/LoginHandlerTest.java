/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;

import javax.servlet.ServletException;

import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.InvocationContext;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import org.xml.sax.SAXException;

public class LoginHandlerTest
    extends LocalTestCase
{
    protected void setUp() throws IOException, ServletException
    {
        ServletRunner servletRunner = new ServletRunner();
        servletRunner.registerServlet("/rets/cct/login",
                                      TestHandlerServlet.class.getName());
        mClient = servletRunner.newClient();
        InvocationContext invocation = mClient.newInvocation(LOGIN_URL);
        TestHandlerServlet servlet =
            (TestHandlerServlet) invocation.getServlet();
        mLogin = new LoginHandler();
        mLogin.setSessionId("login");
        servlet.setHandler(mLogin);
    }

    public void testLoginServlet() throws IOException, SAXException
    {
        WebResponse response = mClient.getResponse(LOGIN_URL);
        assertEquals("text/xml", response.getContentType());
        assertEquals("login", response.getNewCookieValue("RETS-Session-ID"));
        String expected =
            "<RETS ReplyCode=\"0\" ReplyText=\"Operation Successful\">" + EOL +
            "<RETS-RESPONSE>" + EOL +
            "MemberName = Joe Schmoe" + EOL +
            "User = A123,5678,1,A123" + EOL +
            "Broker = B123" + EOL +
            "MetadataVersion = 1.00.000" + EOL +
            "MinMetadataVersion = 1.00.000" + EOL +
            "Action = http://localhost:0/rets/action" + EOL +
            "Login = http://localhost:0/rets/login" + EOL +
            "Logout = http://localhost:0/rets/logout" + EOL +
            "Search = http://localhost:0/rets/search" + EOL +
            "GetMetadata = http://localhost:0/rets/getMetadata" + EOL +
            "</RETS-RESPONSE>" + EOL +
            "</RETS>" + EOL;
        assertLinesEqual(expected, response.getText());
    }

    public void testMinimalCapabilityUrls() throws IOException, SAXException
    {
        mLogin.setCapabilityUrlLevel(CapabilityUrlLevel.MINIMAL);

        WebResponse response = mClient.getResponse(LOGIN_URL);
        String expected =
            "<RETS ReplyCode=\"0\" ReplyText=\"Operation Successful\">" + EOL +
            "<RETS-RESPONSE>" + EOL +
            "MemberName = Joe Schmoe" + EOL +
            "User = A123,5678,1,A123" + EOL +
            "Broker = B123" + EOL +
            "MetadataVersion = 1.00.000" + EOL +
            "MinMetadataVersion = 1.00.000" + EOL +
            "Login = http://localhost:0/rets/login" + EOL +
            "Search = http://localhost:0/rets/search" + EOL +
            "GetMetadata = http://localhost:0/rets/getMetadata" + EOL +
            "</RETS-RESPONSE>" + EOL +
            "</RETS>" + EOL;
        assertLinesEqual(expected, response.getText());
    }

    public void testMaxmimalCapabilityUrls() throws IOException, SAXException
    {
        mLogin.setCapabilityUrlLevel(CapabilityUrlLevel.MAXIMMAL);

        WebResponse response = mClient.getResponse(LOGIN_URL);
        String expected =
            "<RETS ReplyCode=\"0\" ReplyText=\"Operation Successful\">" + EOL +
            "<RETS-RESPONSE>" + EOL +
            "MemberName = Joe Schmoe" + EOL +
            "User = A123,5678,1,A123" + EOL +
            "Broker = B123" + EOL +
            "MetadataVersion = 1.00.000" + EOL +
            "MinMetadataVersion = 1.00.000" + EOL +
            "Action = http://localhost:0/rets/action" + EOL +
            "ChangePassword = http://localhost:0/rets/changePassword" + EOL +
            "GetObject = http://localhost:0/rets/getObject" + EOL +
            "Login = http://localhost:0/rets/login" + EOL +
            "LoginComplete = http://localhost:0/rets/loginComplete" + EOL +
            "Logout = http://localhost:0/rets/logout" + EOL +
            "Search = http://localhost:0/rets/search" + EOL +
            "GetMetadata = http://localhost:0/rets/getMetadata" + EOL +
            "Update = http://localhost:0/rets/update" + EOL +
            "</RETS-RESPONSE>" + EOL +
            "</RETS>" + EOL;
        assertLinesEqual(expected, response.getText());
    }

    public void testRelativeUrls() throws IOException, SAXException
    {
        mLogin.setRelativeUrls(true);

        WebResponse response = mClient.getResponse(LOGIN_URL);
        String expected =
            "<RETS ReplyCode=\"0\" ReplyText=\"Operation Successful\">" + EOL +
            "<RETS-RESPONSE>" + EOL +
            "MemberName = Joe Schmoe" + EOL +
            "User = A123,5678,1,A123" + EOL +
            "Broker = B123" + EOL +
            "MetadataVersion = 1.00.000" + EOL +
            "MinMetadataVersion = 1.00.000" + EOL +
            "Action = /action" + EOL +
            // Login must always be absolute
            "Login = http://localhost:0/rets/login" + EOL +
            "Logout = /logout" + EOL +
            "Search = /search" + EOL +
            "GetMetadata = /getMetadata" + EOL +
            "</RETS-RESPONSE>" + EOL +
            "</RETS>" + EOL;
        assertLinesEqual(expected, response.getText());
    }

    public void testAlternateActionUrl() throws IOException, SAXException
    {
        mLogin.setAlternateActionUrl(true);

        WebResponse response = mClient.getResponse(LOGIN_URL);
        String expected =
            "<RETS ReplyCode=\"0\" ReplyText=\"Operation Successful\">" + EOL +
            "<RETS-RESPONSE>" + EOL +
            "MemberName = Joe Schmoe" + EOL +
            "User = A123,5678,1,A123" + EOL +
            "Broker = B123" + EOL +
            "MetadataVersion = 1.00.000" + EOL +
            "MinMetadataVersion = 1.00.000" + EOL +
            "Action = http://localhost:0/rets/actionAlt" + EOL +
            "Login = http://localhost:0/rets/login" + EOL +
            "Logout = http://localhost:0/rets/logout" + EOL +
            "Search = http://localhost:0/rets/search" + EOL +
            "GetMetadata = http://localhost:0/rets/getMetadata" + EOL +
            "</RETS-RESPONSE>" + EOL +
            "</RETS>" + EOL;
        assertLinesEqual(expected, response.getText());
    }

    public void testAlternateLoginUrl() throws IOException, SAXException
    {
        mLogin.setAlternateLoginUrl(true);

        WebResponse response = mClient.getResponse(LOGIN_URL);
        String expected =
            "<RETS ReplyCode=\"0\" ReplyText=\"Operation Successful\">" + EOL +
            "<RETS-RESPONSE>" + EOL +
            "MemberName = Joe Schmoe" + EOL +
            "User = A123,5678,1,A123" + EOL +
            "Broker = B123" + EOL +
            "MetadataVersion = 1.00.000" + EOL +
            "MinMetadataVersion = 1.00.000" + EOL +
            "Action = http://localhost:0/rets/action" + EOL +
            "Login = http://localhost:0/rets/loginAlt" + EOL +
            "Logout = http://localhost:0/rets/logout" + EOL +
            "Search = http://localhost:0/rets/search" + EOL +
            "GetMetadata = http://localhost:0/rets/getMetadata" + EOL +
            "</RETS-RESPONSE>" + EOL +
            "</RETS>" + EOL;
        assertLinesEqual(expected, response.getText());
    }

    public static final String EOL = "\r\n";
    public static final String LOGIN_URL = "http://localhost/rets/cct/login";
    private ServletUnitClient mClient;
    private LoginHandler mLogin;
}
