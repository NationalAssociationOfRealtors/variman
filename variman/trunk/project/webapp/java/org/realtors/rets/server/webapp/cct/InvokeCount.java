/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp.cct;

import org.apache.commons.lang.enum.Enum;

public class InvokeCount extends Enum
{
    public static final InvokeCount ANY = new InvokeCount("any");
    public static final InvokeCount ONE = new InvokeCount("one");
    public static final InvokeCount ZERO = new InvokeCount("zero");
    public static final InvokeCount ZERO_OR_ONE = new
        InvokeCount("zero or one");
    public static final InvokeCount ZERO_OR_TWO = new
        InvokeCount("zero or two");

    public InvokeCount(String callCount)
    {
        super(callCount);
    }
}
