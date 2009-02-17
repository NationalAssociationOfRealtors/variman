/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server;

import org.apache.commons.lang.enums.ValuedEnum;

public class ReplyCode extends ValuedEnum
{
    public static final ReplyCode SUCCESSFUL =
        new ReplyCode("Operation Successful", 0);

    public static final ReplyCode INVALID_QUERY_FIELD =
    	new ReplyCode("Invalid Query Field", 20200);
    public static final ReplyCode NO_RECORDS_FOUND =
        new ReplyCode("No Records Found", 20201);
    public static final ReplyCode INVALID_SELECT =
        new ReplyCode("Invalid Select", 20202);
    public static final ReplyCode MISC_SEARCH_ERROR =
        new ReplyCode("Miscellaneous Search Error", 20203);
    public static final ReplyCode INVALID_QUERY_SYNTAX =
        new ReplyCode("Invalid Query Syntax", 20206);

    public static final ReplyCode NO_OBJECT_FOUND =
        new ReplyCode("No Object Found", 20403);

    public static final ReplyCode INVALID_TYPE =
        new ReplyCode("Invalid Type", 20501);
    public static final ReplyCode INVALID_IDENTIFIER =
        new ReplyCode("Invalid Identifier", 20502);
    public static final ReplyCode DTD_UNAVAILABLE =
        new ReplyCode("Requested DTD version unavailable", 20514);
    public static final ReplyCode MISC_ERROR =
        new ReplyCode("Miscellaneous Error", 20513);

    private ReplyCode(String name, int value)
    {
        super(name, value);
    }
}
