package org.realtors.rets.server.metadata;

import net.sf.hibernate.PersistentEnum;

public class AlignmentEnum implements PersistentEnum
{
    public static final AlignmentEnum LEFT = new AlignmentEnum(0);
    public static final AlignmentEnum RIGHT = new AlignmentEnum(1);
    public static final AlignmentEnum CENTER = new AlignmentEnum(2);
    public static final AlignmentEnum JUSTIFY = new AlignmentEnum(3);

    private AlignmentEnum(int code)
    {
        this.mCode = code;
    }

    public int toInt() { return mCode; }

    public static AlignmentEnum fromInt(int code)
    {
        switch (code)
        {
            case 0: return LEFT;
            case 1: return RIGHT;
            case 2: return CENTER;
            case 3: return JUSTIFY;
            default: throw new RuntimeException("Unknown Alignment");
        }
    }

    private final int mCode;
}
