package org.realtors.rets.server.metadata;

import net.sf.hibernate.PersistentEnum;

public class UnitEnum implements PersistentEnum
{
    public static final UnitEnum FEET = new UnitEnum(0);
    public static final UnitEnum METERS = new UnitEnum(1);
    public static final UnitEnum SQFT = new UnitEnum(2);
    public static final UnitEnum SQMETERS = new UnitEnum(3);
    public static final UnitEnum ACRES = new UnitEnum(4);
    public static final UnitEnum HECTARES = new UnitEnum(5);

    private UnitEnum(int code)
    {
        this.mCode = code;
    }

    public int toInt() { return mCode; }

    public static UnitEnum fromInt(int code)
    {
        switch (code)
        {
            case 0: return FEET;
            case 1: return METERS;
            case 2: return SQFT;
            case 3: return SQMETERS;
            case 4: return ACRES;
            case 5: return HECTARES;
            default: throw new RuntimeException("Unknown Unit");
        }
    }

    private final int mCode;
}
