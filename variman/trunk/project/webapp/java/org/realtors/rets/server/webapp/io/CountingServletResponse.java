/*
 * Variman RETS Server
 *
 * Author: RealGo
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.webapp.io;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;

import org.realtors.rets.server.io.ByteCounter;
import org.realtors.rets.server.io.CountingPrintWriter;

/**
 * An {@link ServletResponse} implementation which wraps another {@code
 * ServletResponse} in order to count the number of bytes sent in the response.
 * <p>
 * This should not be used to wrap {@link HttpServletResponse}s. Use a
 * {@link CountingHttpServletResponse} for that. If an HTTP servlet response
 * were wrapped by this class then it would not appear to be an HTTP servlet
 * response but a plain-old servlet response.
 * 
 * @author Danny
 * @see CountingHttpServletResponse
 * @since 0.40.15
 */
public class CountingServletResponse extends ServletResponseWrapper implements ByteCounter {

    // State Variables -------------------------------------------------------
    private CountingServletOutputStream countingServletOutputStream;
    private CountingPrintWriter countingPrintWriter;

    /**
     * Constructs a new CountingServletResponse.
     * 
     * @param response The servlet response on which to count response bytes.
     *            Must not be {@code null}. Should not be a
     *            {@link HttpServletResponse}. Use the
     *            {@link CountingHttpServletResponse} instead.
     * @see CountingHttpServletResponse
     */
    public CountingServletResponse(ServletResponse response) {
        super(response);
    }

    /*- (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#getOutputStream()
     */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (this.countingServletOutputStream == null) {
            ServletOutputStream servletOutputStream = super.getOutputStream();
            this.countingServletOutputStream = new CountingServletOutputStream(servletOutputStream);
        }
        return this.countingServletOutputStream;
    }

    /*- (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#getWriter()
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        if (this.countingPrintWriter == null) {
            PrintWriter printWriter = super.getWriter();
            this.countingPrintWriter = new CountingPrintWriter(printWriter, false);
        }
        return this.countingPrintWriter;
    }

    /**
     * Returns the number of bytes written to either the servlet output stream
     * or the print writer.
     * 
     * @return The number of bytes written to the response.
     */
    public long getByteCount() {
        long count = 0L;
        if (this.countingServletOutputStream != null) {
            count = this.countingServletOutputStream.getByteCount();
        } else if (this.countingPrintWriter != null) {
            count = this.countingPrintWriter.getByteCount();
        }
        return count;
    }

}
