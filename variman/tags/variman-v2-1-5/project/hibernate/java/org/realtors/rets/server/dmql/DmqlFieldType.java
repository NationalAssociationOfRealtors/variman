package org.realtors.rets.server.dmql;

import org.apache.commons.lang.enum.Enum;

/**
 * Field types needed for DMQL.  There are less types here than supported
 * by the metadata.  This is because certain metadata types are equivalent
 * from a parsing perspective.
 */
public final class DmqlFieldType extends Enum
{
    /**
     * If the interpretation of the field is a lookup.  This basically
     * overrides the data type of the field.
     */
    public static final DmqlFieldType LOOKUP = new DmqlFieldType("lookup");

    /**
     * The data type of the field is Character or Boolean.
     */
    public static final DmqlFieldType CHARACTER =
        new DmqlFieldType("character");

    /**
     * The data type of the field is Tiny, Small, Int, Long, or Decimal.
     */
    public static final DmqlFieldType NUMERIC = new DmqlFieldType("numeric");

    /**
     * The data type of the field is Date, DateTime, or Time.
     */
    public static final DmqlFieldType TEMPORAL = new DmqlFieldType("temporal");

    private DmqlFieldType(String fieldName)
    {
        super(fieldName);
    }
}
