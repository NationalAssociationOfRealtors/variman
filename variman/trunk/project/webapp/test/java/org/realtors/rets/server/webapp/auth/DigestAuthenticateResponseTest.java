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

    public void testResponseHedaerGenerateNonce()
    {
        DigestAuthenticateResponse response1 =
            new DigestAuthenticateResponse("RETS Server");
        DigestAuthenticateResponse response2 =
            new DigestAuthenticateResponse("RETS Server");

        // nonce and opage should get generated
        assertFalse(response1.getNonce().equals(response2.getNonce()));
        assertFalse(response1.getOpaque().equals(response2.getOpaque()));
    }
}
