package org.nar.rets.metadata.newdb;

import net.sf.hibernate.PersistentEnum;

public class ResourceStandardNameEnum implements PersistentEnum
{
    public static final ResourceStandardNameEnum RESIDENTIALPROPERTY =
        new ResourceStandardNameEnum(0);
    public static final ResourceStandardNameEnum LOTSANDLAND =
        new ResourceStandardNameEnum(1);
    public static final ResourceStandardNameEnum COMMONINTEREST =
        new ResourceStandardNameEnum(2);
    public static final ResourceStandardNameEnum MULTIFAMILY =
        new ResourceStandardNameEnum(3);

    private ResourceStandardNameEnum(int code)
    {
        this.mCode = code;
    }

    public int toInt() { return mCode; }

    public static ResourceStandardNameEnum fromInt(int code)
    {
        switch (code)
        {
            case 0: return RESIDENTIALPROPERTY;
            case 1: return LOTSANDLAND;
            case 2: return COMMONINTEREST;
            case 3: return MULTIFAMILY;
            default:
                throw new RuntimeException("Unknown Resource Standard Name");
        }
    }

    private final int mCode;
}
