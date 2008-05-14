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


public class DataTypeEnum
{
    public static final DataTypeEnum BOOLEAN = new DataTypeEnum(0);
    public static final DataTypeEnum CHARACTER = new DataTypeEnum(1);
    public static final DataTypeEnum DATE = new DataTypeEnum(2);
    public static final DataTypeEnum DATETIME = new DataTypeEnum(3);
    public static final DataTypeEnum TIME = new DataTypeEnum(4);
    public static final DataTypeEnum TINY = new DataTypeEnum(5);
    public static final DataTypeEnum SMALL = new DataTypeEnum(6);
    public static final DataTypeEnum INT = new DataTypeEnum(7);
    public static final DataTypeEnum LONG = new DataTypeEnum(8);
    public static final DataTypeEnum DECIMAL = new DataTypeEnum(9);

    private static Map mStringMap;

    static
    {
        mStringMap = new HashMap();
        mStringMap.put("boolean", BOOLEAN);
        mStringMap.put("character", CHARACTER);
        mStringMap.put("date", DATE);
        mStringMap.put("datetime", DATETIME);
        mStringMap.put("time", TIME);
        mStringMap.put("tiny", TINY);
        mStringMap.put("small", SMALL);
        mStringMap.put("int", INT);
        mStringMap.put("long", LONG);
        mStringMap.put("decimal", DECIMAL);
    }

    private DataTypeEnum(int dataType)
    {
        this.mDataType = dataType;
    }

    public int toInt()
    {
        return mDataType;
    }

    public static DataTypeEnum fromInt(int dataType)
    {
        switch (dataType)
        {
            case 0: return BOOLEAN;
            case 1: return CHARACTER;
            case 2: return DATE;
            case 3: return DATETIME;
            case 4: return TIME;
            case 5: return TINY;
            case 6: return SMALL;
            case 7: return INT;
            case 8: return LONG;
            case 9: return DECIMAL;
            default:
                throw new IllegalArgumentException("Unknown code: " + dataType);
        }
    }

    // todo: fix this stuff
    public static DataTypeEnum fromString(String value)
    {
        return (DataTypeEnum) mStringMap.get(value.toLowerCase());
    }

    public String toString()
    {
        return toString(toInt());
    }

    public static String toString(int code)
    {
        switch (code)
        {
            case 0: return "Boolean";
            case 1: return "Character";
            case 2: return "Date";
            case 3: return "DateTime";
            case 4: return "Time";
            case 5: return "Tiny";
            case 6: return "Small";
            case 7: return "Int";
            case 8: return "Long";
            case 9: return "Decimal";
            default:
                throw new IllegalArgumentException("Unknown code: " + code);
        }
    }

    public static String toString(DataTypeEnum ote)
    {
        return toString(ote.toInt());
    }

    private final int mDataType;
}
