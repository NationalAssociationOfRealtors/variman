/*
 */
package org.realtors.rets.server.webapp.auth;

import java.security.NoSuchAlgorithmException;

import junit.framework.TestCase;

public class DigestAuthorizationRequestTest extends TestCase
{
    public void testRequestHeader()
    {
        DigestAuthorizationRequest request = new DigestAuthorizationRequest(
            "Digest username=\"Joe\", realm=\"RETS Server\", " +
            "nonce=\"246e1b9f80fd87d67c6eceffbcf89941\", " +
            "uri=\"/rets/login\", qop=\"auth\", algorithm=\"HashUtils\", " +
            "nc=00000001, cnonce=\"f151926f19d2566706621616d29f257c\", " +
            "response=\"4ac947bd0b23ca972a5f1d5d794b13a6\", " +
            "opaque=\"121d932ad13ff598b0df1d700e422812\"");

        assertEquals("Joe", request.getUsername());
        assertEquals("RETS Server", request.getRealm());
        assertEquals("246e1b9f80fd87d67c6eceffbcf89941", request.getNonce());
        assertEquals("/rets/login", request.getUri());
        assertEquals("auth", request.getQop());
        assertEquals("HashUtils", request.getAlgorithm());
        assertEquals("00000001", request.getNonceCount());
        assertEquals("f151926f19d2566706621616d29f257c", request.getCnonce());
        assertEquals("4ac947bd0b23ca972a5f1d5d794b13a6", request.getResponse());
        assertEquals("121d932ad13ff598b0df1d700e422812", request.getOpaque());

        request.setMethod("GET");
        assertTrue(request.verifyResponse("Schmoe"));
    }

    public void testRequestHeaderNoQuotes()
    {
        // Mozilla-style qop with no quotes
        DigestAuthorizationRequest request = new DigestAuthorizationRequest(
            "Digest username=\"Joe\", realm=\"RETS Server\", " +
            "nonce=\"d1b916ff2bf199f51c4670d011f9ac21\", " +
            "uri=\"/rets/login\", " +
            "response=\"d7d4e90e15bdc2a43812f9e5383fac59\", " +
            "opaque=\"c54a061147901a1a1ddc55369a3439d8\", " +
            "qop=auth, nc=00000001, " +
            "cnonce=\"cbbaa5564d53563e\"");

        assertEquals("Joe", request.getUsername());
        assertEquals("RETS Server", request.getRealm());
        assertEquals("d1b916ff2bf199f51c4670d011f9ac21", request.getNonce());
        assertEquals("/rets/login", request.getUri());
        assertEquals("auth", request.getQop());
        assertEquals("HashUtils", request.getAlgorithm());
        assertEquals("00000001", request.getNonceCount());
        assertEquals("cbbaa5564d53563e", request.getCnonce());
        assertEquals("d7d4e90e15bdc2a43812f9e5383fac59", request.getResponse());
        assertEquals("c54a061147901a1a1ddc55369a3439d8", request.getOpaque());

        request.setMethod("GET");
        assertTrue(request.verifyResponse("Schmoe"));
    }

    public void testEqualsInKeyValue()
    {
        DigestAuthorizationRequest request = new DigestAuthorizationRequest(
            "Digest username=\"Joe\", realm=\"RETS Server\", " +
            "nonce=\"e023742615efa35fa18df72d750a4317\", uri=\"/rets/login\", " +
            "algorithm=\"HashUtils\", qop=\"auth\", cnonce=\"NzgzMTA=\", " +
            "nc=00000001, response=\"b83db9c43d82e38380e24ecb03109d5d\", " +
            "opaque=\"6055cb5ccdff7d130976dafffcd2e12c\"");
        assertEquals("NzgzMTA=", request.getCnonce());

        request.setMethod("GET");
        assertTrue(request.verifyResponse("Schmoe"));
    }

    public void testValidResponse() throws NoSuchAlgorithmException
    {
        DigestAuthorizationRequest request = new DigestAuthorizationRequest();
        request.setUsername("Joe");
        request.setRealm("RETS Server");
        request.setMethod("GET");
        request.setUri("/rets/login");
        request.setQop("auth");
        request.setNonce("246e1b9f80fd87d67c6eceffbcf89941");
        request.setOpaque("121d932ad13ff598b0df1d700e422812");
        request.setCnonce("f151926f19d2566706621616d29f257c");
        request.setNonceCount("00000001");
        request.setResponse("4ac947bd0b23ca972a5f1d5d794b13a6");
        assertTrue(request.verifyResponse("Schmoe"));
        // Test a pre-hashed A1 password
        assertTrue(request.verifyResponse("a04aab7e749c8f532b78bdd61dfa0cb2",
                                          true));
    }

    /**
     * Ensure that a header like Showingtime's with quotes around nc fail.
     */
    public void testShowingtimeHeader()
    {
        DigestAuthorizationRequest request = new DigestAuthorizationRequest(
            "Digest username=\"showingtime2\", realm=\"RETS Server\", " +
            "nonce=\"27bc2a479bd8213202039dbc3ef5922b\", " +
            "opaque=\"27bc2a479bd8213202039dbc3ef5922b\", " +
            "uri=\"/rets/cct/login\", " +
            "response=\"2c69a66c69996c5041f52697e70906b8\", qop=\"auth\", " +
            "cnonce=\"0a4f113b\", nc=\"00000001\"");

        assertEquals("showingtime2", request.getUsername());
        assertEquals("RETS Server", request.getRealm());
        assertEquals("27bc2a479bd8213202039dbc3ef5922b", request.getNonce());
        assertEquals("/rets/cct/login", request.getUri());
        assertEquals("auth", request.getQop());
        assertEquals("HashUtils", request.getAlgorithm());
        assertEquals("\"00000001\"", request.getNonceCount());
        assertEquals("0a4f113b", request.getCnonce());
        assertEquals("2c69a66c69996c5041f52697e70906b8", request.getResponse());
        assertEquals("27bc2a479bd8213202039dbc3ef5922b", request.getOpaque());

        request.setMethod("GET");
        assertFalse(request.verifyResponse("stcom"));

        // Test that removing quotes from nonce count results in a succesful
        // verify.
        request.setNonceCount("00000001");
        assertTrue(request.verifyResponse("stcom"));
    }

    public void testShowingtimeResponse() throws NoSuchAlgorithmException
    {
        DigestAuthorizationRequest request = new DigestAuthorizationRequest();
        request.setUsername("showingtime2");
        request.setRealm("RETS Server");
        request.setMethod("GET");
        request.setUri("/rets/cct/login");
        request.setQop("auth");
        request.setNonce("27bc2a479bd8213202039dbc3ef5922b");
        request.setOpaque("27bc2a479bd8213202039dbc3ef5922b");
        request.setCnonce("0a4f113b");
        request.setNonceCount("00000001");
        request.setResponse("2c69a66c69996c5041f52697e70906b8");
        assertTrue(request.verifyResponse("stcom"));
        // Test a pre-hashed A1 password
        assertTrue(request.verifyResponse("9009f876938ab2dc5e71423bfe6dc0c2",
                                          true));
    }

    public void testNullPassword() throws NoSuchAlgorithmException
    {
        DigestAuthorizationRequest request = new DigestAuthorizationRequest();
        request.setUsername("Joe");
        request.setRealm("RETS Server");
        request.setMethod("GET");
        request.setUri("/rets/login");
        request.setQop("auth");
        request.setNonce("246e1b9f80fd87d67c6eceffbcf89941");
        request.setOpaque("121d932ad13ff598b0df1d700e422812");
        request.setCnonce("f151926f19d2566706621616d29f257c");
        request.setNonceCount("00000001");
        // Response if password is interpreted as 'n', 'u', 'l', 'l'
        request.setResponse("16620f100bad0abee21864791b2ff1e5");
        assertFalse(request.verifyResponse(null));
    }

    public void testNoQop()
    {
        DigestAuthorizationRequest request = new DigestAuthorizationRequest();
        request.setUsername("Joe");
        request.setRealm("RETS Server");
        request.setMethod("GET");
        request.setUri("/rets/login");
        request.setNonce("e19ef455409e9663b384d837453c74fd");
        request.setResponse("ddd25f230a426925b4a467e186718094");
        assertTrue(request.verifyResponse("Schmoe"));
    }

    public void testCurlHeader()
    {
        DigestAuthorizationRequest request = new DigestAuthorizationRequest(
            "Digest username=\"Joe\", realm=\"RETS Server\", " +
            "nonce=\"e19ef455409e9663b384d837453c74fd\", " +
            "uri=\"/rets/login\", " +
            "response=\"ddd25f230a426925b4a467e186718094\"");
        assertEquals("Joe", request.getUsername());
        assertEquals("RETS Server", request.getRealm());
        assertEquals("e19ef455409e9663b384d837453c74fd", request.getNonce());
        assertEquals("/rets/login", request.getUri());
        assertNull(request.getQop());
        assertEquals("HashUtils", request.getAlgorithm());
        assertEquals("", request.getNonceCount());
        assertEquals("", request.getCnonce());
        assertEquals("ddd25f230a426925b4a467e186718094", request.getResponse());
        assertEquals("", request.getOpaque());

        request.setMethod("GET");
        assertTrue(request.verifyResponse("Schmoe"));
    }
}
