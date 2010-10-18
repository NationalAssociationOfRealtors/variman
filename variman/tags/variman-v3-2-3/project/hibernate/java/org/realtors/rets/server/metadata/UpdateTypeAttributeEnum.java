/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;


public class UpdateTypeAttributeEnum
{
    public static final UpdateTypeAttributeEnum DISPLAYONLY =
        new UpdateTypeAttributeEnum(1);
    public static final UpdateTypeAttributeEnum REQUIRED =
        new UpdateTypeAttributeEnum(2);
    public static final UpdateTypeAttributeEnum AUTOPOP =
        new UpdateTypeAttributeEnum(3);
    public static final UpdateTypeAttributeEnum INTERACTIVEVALIDATE =
        new UpdateTypeAttributeEnum(4);
    public static final UpdateTypeAttributeEnum CLEARONCLONING =
        new UpdateTypeAttributeEnum(5);

    private UpdateTypeAttributeEnum(int code)
    {
        this.mCode = code;
    }

    public int toInt()
    {
        return mCode;
    }

    public static UpdateTypeAttributeEnum fromInt(int code)
    {
        switch (code)
        {
            case 1: return DISPLAYONLY;
            case 2: return REQUIRED;
            case 3: return AUTOPOP;
            case 4: return INTERACTIVEVALIDATE;
            case 5: return CLEARONCLONING;
            default:
                throw new IllegalArgumentException(
                    "Unkonwn Update Type Attribute: " + code);
        }
    }

    public String toString()
    {
        return "" + mCode;
    }

    private final int mCode;
}
                
