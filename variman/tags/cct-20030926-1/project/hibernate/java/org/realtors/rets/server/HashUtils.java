/*
 */
package org.realtors.rets.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.exception.NestableError;

public class HashUtils
{
    /**
     * Calculates the HashUtils hash and returns the hex value as a string.
     *
     * @param data Data to hash
     * @return HashUtils hash as a hex string
     */
    public static String md5(String data)
    {
        byte[] bytes = getMd5Digest().digest(data.getBytes());
        return hexStringFromBytes(bytes);
    }

    /**
     * Converts an array of bytes into a hex string.
     *
     * @param bytes Byte array to convert
     * @return hex string
     */
    public static String hexStringFromBytes(byte[] bytes)
    {
        StringBuffer hex = new StringBuffer(bytes.length * 2);
        int msb;
        int lsb = 0;
        int i;
        // MSB maps to idx 0
        for (i = 0; i < bytes.length; i++)
        {
            msb = ((int) bytes[i] & 0x000000FF) / 16;
            lsb = ((int) bytes[i] & 0x000000FF) % 16;
            hex.append(HEX_CHARS[msb]).append(HEX_CHARS[lsb]);
        }
        return hex.toString();
    }

    private static synchronized MessageDigest getMd5Digest()
    {
        try
        {
            if (mMd5Digest == null)
            {
                mMd5Digest = MessageDigest.getInstance("MD5");
            }
            return mMd5Digest;
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new NestableError(e);
        }
    }

    private static MessageDigest mMd5Digest = null;
    private static final char[] HEX_CHARS = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f'
    };
}
