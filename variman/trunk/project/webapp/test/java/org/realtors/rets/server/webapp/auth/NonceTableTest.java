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

    public void testNonceCountMustIncrease()
    {
        NonceTable nonceTable = new NonceTable();
        String nonce = nonceTable.generateNonce();
        assertTrue(nonceTable.isValidNonce(nonce));
        String opaque = nonceTable.getOpaque(nonce);
        assertTrue(nonceTable.validateRequest(nonce, opaque, "00000001"));
        assertTrue(nonceTable.validateRequest(nonce, opaque, "00000002"));
        assertTrue(nonceTable.validateRequest(nonce, opaque, "0000000f"));
        assertTrue(nonceTable.validateRequest(nonce, opaque, "00000010"));
        assertFalse(nonceTable.validateRequest(nonce, opaque, "00000001"));
        assertFalse(nonceTable.validateRequest(nonce, "", "f0000000"));
        assertFalse(nonceTable.validateRequest("", opaque, "f0000000"));
        assertTrue(nonceTable.validateRequest(nonce, opaque, "f0000000"));
    }

    public void testExpireNonces() throws InterruptedException
    {
        long time = System.currentTimeMillis();
        NonceTable nonceTable = new NonceTable();
        String nonce1 = nonceTable.generateNonce();
        // Set time to be one minute in the past
        nonceTable.setExpirationTime(nonce1, time - DateUtils.MILLIS_IN_MINUTE);
        String nonce2 = nonceTable.generateNonce();
        // Set time to be one minute in the future
        nonceTable.setExpirationTime(nonce2, time + DateUtils.MILLIS_IN_MINUTE);

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
                    DateUtils.MILLIS_IN_MINUTE));
    }

    public void testBadValidateExpirationTime()
    {
        long time = System.currentTimeMillis();
        NonceTable nonceTable = new NonceTable();
        String nonce = nonceTable.generateNonce();
        String opaque = nonceTable.getOpaque(nonce);
        nonceTable.validateRequest(nonce, opaque, "00000000");
        long expirationTime = nonceTable.getExpirationTime(nonce);
        // Due to timing issues, the exact expiration time cannot be checked, so
        // make sure it is within a minute of expected
        assertTrue(expirationTime >=
                   (time + NonceTable.DEFAULT_INITIAL_TIMEOUT));
        assertTrue(expirationTime <=
                   (time + NonceTable.DEFAULT_INITIAL_TIMEOUT +
                    DateUtils.MILLIS_IN_MINUTE));
    }

    public void testGoodValidateExpirationTime()
    {
        long time = System.currentTimeMillis();
        NonceTable nonceTable = new NonceTable();
        String nonce = nonceTable.generateNonce();
        String opaque = nonceTable.getOpaque(nonce);
        nonceTable.validateRequest(nonce, opaque, "00000001");
        long expirationTime = nonceTable.getExpirationTime(nonce);
        // Due to timing issues, the exact expiration time cannot be checked, so
        // make sure it is within a minute of expected
        assertTrue(expirationTime >=
                   (time + NonceTable.DEFAULT_SUCCESS_TIMEOUT));
        assertTrue(expirationTime <=
                   (time + NonceTable.DEFAULT_SUCCESS_TIMEOUT +
                    DateUtils.MILLIS_IN_MINUTE));
    }
}
