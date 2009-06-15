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

public class AlignmentEnum
{
    public static final AlignmentEnum LEFT = new AlignmentEnum(0);
    public static final AlignmentEnum RIGHT = new AlignmentEnum(1);
    public static final AlignmentEnum CENTER = new AlignmentEnum(2);
    public static final AlignmentEnum JUSTIFY = new AlignmentEnum(3);

    private static Map mStringMap;

    static
    {
        mStringMap = new HashMap();
        mStringMap.put("left", LEFT);
        mStringMap.put("right", RIGHT);
        mStringMap.put("center", CENTER);
        mStringMap.put("justify", JUSTIFY);
    }

    private AlignmentEnum(int code)
    {
        this.mCode = code;
    }

    public int toInt()
    {
        return mCode;
    }

    public static AlignmentEnum fromInt(int code)
    {
        switch (code)
        {
            case 0: return LEFT;
            case 1: return RIGHT;
            case 2: return CENTER;
            case 3: return JUSTIFY;
            default:
                throw new IllegalArgumentException("Unknown Alignment: " +
                                                   code);
        }
    }

    public static AlignmentEnum fromString(String value)
    {
        return (AlignmentEnum) mStringMap.get(value.toLowerCase());
    }

    public String toString()
    {
        return toString(toInt());
    }

    public static String toString(int code)
    {
        switch (code)
        {
            case 0: return "Left";
            case 1: return "Right";
            case 2: return "Center";
            case 3: return "Justify";
            default:
                throw new IllegalArgumentException("Unknown Alignment: " +
                                                   code);
        }
    }

    public static String toString(AlignmentEnum ae)
    {
        return toString(ae.toInt());
    }

    private final int mCode;
}
