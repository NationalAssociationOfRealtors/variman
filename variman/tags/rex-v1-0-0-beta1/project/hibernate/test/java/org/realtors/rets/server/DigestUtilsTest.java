/*
 */
package org.realtors.rets.server;

import junit.framework.TestCase;

public class DigestUtilsTest extends TestCase
{
    public void testMd5Hex()
    {
        // Examples from RFC 1321
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", md5(""));
        assertEquals("0cc175b9c0f1b6a831c399e269772661", md5("a"));
        assertEquals("900150983cd24fb0d6963f7d28e17f72", md5("abc"));
        assertEquals("f96b697d7cb7938d525a2f31aaf161d0", md5("message digest"));
        assertEquals("c3fcd3d76192e4007dfb496cca67e13b",
                     md5("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("d174ab98d277d9f5a5611c2c9f419d9f",
                     md5("ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                         "abcdefghijklmnopqrstuvwxyz" +
                         "0123456789"));
        assertEquals("57edf4a22be3c955ac49da2e2107b67a",
                     md5("1234567890123456789012345678901234567890" +
                         "1234567890123456789012345678901234567890"));
    }

    private String md5(String data)
    {
        return DigestUtils.md5Hex(data);
    }

    public void testShaHex()
    {
        // Examples from FIPS 180-1
        assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", sha("abc"));
        assertEquals("84983e441c3bd26ebaae4aa1f95129e5e54670f1",
                     sha("abcdbcdecdefdefgefghfghighij" +
                         "hijkijkljklmklmnlmnomnopnopq"));
    }

    private String sha(String data)
    {
        return DigestUtils.shaHex(data);
    }
}
