/*
 */
package org.realtors.rets.server.webapp.auth;

import junit.framework.TestCase;

public class DigestAuthenticateResponseTest extends TestCase
{
    public void testResponseHeader()
    {
        DigestAuthenticateResponse response =
            new DigestAuthenticateResponse("RETS Server");
        response.setNonce("246e1b9f80fd87d67c6eceffbcf89941");
        response.setOpaque("121d932ad13ff598b0df1d700e422812");

        String header = response.getHeader();
        assertEquals("Digest realm=\"RETS Server\", " +
                     "qop=\"auth\", " +
                     "nonce=\"246e1b9f80fd87d67c6eceffbcf89941\", " +
                     "opaque=\"121d932ad13ff598b0df1d700e422812\"", header);
    }

    public void testStale()
    {
        DigestAuthenticateResponse response =
            new DigestAuthenticateResponse("RETS Server");
        response.setNonce("246e1b9f80fd87d67c6eceffbcf89941");
        response.setOpaque("121d932ad13ff598b0df1d700e422812");
        response.setStale(true);

        String header = response.getHeader();
        assertEquals("Digest realm=\"RETS Server\", " +
                     "qop=\"auth\", " +
                     "nonce=\"246e1b9f80fd87d67c6eceffbcf89941\", " +
                     "opaque=\"121d932ad13ff598b0df1d700e422812\", " +
                     "stale=true", header);
    }
}
