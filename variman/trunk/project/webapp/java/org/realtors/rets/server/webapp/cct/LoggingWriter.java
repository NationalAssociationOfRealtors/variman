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

import java.io.PrintWriter;
import java.io.FilterWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

public class LoggingWriter extends FilterWriter
{
    public LoggingWriter(PrintWriter writer, Logger log)
    {
        super(writer);
        mLog = log;
    }

    public void write(char cbuf[], int off, int len) throws IOException
    {
        mLog.debug(new String(cbuf, off, len));
        super.write(cbuf, off, len);
    }

    public void write(String str, int off, int len) throws IOException
    {
        mLog.debug(str.substring(off, off + len));
        super.write(str, off, len);
    }

    private Logger mLog;
}
