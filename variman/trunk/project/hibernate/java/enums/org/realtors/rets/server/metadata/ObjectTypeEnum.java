package org.realtors.rets.server.metadata;

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

    private ObjectTypeEnum(int code)
    {
        this.mCode = code;
    }

    public int toInt() { return mCode; }

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

    private final int mCode;
}
