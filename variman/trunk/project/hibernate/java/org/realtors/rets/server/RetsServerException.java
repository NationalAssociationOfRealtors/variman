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
