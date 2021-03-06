/*
 */
package org.realtors.rets.server.webapp.cct;

import java.io.IOException;

import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;

public class LoggingServletOutputStream extends ServletOutputStream
{
    public LoggingServletOutputStream(ServletOutputStream outputStream,
                                     Logger log)
    {
        mOutputStream = outputStream;
        mLog = log;
    }

    public void write(int b) throws IOException
    {
        String string = new String(new byte[] {(byte) b});
        mLog.debug(string);
        mOutputStream.write(b);
    }

    private ServletOutputStream mOutputStream;
    private Logger mLog;
}
