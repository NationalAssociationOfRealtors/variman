/*
 */
package org.realtors.rets.server.webapp.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.exception.NestableError;

public class Util
{

    private static synchronized MessageDigest getDigest()
    {
        try
        {
            if (mDigest == null)
            {
                mDigest = MessageDigest.getInstance("MD5");
            }
            return mDigest;
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new NestableError(e);
        }
    }

    public static String md5(String data)
    {
        byte[] bytes = getDigest().digest(data.getBytes());
        return hexStringFromBytes(bytes);
    }

    public static String hexStringFromBytes(byte[] b)
    {
        StringBuffer hex = new StringBuffer(b.length * 2);
        int msb;
        int lsb = 0;
        int i;
        // MSB maps to idx 0
        for (i = 0; i < b.length; i++)
        {
            msb = ((int) b[i] & 0x000000FF) / 16;
            lsb = ((int) b[i] & 0x000000FF) % 16;
            hex.append(sHexChars[msb]).append(sHexChars[lsb]);
        }
        return hex.toString();
    }

    private static MessageDigest mDigest = null;
    private static final char[] sHexChars = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f'
    };
}
