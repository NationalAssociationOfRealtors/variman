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

public class ObjectTypeEnum
{
    public static final ObjectTypeEnum PHOTO = new ObjectTypeEnum(0);
    public static final ObjectTypeEnum PLAT = new ObjectTypeEnum(1);
    public static final ObjectTypeEnum VIDEO = new ObjectTypeEnum(2);
    public static final ObjectTypeEnum AUDIO = new ObjectTypeEnum(3);
    public static final ObjectTypeEnum THUMBNAIL = new ObjectTypeEnum(4);
    public static final ObjectTypeEnum MAP = new ObjectTypeEnum(5);
    public static final ObjectTypeEnum VRIMAGE = new ObjectTypeEnum(6);
 
    private static int mNext = 7;
    private static Map mEnumMap;
    private static Map mStringMap;

    static
    {
    	mEnumMap = new HashMap();
    	mEnumMap.put(0, "Photo");
        mEnumMap.put(1, "Plat");
        mEnumMap.put(2, "Video");
        mEnumMap.put(3, "Audio");
        mEnumMap.put(4, "Thumbnail");
        mEnumMap.put(5, "Map");
        mEnumMap.put(6, "VRImage");
    	
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
    	String key = (String) mEnumMap.get(code);
    	
    	if (key == null)
        	throw new RuntimeException("Unknown Object Type");
    	
   		return (ObjectTypeEnum) mStringMap.get(key);
    }

    public static ObjectTypeEnum fromString(String value)
    {
    	ObjectTypeEnum ote = (ObjectTypeEnum) mStringMap.get(value.toLowerCase());
    	if (ote == null)
    	{
    		synchronized (ObjectTypeEnum.class)
    		{
    			ote = new ObjectTypeEnum(mNext);
    			mEnumMap.put(mNext++, value);
    			mStringMap.put(value.toLowerCase(), ote);
    		}
    	}
        return ote;
    }

    public String toString()
    {
        return toString(toInt());
    }

    public static String toString(int code)
    {
    	String key = (String) mEnumMap.get(code);
    	
    	if (key == null)
        	throw new RuntimeException("Unknown Object Type");
    	
    	return key;
    }

    public static String toString(ObjectTypeEnum ote)
    {
        return toString(ote.toInt());
    }

    private final int mCode;
}
