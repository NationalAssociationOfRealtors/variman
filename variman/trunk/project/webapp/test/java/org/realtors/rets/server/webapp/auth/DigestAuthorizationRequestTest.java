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
            "uri=\"/rets/login\", qop=\"auth\", algorithm=\"MD5\", " +
            "nc=00000001, cnonce=\"f151926f19d2566706621616d29f257c\", " +
            "response=\"4ac947bd0b23ca972a5f1d5d794b13a6\", " +
            "opaque=\"121d932ad13ff598b0df1d700e422812\"");

        assertEquals("Joe", request.getUsername());
        assertEquals("RETS Server", request.getRealm());
        assertEquals("246e1b9f80fd87d67c6eceffbcf89941", request.getNonce());
        assertEquals("/rets/login", request.getUri());
        assertEquals("auth", request.getQop());
        assertEquals("MD5", request.getAlgorithm());
        assertEquals("00000001", request.getNonceCount());
        assertEquals("f151926f19d2566706621616d29f257c", request.getCnonce());
        assertEquals("4ac947bd0b23ca972a5f1d5d794b13a6", request.getResponse());
        assertEquals("121d932ad13ff598b0df1d700e422812", request.getOpaque());

        request.setMethod("GET");
        request.setPassword("Schmoe");
        assertTrue(request.verifyResponse());
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
        assertEquals("MD5", request.getAlgorithm());
        assertEquals("00000001", request.getNonceCount());
        assertEquals("cbbaa5564d53563e", request.getCnonce());
        assertEquals("d7d4e90e15bdc2a43812f9e5383fac59", request.getResponse());
        assertEquals("c54a061147901a1a1ddc55369a3439d8", request.getOpaque());

        request.setMethod("GET");
        request.setPassword("Schmoe");
        assertTrue(request.verifyResponse());
    }

    public void testEqualsInKeyValue()
    {
        DigestAuthorizationRequest request = new DigestAuthorizationRequest(
            "Digest username=\"Joe\", realm=\"RETS Server\", " +
            "nonce=\"e023742615efa35fa18df72d750a4317\", uri=\"/rets/login\", " +
            "algorithm=\"MD5\", qop=\"auth\", cnonce=\"NzgzMTA=\", " +
            "nc=00000001, response=\"b83db9c43d82e38380e24ecb03109d5d\", " +
            "opaque=\"6055cb5ccdff7d130976dafffcd2e12c\"");
        assertEquals("NzgzMTA=", request.getCnonce());

        request.setMethod("GET");
        request.setPassword("Schmoe");
        assertTrue(request.verifyResponse());
    }

    public void testValidResponse() throws NoSuchAlgorithmException
    {
        DigestAuthorizationRequest request = new DigestAuthorizationRequest();
        request.setUsername("Joe");
        request.setPassword("Schmoe");
        request.setRealm("RETS Server");
        request.setMethod("GET");
        request.setUri("/rets/login");
        request.setQop("auth");
        request.setNonce("246e1b9f80fd87d67c6eceffbcf89941");
        request.setOpaque("121d932ad13ff598b0df1d700e422812");
        request.setCnonce("f151926f19d2566706621616d29f257c");
        request.setNonceCount("00000001");
        request.setResponse("4ac947bd0b23ca972a5f1d5d794b13a6");
        assertTrue(request.verifyResponse());
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
        // Response if password is interpreted as "null"
        request.setResponse("16620f100bad0abee21864791b2ff1e5");
        assertFalse(request.verifyResponse());
    }
}
