package org.realtors.rets.server.metadata;

import java.util.Map;
import java.util.HashMap;

import net.sf.hibernate.PersistentEnum;

public class DataTypeEnum implements PersistentEnum
{
    public static final DataTypeEnum BOOLEAN = new DataTypeEnum(0);
    public static final DataTypeEnum CHARACTER = new DataTypeEnum(1);
    public static final DataTypeEnum DATE = new DataTypeEnum(2);
    public static final DataTypeEnum DATETIME = new DataTypeEnum(3);
    public static final DataTypeEnum TIME = new DataTypeEnum(4);
    public static final DataTypeEnum TINY = new DataTypeEnum(5);
    public static final DataTypeEnum SMALL = new DataTypeEnum(6);
    public static final DataTypeEnum INT = new DataTypeEnum(7);
    public static final DataTypeEnum LONG = new DataTypeEnum(8);
    public static final DataTypeEnum DECIMAL = new DataTypeEnum(9);

    private static Map mStringMap;

    static
    {
        mStringMap = new HashMap();
        mStringMap.put("boolean", BOOLEAN);
        mStringMap.put("character", CHARACTER);
        mStringMap.put("date", DATE);
        mStringMap.put("datetime", DATETIME);
        mStringMap.put("time", TIME);
        mStringMap.put("tiny", TINY);
        mStringMap.put("small", SMALL);
        mStringMap.put("int", INT);
        mStringMap.put("long", LONG);
        mStringMap.put("decimal", DECIMAL);
    }

    private DataTypeEnum(int dataType)
    {
        this.mDataType = dataType;
    }

    public int toInt() { return mDataType; }

    public static DataTypeEnum fromInt(int dataType)
    {
        switch (dataType)
        {
            case 0: return BOOLEAN;
            case 1: return CHARACTER;
            case 2: return DATE;
            case 3: return DATETIME;
            case 4: return TIME;
            case 5: return TINY;
            case 6: return SMALL;
            case 7: return INT;
            case 8: return LONG;
            case 9: return DECIMAL;
            default: throw new RuntimeException("Unknown DataType");
        }
    }

    // todo: fix this stuff
    public static DataTypeEnum fromString(String value)
    {
        return (DataTypeEnum) mStringMap.get(value.toLowerCase());
    }

    public String toString()
    {
        return toString(toInt());
    }

    public static String toString(int code)
    {
        switch (code)
        {
            case 0: return "boolean";
            case 1: return "character";
            case 2: return "date";
            case 3: return "datetime";
            case 4: return "time";
            case 5: return "tiny";
            case 6: return "small";
            case 7: return "int";
            case 8: return "long";
            case 9: return "decimal";
            default: throw new RuntimeException("Unknown DataType");
        }
    }

    public static String toString(DataTypeEnum ote)
    {
        return toString(ote.toInt());
    }

    private final int mDataType;
}