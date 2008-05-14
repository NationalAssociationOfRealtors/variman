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


public class InterpretationEnum
{
    public static final InterpretationEnum NUMBER = new InterpretationEnum(0);
    public static final InterpretationEnum CURRENCY =
        new InterpretationEnum(1);
    public static final InterpretationEnum LOOKUP = new InterpretationEnum(2);
    public static final InterpretationEnum LOOKUPMULTI =
        new InterpretationEnum(3);
    public static final InterpretationEnum LOOKUPBITSTRING =
        new InterpretationEnum(4);
    public static final InterpretationEnum LOOKUPBITMASK =
        new InterpretationEnum(5);

    private static Map mStringMap;

    static
    {
        mStringMap = new HashMap();
        mStringMap.put("number", NUMBER);
        mStringMap.put("currency", CURRENCY);
        mStringMap.put("lookup", LOOKUP);
        mStringMap.put("lookupmulti", LOOKUPMULTI);
        mStringMap.put("lookupbitstring", LOOKUPBITSTRING);
        mStringMap.put("lookupbitmask", LOOKUPBITMASK);
    }

    private InterpretationEnum(int code)
    {
        this.mCode = code;
    }

    public int toInt()
    {
        return mCode;
    }

    public static InterpretationEnum fromInt(int code)
    {
        switch (code)
        {
            case 0: return NUMBER;
            case 1: return CURRENCY;
            case 2: return LOOKUP;
            case 3: return LOOKUPMULTI;
            case 4: return LOOKUPBITSTRING;
            case 5: return LOOKUPBITMASK;
            default:
                throw new IllegalArgumentException("Unknown code: " + code);
        }
    }

    public static InterpretationEnum fromString(String value)
    {
        return (InterpretationEnum) mStringMap.get(value.toLowerCase());
    }

    public String toString()
    {
        return toString(toInt());
    }

    public static String toString(int code)
    {
        switch (code)
        {
            case 0: return "Number";
            case 1: return "Currency";
            case 2: return "Lookup";
            case 3: return "LookupMulti";
            case 4: return "LookupBitstring";
            case 5: return "LookupBitmask";
            default:
                throw new IllegalArgumentException("Unknown code: " + code);
        }
    }

    public static String toString(InterpretationEnum ie)
    {
        return toString(ie.toInt());
    }

    private final int mCode;
}
