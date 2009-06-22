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

/*
 * TODO: Remove dependency on Hibernate 2. Don't have any dependency on any version of Hibernate.
 * TODO: Add the ability for extensibility to allow other object types.
 */
public class ObjectTypeEnum implements PersistentEnum
{
    public static final ObjectTypeEnum PHOTO = new ObjectTypeEnum(0);
    public static final ObjectTypeEnum PLAT = new ObjectTypeEnum(1);
    public static final ObjectTypeEnum VIDEO = new ObjectTypeEnum(2);
    public static final ObjectTypeEnum AUDIO = new ObjectTypeEnum(3);
    public static final ObjectTypeEnum THUMBNAIL = new ObjectTypeEnum(4);
    public static final ObjectTypeEnum MAP = new ObjectTypeEnum(5);
    public static final ObjectTypeEnum VRIMAGE = new ObjectTypeEnum(6);
    public static final ObjectTypeEnum PHOTO160x120 = new ObjectTypeEnum(100);
    public static final ObjectTypeEnum PHOTO240x180 = new ObjectTypeEnum(101);
    public static final ObjectTypeEnum PHOTO320x240 = new ObjectTypeEnum(102);
    public static final ObjectTypeEnum PHOTO480x360 = new ObjectTypeEnum(103);
    public static final ObjectTypeEnum PHOTO640x480 = new ObjectTypeEnum(104);

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
        mStringMap.put("Photo160x120", PHOTO160x120);
        mStringMap.put("Photo240x180", PHOTO240x180);
        mStringMap.put("Photo320x240", PHOTO320x240);
        mStringMap.put("Photo480x360", PHOTO480x360);
        mStringMap.put("Photo640x480", PHOTO640x480);
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
            case 100: return PHOTO160x120;
            case 101: return PHOTO240x180;
            case 102: return PHOTO320x240;
            case 103: return PHOTO480x360;
            case 104: return PHOTO640x480;
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
            case 5: return "Map";
            case 6: return "VRImage";
            case 100: return "Photo160x120";
            case 101: return "Photo240x180";
            case 102: return "Photo320x240";
            case 103: return "Photo480x360";
            case 104: return "Photo640x480";
            default: throw new RuntimeException("Unknown Object Type");
        }
    }

    public static String toString(ObjectTypeEnum ote)
    {
        return toString(ote.toInt());
    }

    private final int mCode;
}
