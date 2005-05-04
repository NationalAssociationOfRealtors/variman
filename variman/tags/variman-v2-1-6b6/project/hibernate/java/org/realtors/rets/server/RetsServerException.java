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

import org.apache.commons.lang.exception.NestableException;

public class RetsServerException extends NestableException
{
    public RetsServerException()
    {
        super();
    }

    public RetsServerException(String msg)
    {
        super(msg);
    }

    public RetsServerException(String msg, Throwable cause)
    {
        super(msg, cause);
    }

    public RetsServerException(Throwable cause)
    {
        super(cause);
    }
}
