/*
 */
package org.realtors.rets.server.dmql;

import org.apache.commons.lang.enum.Enum;

public final class LookupListType extends Enum
{
    public static final LookupListType AND = new LookupListType("AND");
    public static final LookupListType OR = new LookupListType("OR");
    public static final LookupListType NOT = new LookupListType("NOT");

    private LookupListType(String type)
    {
        super(type);
    }
}
