/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.exception.NestableError;

/**
 * Operations to simplifiy common <code>MessageDigest</code> tasks.
 *
 * @author Dave Dribin
 */
public class DigestUtils
{
    /**
     * Calculates the MD5 digest and returns the value as a hex string.
     *
     * @param data Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(String data)
    {
        return hexStringFromBytes(md5(data));
    }

    /**
     * Calculates the MD5 digest and returns the value as a hex string.
     *
     * @param data Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(byte[] data)
    {
        return hexStringFromBytes(md5(data));
    }

    /**
     * Calculates the MD5 digest and returns the value as a byte array.
     *
     * @param data Data to digest
     * @return MD5 digest
     */
    public static byte[] md5(String data)
    {
        return md5(data.getBytes());
    }

    /**
     * Calculates the MD5 digest and resturns the value as a byte array.
     *
     * @param data Data to digest
     * @return MD5 digest
     */
    public static byte[] md5(byte[] data)
    {
        return getMd5Digest().digest(data);
    }

    /**
     * Calculates the SHA digest and returns the value as a hex string.
     *
     * @param data Data to digest
     * @return SHA digest as a hex string
     */
    public static String shaHex(String data)
    {
        return hexStringFromBytes(sha(data));
    }

    public static String shaHex(byte[] data)
    {
        return hexStringFromBytes(sha(data));
    }

    /**
     * Calculates the SHA digest and returns the value as a byte array.
     *
     * @param data Data to digest
     * @return SHA digest
     */
    public static byte[] sha(String data)
    {
        return sha(data.getBytes());
    }

    /**
     * Calculates the SHA digest and returns the value as a byte array.
     *
     * @param data Data to digest
     * @return SHA digest
     */
    public static byte[] sha(byte[] data)
    {
        return getShaDigest().digest(data);
    }

    /**
     * Converts an array of bytes into a lower-case hex string.
     *
     * @param bytes Byte array to convert
     * @return Hex string
     */
    public static String hexStringFromBytes(byte[] bytes)
    {
        StringBuffer hex = new StringBuffer(bytes.length * 2);
        // Most significant byte maps to index 0
        for (int i = 0; i < bytes.length; i++)
        {
            int highNybble = ((int) bytes[i] & 0x000000F0) >> 4;
            int lowNybble  = ((int) bytes[i] & 0x0000000F);
            hex.append(HEX_CHARS[highNybble]).append(HEX_CHARS[lowNybble]);
        }
        return hex.toString();
    }

    /**
     * Returns the singleton MD5 digest.
     *
     * @return the singleton MD5 digest
     */
    private static synchronized MessageDigest getMd5Digest()
    {
        try
        {
            if (sMd5Digest == null)
            {
                sMd5Digest = MessageDigest.getInstance("MD5");
            }
            return sMd5Digest;
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new NestableError(e);
        }
    }

    /**
     * Returns the singleton SHA digest.
     *
     * @return the singleton SHA digest
     */
    private static synchronized MessageDigest getShaDigest()
    {
        try
        {
            if (sShaDigest == null)
            {
                sShaDigest = MessageDigest.getInstance("SHA");
            }
            return sShaDigest;
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new NestableError(e);
        }
    }

    /** Singleton instance of MD5 digest. */
    private static MessageDigest sMd5Digest = null;

    /** Singleton instance of SHA digest */
    private static MessageDigest sShaDigest;

    /** Array to convert an int to a lower case hexadecimal digit. */
    private static final char[] HEX_CHARS = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f'
    };
}
