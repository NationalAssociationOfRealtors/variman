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

public class ResourceStandardNameEnum
{
    public static final ResourceStandardNameEnum ACTIVE_AGENT =
        new ResourceStandardNameEnum(0);
    public static final ResourceStandardNameEnum AGENT  =
        new ResourceStandardNameEnum(1);
    public static final ResourceStandardNameEnum HISTORY =
        new ResourceStandardNameEnum(2);
    public static final ResourceStandardNameEnum OFFICE =
        new ResourceStandardNameEnum(3);
    public static final ResourceStandardNameEnum OPEN_HOUSE =
        new ResourceStandardNameEnum(4);
    public static final ResourceStandardNameEnum PROPERTY =
        new ResourceStandardNameEnum(5);
    public static final ResourceStandardNameEnum PROSPECT =
        new ResourceStandardNameEnum(6);
    public static final ResourceStandardNameEnum TAX =
        new ResourceStandardNameEnum(7);
    public static final ResourceStandardNameEnum TOUR =
        new ResourceStandardNameEnum(8);

    private static Map mStringMap;

    static
    {
        mStringMap = new HashMap();
        mStringMap.put("activeagent", ACTIVE_AGENT);
        mStringMap.put("agent", AGENT);
        mStringMap.put("history", HISTORY);
        mStringMap.put("office", OFFICE);
        mStringMap.put("openhouse", OPEN_HOUSE);
        mStringMap.put("property", PROPERTY);
        mStringMap.put("prospect", PROSPECT);
        mStringMap.put("tax", TAX);
        mStringMap.put("tour", TOUR);
    }

    private ResourceStandardNameEnum(int code)
    {
        this.mCode = code;
    }

    public int toInt()
    {
        return mCode;
    }

    public static ResourceStandardNameEnum fromInt(int code)
    {
        switch (code)
        {
            case 0: return ACTIVE_AGENT;
            case 1: return AGENT;
            case 2: return HISTORY;
            case 3: return OFFICE;
            case 4: return OPEN_HOUSE;
            case 5: return PROPERTY;
            case 6: return PROSPECT;
            case 7: return TAX;
            case 8: return TOUR;
            default:
                throw new RuntimeException("Unknown Resource Standard Name");
        }
    }

    public static ResourceStandardNameEnum fromString(String value)
    {
        return (ResourceStandardNameEnum) mStringMap.get(value.toLowerCase());
    }

    public String toString()
    {
        return toString(toInt());
    }

    public static String toString(int code)
    {
        switch (code)
        {
            case 0: return "ActiveAgent";
            case 1: return "Agent";
            case 2: return "History";
            case 3: return "Office";
            case 4: return "OpenHouse";
            case 5: return "Property";
            case 6: return "Prospect";
            case 7: return "Tax";
            case 8: return "Tour";
            default:
                throw new RuntimeException("Unknown Resource Standard Name");
        }
    }

    public static String toString(ResourceStandardNameEnum rsne)
    {
        return toString(rsne.toInt());
    }

    private final int mCode;
}
