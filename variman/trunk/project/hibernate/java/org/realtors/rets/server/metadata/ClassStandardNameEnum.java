/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import java.util.HashMap;
import java.util.Map;

public class ClassStandardNameEnum
{
    public static final ClassStandardNameEnum RESIDENTIAL =
        new ClassStandardNameEnum(0);
    public static final ClassStandardNameEnum LOTS_AND_LAND  =
        new ClassStandardNameEnum(1);
    public static final ClassStandardNameEnum COMMON_INTEREST =
        new ClassStandardNameEnum(2);
    public static final ClassStandardNameEnum MULTI_FAMILY =
        new ClassStandardNameEnum(3);

    private static Map mStringMap;

    static
    {
        mStringMap = new HashMap();
        //FIXME: We must get more from the XML DTD
        mStringMap.put("residential-property", RESIDENTIAL);
        mStringMap.put("residentialproperty", RESIDENTIAL);
        mStringMap.put("lotsandland", LOTS_AND_LAND);
        mStringMap.put("commoninterest", COMMON_INTEREST);
        mStringMap.put("multifamily", MULTI_FAMILY);
    }

    private ClassStandardNameEnum(int code)
    {
        this.mCode = code;
    }

    public int toInt()
    {
        return mCode;
    }

    public static ClassStandardNameEnum fromInt(int code)
    {
        switch (code)
        {
            case 0: return RESIDENTIAL;
            case 1: return LOTS_AND_LAND;
            case 2: return COMMON_INTEREST;
            case 3: return MULTI_FAMILY;
            default:
                throw new RuntimeException("Unknown Class Standard Name");
        }
    }

    public static ClassStandardNameEnum fromString(String value)
    {
        if (value == null)
        {
            return null;
        }
        else
        {
            return (ClassStandardNameEnum) mStringMap.get(value.toLowerCase());
        }
    }

    public String toString()
    {
        return toString(toInt());
    }

    public static String toString(int code)
    {
        switch (code)
        {
            case 0: return "ResidentialProperty";
            case 1: return "LotsAndLand";
            case 2: return "CommonInterest";
            case 3: return "MultiFamily";
            default:
                throw new RuntimeException("Unknown Class Standard Name");
        }
    }

    public static String toString(ClassStandardNameEnum rsne)
    {
        return toString(rsne.toInt());
    }

    private final int mCode;
}
