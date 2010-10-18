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

public class UnitEnum
{
    public static final UnitEnum FEET = new UnitEnum(0);
    public static final UnitEnum METERS = new UnitEnum(1);
    public static final UnitEnum SQFT = new UnitEnum(2);
    public static final UnitEnum SQMETERS = new UnitEnum(3);
    public static final UnitEnum ACRES = new UnitEnum(4);
    public static final UnitEnum HECTARES = new UnitEnum(5);

    private static Map mStringMap;

    static
    {
        mStringMap = new HashMap();
        mStringMap.put("feet", FEET);
        mStringMap.put("meters", METERS);
        mStringMap.put("sqft", SQFT);
        mStringMap.put("sqmeters", SQMETERS);
        mStringMap.put("acres", ACRES);
        mStringMap.put("hectares", HECTARES);
    }

    private UnitEnum(int code)
    {
        this.mCode = code;
    }

    public int toInt()
    {
        return mCode;
    }

    public static UnitEnum fromInt(int code)
    {
        switch (code)
        {
            case 0: return FEET;
            case 1: return METERS;
            case 2: return SQFT;
            case 3: return SQMETERS;
            case 4: return ACRES;
            case 5: return HECTARES;
            default: throw new RuntimeException("Unknown Unit");
        }
    }

    public static UnitEnum fromString(String value)
    {
        return (UnitEnum) mStringMap.get(value.toLowerCase());
    }

    public String toString()
    {
        return toString(toInt());
    }

    public static String toString(int code)
    {
        switch (code)
        {
            case 0: return "Feet";
            case 1: return "Meters";
            case 2: return "SqFt";
            case 3: return "SqMeters";
            case 4: return "Acres";
            case 5: return "Hectares";
            default: throw new RuntimeException("Unknown Unit");
        }
    }

    public static String toString(UnitEnum ue)
    {
        return toString(ue.toInt());
    }

    private final int mCode;
}
