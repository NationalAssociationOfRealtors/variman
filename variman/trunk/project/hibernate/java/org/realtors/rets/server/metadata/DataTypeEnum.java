package org.realtors.rets.server.metadata;

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

    private final int mDataType;
}
