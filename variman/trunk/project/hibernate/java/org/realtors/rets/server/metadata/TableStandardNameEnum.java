package org.realtors.rets.server.metadata;

import java.util.Map;

import net.sf.hibernate.PersistentEnum;

public class TableStandardNameEnum implements PersistentEnum
{

    private static Map mStringMap;

    static
    {
        // todo: init map
    }
    
    private TableStandardNameEnum(int code)
    {
        this.mCode = code;
    }

    public int toInt() { return mCode; }

    public static TableStandardNameEnum fromInt(int code)
    {
        return null;
    }

    public static TableStandardNameEnum fromString(String value)
    {
        return null;
    }

    public String toString()
    {
        return toString(toInt());
    }

    public static String toString(int code)
    {
        return null;
    }

    public static String toString(TableStandardNameEnum tsne)
    {
        return toString(tsne.toInt());
    }

    private final int mCode;
}
