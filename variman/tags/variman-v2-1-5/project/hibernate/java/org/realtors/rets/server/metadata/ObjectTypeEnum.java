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

import net.sf.hibernate.PersistentEnum;

public class ObjectTypeEnum implements PersistentEnum
{
    public static final ObjectTypeEnum PHOTO = new ObjectTypeEnum(0);
    public static final ObjectTypeEnum PLAT = new ObjectTypeEnum(1);
    public static final ObjectTypeEnum VIDEO = new ObjectTypeEnum(2);
    public static final ObjectTypeEnum AUDIO = new ObjectTypeEnum(3);
    public static final ObjectTypeEnum THUMBNAIL = new ObjectTypeEnum(4);
    public static final ObjectTypeEnum MAP = new ObjectTypeEnum(5);
    public static final ObjectTypeEnum VRIMAGE = new ObjectTypeEnum(6);

    private static Map mStringMap;

    static
    {
        mStringMap = new HashMap();
        mStringMap.put("photo", PHOTO);
        mStringMap.put("plat", PLAT);
        mStringMap.put("video", VIDEO);
        mStringMap.put("audio", AUDIO);
        mStringMap.put("thumbnail", THUMBNAIL);
        mStringMap.put("map", MAP);
        mStringMap.put("vrimage", VRIMAGE);
    }

    private ObjectTypeEnum(int code)
    {
        this.mCode = code;
    }

    public int toInt()
    {
        return mCode;
    }

    public static ObjectTypeEnum fromInt(int code)
    {
        switch (code)
        {
            case 0: return PHOTO;
            case 1: return PLAT;
            case 2: return VIDEO;
            case 3: return AUDIO;
            case 4: return THUMBNAIL;
            case 5: return MAP;
            case 6: return VRIMAGE;
            default: throw new RuntimeException("Unknown Object Type");
        }
    }

    public static ObjectTypeEnum fromString(String value)
    {
        return (ObjectTypeEnum) mStringMap.get(value.toLowerCase());
    }

    public String toString()
    {
        return toString(toInt());
    }

    public static String toString(int code)
    {
        switch (code)
        {
            case 0: return "Photo";
            case 1: return "Plat";
            case 2: return "Video";
            case 3: return "Audio";
            case 4: return "Thumbnail";
            case 5: return "VRImage";
            default: throw new RuntimeException("Unknown Object Type");
        }
    }

    public static String toString(ObjectTypeEnum ote)
    {
        return toString(ote.toInt());
    }

    private final int mCode;
}
