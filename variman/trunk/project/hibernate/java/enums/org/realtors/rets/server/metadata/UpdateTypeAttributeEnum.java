package org.nar.rets.metadata.newdb;

import net.sf.hibernate.PersistentEnum;

public class UpdateTypeAttributeEnum implements PersistentEnum
{
    public static final UpdateTypeAttributeEnum DISPLAYONLY =
        new UpdateTypeAttributeEnum(1);
    public static final UpdateTypeAttributeEnum REQUIRED =
        new UpdateTypeAttributeEnum(2);
    public static final UpdateTypeAttributeEnum AUTOPOP =
        new UpdateTypeAttributeEnum(3);
    public static final UpdateTypeAttributeEnum INTERACTIVEVALIDATE =
        new UpdateTypeAttributeEnum(4);
    public static final UpdateTypeAttributeEnum CLEARONCLONING =
        new UpdateTypeAttributeEnum(5);

    private UpdateTypeAttributeEnum(int code)
    {
        this.mCode = code;
    }

    public int toInt() { return mCode; }

    public static UpdateTypeAttributeEnum fromInt(int code)
    {
        switch (code)
        {
            case 1: return DISPLAYONLY;
            case 2: return REQUIRED;
            case 3: return AUTOPOP;
            case 4: return INTERACTIVEVALIDATE;
            case 5: return CLEARONCLONING;
            default:
                throw new RuntimeException("Unkonwn Update Type Attribute");
        }
    }

    private final int mCode;
}
                
