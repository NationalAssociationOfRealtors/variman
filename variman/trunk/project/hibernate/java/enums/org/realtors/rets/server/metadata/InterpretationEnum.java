package org.realtors.rets.server.metadata;

import net.sf.hibernate.PersistentEnum;

public class InterpretationEnum implements PersistentEnum
{
    public static final InterpretationEnum NUMBER = new InterpretationEnum(0);
    public static final InterpretationEnum CURRENCY =
        new InterpretationEnum(1);
    public static final InterpretationEnum LOOKUP = new InterpretationEnum(2);
    public static final InterpretationEnum LOOKUPMULTI =
        new InterpretationEnum(3);
    public static final InterpretationEnum LOOKUPBITSTRING =
        new InterpretationEnum(4);
    public static final InterpretationEnum LOOKUPBITMASK =
        new InterpretationEnum(5);

    private InterpretationEnum(int code)
    {
        this.mCode = code;
    }

    public int toInt() { return mCode; }

    public static InterpretationEnum fromInt(int code)
    {
        switch (code)
        {
            case 0: return NUMBER;
            case 1: return CURRENCY;
            case 2: return LOOKUP;
            case 3: return LOOKUPMULTI;
            case 4: return LOOKUPBITSTRING;
            case 5: return LOOKUPBITMASK;
            default: throw new RuntimeException("Unknown Interpretation");
        }
    }

    private final int mCode;
}
