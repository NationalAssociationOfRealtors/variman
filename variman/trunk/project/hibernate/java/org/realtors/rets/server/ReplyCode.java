/*
 */
package org.realtors.rets.server;

import org.apache.commons.lang.enum.ValuedEnum;

public class ReplyCode extends ValuedEnum
{
    public static final ReplyCode NO_RECORDS_FOUND =
        new ReplyCode("No Records Found", 20201);
    public static final ReplyCode INVALID_QUERY_SYNTAX =
        new ReplyCode("Invalid Query Syntax", 20206);

    private ReplyCode(String name, int value)
    {
        super(name, value);
    }
}
