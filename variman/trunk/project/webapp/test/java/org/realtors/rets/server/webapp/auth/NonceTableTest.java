/*
 */
package org.realtors.rets.server.webapp.auth;

import junit.framework.TestCase;
import org.apache.commons.lang.time.DateUtils;

public class NonceTableTest extends TestCase
{
    public void testGenerateNonce()
    {
        NonceTable nonceTable = new NonceTable();

        String nonce1 = nonceTable.generateNonce();
        String opaque1 = nonceTable.getOpaque(nonce1);
        String nonce2 = nonceTable.generateNonce();
        String opaque2 = nonceTable.getOpaque(nonce2);
        assertTrue(!nonce1.equals(nonce2));
        assertTrue(!opaque1.equals(opaque2));
        assertTrue(nonceTable.isValidNonce(nonce1));
        assertTrue(nonceTable.isValidNonce(nonce2));
    }

    public void testValidateRfc2617Request()
    {
        NonceTable nonceTable = new NonceTable();
        String nonce = nonceTable.generateNonce();
        String opaque = nonceTable.getOpaque(nonce);
        DigestAuthorizationRequest request = new DigestAuthorizationRequest();
        request.setQop("auth");
        request.setNonce(nonce);
        request.setOpaque(opaque);
        request.setNonceCount("00000001");
        assertTrue(nonceTable.validateRequest(request));
        request.setNonceCount("00000002");
        assertTrue(nonceTable.validateRequest(request));
        request.setNonceCount("0000000f");
        assertTrue(nonceTable.validateRequest(request));
        request.setNonceCount("00000010");
        assertTrue(nonceTable.validateRequest(request));
        // Try same nonce twice in a row
        assertFalse(nonceTable.validateRequest(request));
        // Try an old nonce count
        request.setNonceCount("00000001");
        assertFalse(nonceTable.validateRequest(request));
        // Try no opaque value
        request.setOpaque("");
        assertFalse(nonceTable.validateRequest(request));
        // Try no nonce value
        request.setOpaque(opaque);
        request.setNonce("");
        assertFalse(nonceTable.validateRequest(request));
        // Make sure the request validates, even after previous failures
        request.setNonce(nonce);
        request.setNonceCount("f0000000");
        assertTrue(nonceTable.validateRequest(request));
    }

    public void testValidateRfc2609Request()
    {
        NonceTable nonceTable = new NonceTable();
        String nonce = nonceTable.generateNonce();

        DigestAuthorizationRequest request = new DigestAuthorizationRequest();
        request.setQop(null);
        assertFalse(nonceTable.validateRequest(request));
        request.setNonce(nonce);
        assertTrue(nonceTable.validateRequest(request));
        request.setQop("auth");
        assertFalse(nonceTable.validateRequest(request));
    }

    public void testExpireNonces() throws InterruptedException
    {
        long time = System.currentTimeMillis();
        NonceTable nonceTable = new NonceTable();
        String nonce1 = nonceTable.generateNonce();
        // Set time to be one minute in the past
        nonceTable.setExpirationTime(nonce1, time - DateUtils.MILLIS_PER_MINUTE);
        String nonce2 = nonceTable.generateNonce();
        // Set time to be one minute in the future
        nonceTable.setExpirationTime(nonce2, time + DateUtils.MILLIS_PER_MINUTE);

        assertTrue(nonceTable.isValidNonce(nonce1));
        assertTrue(nonceTable.isValidNonce(nonce2));

        // This should invalidate nonce1, but keep nonce2
        nonceTable.expireNonces();
        assertFalse(nonceTable.isValidNonce(nonce1));
        assertTrue(nonceTable.isValidNonce(nonce2));
    }

    public void testInitialExpirationTime()
    {
        long time = System.currentTimeMillis();
        NonceTable nonceTable = new NonceTable();
        String nonce = nonceTable.generateNonce();
        long expirationTime = nonceTable.getExpirationTime(nonce);
        // Due to timing issues, the exact expiration time cannot be checked, so
        // make sure it is within a minute of expected
        assertTrue(expirationTime >=
                   (time + NonceTable.DEFAULT_INITIAL_TIMEOUT));
        assertTrue(expirationTime <=
                   (time + NonceTable.DEFAULT_INITIAL_TIMEOUT +
                    DateUtils.MILLIS_PER_MINUTE));
    }

    public void testGood2617ValidateExpirationTime()
    {
        long time = System.currentTimeMillis();
        NonceTable nonceTable = new NonceTable();
        String nonce = nonceTable.generateNonce();
        DigestAuthorizationRequest request = new DigestAuthorizationRequest();
        request.setQop("auth");
        request.setNonce(nonce);
        request.setOpaque(nonceTable.getOpaque(nonce));
        request.setNonceCount("00000001");
        nonceTable.validateRequest(request);
        long expirationTime = nonceTable.getExpirationTime(nonce);
        // Due to timing issues, the exact expiration time cannot be checked, so
        // make sure it is within a minute of expected
        assertTrue(expirationTime >=
                   (time + NonceTable.DEFAULT_SUCCESS_TIMEOUT));
        assertTrue(expirationTime <=
                   (time + NonceTable.DEFAULT_SUCCESS_TIMEOUT +
                    DateUtils.MILLIS_PER_MINUTE));
    }

    public void testGood2609ValidateExpirationTime()
    {
        long time = System.currentTimeMillis();
        NonceTable nonceTable = new NonceTable();
        String nonce = nonceTable.generateNonce();
        DigestAuthorizationRequest request = new DigestAuthorizationRequest();
        request.setQop(null);
        request.setNonce(nonce);
        nonceTable.validateRequest(request);
        long expirationTime = nonceTable.getExpirationTime(nonce);
        // Due to timing issues, the exact expiration time cannot be checked, so
        // make sure it is within a minute of expected
        assertTrue(expirationTime >=
                   (time + NonceTable.DEFAULT_SUCCESS_TIMEOUT));
        assertTrue(expirationTime <=
                   (time + NonceTable.DEFAULT_SUCCESS_TIMEOUT +
                    DateUtils.MILLIS_PER_MINUTE));
    }

    public void testBad2617ValidateExpirationTime()
    {
        long time = System.currentTimeMillis();
        NonceTable nonceTable = new NonceTable();
        String nonce = nonceTable.generateNonce();
        DigestAuthorizationRequest request = new DigestAuthorizationRequest();
        request.setQop("auth");
        request.setNonce(nonce);
        request.setOpaque(nonceTable.getOpaque(nonce));
        request.setNonceCount("00000000");
        nonceTable.validateRequest(request);
        long expirationTime = nonceTable.getExpirationTime(nonce);
        // Due to timing issues, the exact expiration time cannot be checked, so
        // make sure it is within a minute of expected
        assertTrue(expirationTime >=
                   (time + NonceTable.DEFAULT_INITIAL_TIMEOUT));
        assertTrue(expirationTime <=
                   (time + NonceTable.DEFAULT_INITIAL_TIMEOUT +
                    DateUtils.MILLIS_PER_MINUTE));
    }

    public void testBad2609ValidateExpirationTime()
    {
        long time = System.currentTimeMillis();
        NonceTable nonceTable = new NonceTable();
        String nonce = nonceTable.generateNonce();
        DigestAuthorizationRequest request = new DigestAuthorizationRequest();
        request.setQop(null);
        request.setNonce(nonce + "foo");
        nonceTable.validateRequest(request);
        long expirationTime = nonceTable.getExpirationTime(nonce);
        // Due to timing issues, the exact expiration time cannot be checked, so
        // make sure it is within a minute of expected
        assertTrue(expirationTime >=
                   (time + NonceTable.DEFAULT_INITIAL_TIMEOUT));
        assertTrue(expirationTime <=
                   (time + NonceTable.DEFAULT_INITIAL_TIMEOUT +
                    DateUtils.MILLIS_PER_MINUTE));
    }
}
