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
            "Action = " + ABS_ACTION + EOL +
            "Login = " + ABS_LOGIN + EOL +
            "Logout = " + ABS_LOGOUT + EOL +
            "Search = " + ABS_SEARCH + EOL +
            "GetMetadata = "+ ABS_METADATA + EOL +
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
            "Login = " + ABS_LOGIN + EOL +
            "Search = " + ABS_SEARCH + EOL +
            "GetMetadata = " + ABS_METADATA + EOL +
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
            "Action = " + ABS_ACTION + EOL +
            "ChangePassword = " + ABS_PASSWORD + EOL +
            "GetObject = " + ABS_GET_OBJECT + EOL +
            "Login = " + ABS_LOGIN + EOL +
            "LoginComplete = " + ABS_LOGIN_COMPLETE + EOL +
            "Logout = " + ABS_LOGOUT + EOL +
            "Search = " + ABS_SEARCH + EOL +
            "GetMetadata = " + ABS_METADATA  + EOL +
            "Update = " + ABS_UPDATE + EOL +
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
            "Action = /rets/cct/action" + EOL +
            // Login must always be absolute?
            "Login = http://localhost:0/rets/cct/login" + EOL +
            "Logout = /rets/cct/logout" + EOL +
            "Search = /rets/cct/search" + EOL +
            "GetMetadata = /rets/cct/getMetadata" + EOL +
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
            "Action = " + ABS_ACTION_ALT + EOL +
            "Login = " + ABS_LOGIN + EOL +
            "Logout = " + ABS_LOGOUT + EOL +
            "Search = " + ABS_SEARCH + EOL +
            "GetMetadata = " + ABS_METADATA + EOL +
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
            "Action = " + ABS_ACTION + EOL +
            "Login = " + ABS_LOGIN_ALT + EOL +
            "Logout = " + ABS_LOGOUT + EOL +
            "Search = " + ABS_SEARCH + EOL +
            "GetMetadata = " + ABS_METADATA + EOL +
            "</RETS-RESPONSE>" + EOL +
            "</RETS>" + EOL;
        assertLinesEqual(expected, response.getText());
    }

    public static final String EOL = "\r\n";
    public static final String LOGIN_URL = "http://localhost/rets/cct/login";
    public static final String ABS = "http://localhost:0/rets/";
    public static final String ABS_ACTION = ABS + "cct/action";
    public static final String ABS_ACTION_ALT = ABS + "cct/actionAlt";
    public static final String ABS_PASSWORD = ABS + "changePassword";
    public static final String ABS_GET_OBJECT = ABS + "getObject";
    public static final String ABS_LOGIN = ABS + "cct/login";
    public static final String ABS_LOGIN_ALT = ABS + "cct/loginAlt";
    public static final String ABS_LOGIN_COMPLETE = ABS + "loginComplete";
    public static final String ABS_LOGOUT = ABS + "cct/logout";
    public static final String ABS_SEARCH = ABS + "cct/search";
    public static final String ABS_METADATA = ABS + "cct/getMetadata";
    public static final String ABS_UPDATE = ABS + "update";
    private ServletUnitClient mClient;
    private LoginHandler mLogin;
}
