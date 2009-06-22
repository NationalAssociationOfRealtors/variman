/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.config;

public class RetsConfigUtils
{
    private RetsConfigUtils()
    {
        // Prevents instantiation.
    }
    
    public static String getPhotoPattern(final RetsConfig retsConfig, final String defaultValue)
    {
        return getDefault(retsConfig.getPhotoPattern(), defaultValue);
    }

    public static String  getGetObjectRoot(final RetsConfig retsConfig, final String defaultValue)
    {
        return getDefault(retsConfig.getGetObjectRoot(), defaultValue);
    }

    public static String getObjectSetPattern(final RetsConfig retsConfig, final String defaultValue)
    {
        return getDefault(retsConfig.getObjectSetPattern(), defaultValue);
    }

    public static int getNonceInitialTimeout(final RetsConfig retsConfig, final int defaultValue)
    {
        return getDefault(retsConfig.getNonceInitialTimeout(), defaultValue);
    }

    public static int getNonceSuccessTimeout(final RetsConfig retsConfig, final int defaultValue)
    {
        return getDefault(retsConfig.getNonceSuccessTimeout(), defaultValue);
    }

    public static String getMetadataDir(final RetsConfig retsConfig, final String defaultValue)
    {
        return getDefault(retsConfig.getMetadataDir(), defaultValue);
    }

    private static String getDefault(final String string, final String defaultValue)
    {
        if (string == null)
        {
            return defaultValue;
        }
        else
        {
            return string;
        }
    }

    private static int getDefault(final int number, final int defaultValue)
    {
        if (number == -1)
        {
            return defaultValue;
        }
        else
        {
            return number;
        }
    }

}
