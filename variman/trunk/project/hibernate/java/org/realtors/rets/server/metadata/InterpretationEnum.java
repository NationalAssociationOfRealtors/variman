package org.realtors.rets.server.metadata;

import java.util.Map;
import java.util.HashMap;

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

    private static Map mStringMap;

    static
    {
        mStringMap = new HashMap();
        mStringMap.put("number", NUMBER);
        mStringMap.put("currency", CURRENCY);
        mStringMap.put("lookup", LOOKUP);
        mStringMap.put("lookupmulti", LOOKUPMULTI);
        mStringMap.put("lookupbitstring", LOOKUPBITSTRING);
        mStringMap.put("lookupbitmask", LOOKUPBITMASK);
    }

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

    public static InterpretationEnum fromString(String value)
    {
        return (InterpretationEnum) mStringMap.get(value.toLowerCase());
    }

    public String toString()
    {
        return toString(toInt());
    }

    public static String toString(int code)
    {
        switch (code)
        {
            case 0: return "number";
            case 1: return "currency";
            case 2: return "lookup";
            case 3: return "lookupmulti";
            case 4: return "lookupbitstring";
            case 5: return "lookupbitmask";
            default: throw new RuntimeException("Unknown Interpretation");
        }
    }

    public static String toString(InterpretationEnum ie)
    {
        return toString(ie.toInt());
    }

    private final int mCode;
}