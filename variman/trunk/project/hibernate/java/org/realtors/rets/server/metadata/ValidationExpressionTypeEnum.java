package org.realtors.rets.server.metadata;

import net.sf.hibernate.PersistentEnum;

public class ValidationExpressionTypeEnum implements PersistentEnum
{
    public static final ValidationExpressionTypeEnum ACCEPT =
        new ValidationExpressionTypeEnum(0);
    public static final ValidationExpressionTypeEnum REJECT =
        new ValidationExpressionTypeEnum(1);
    public static final ValidationExpressionTypeEnum SET =
        new ValidationExpressionTypeEnum(2);

    private ValidationExpressionTypeEnum(int code)
    {
        this.mCode = code;
    }

    public int toInt() { return mCode; }

    public static ValidationExpressionTypeEnum fromInt(int code)
    {
        switch (code)
        {
            case 0: return ACCEPT;
            case 1: return REJECT;
            case 2: return SET;
            default:
                throw new IllegalArgumentException(
                    "Unknown Validation Expression Type: " + code);
        }
    }

    public String toString()
    {
        return toString(mCode);
    }

    public static String toString(int code)
    {
        if (code == ACCEPT.mCode)
        {
            return "ACCEPT";
        }
        else if (code == REJECT.mCode)
        {
            return "REJECT";
        }
        else if (code == SET.mCode)
        {
            return "SET";
        }
        else
        {
            throw new IllegalArgumentException(
                "Unknown Validation Expression Type: " + code);
        }
    }

    private final int mCode;
}
