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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.realtors.rets.server.io.ByteCounter;
import org.realtors.rets.server.io.CountingPrintWriter;

/**
 * An {@link HttpServletResponse} implementation which wraps another {@code
 * HttpServletResponse} in order to count the number of bytes sent in the
 * response.
 * <p>
 * Although an {@link HttpServletResponse} is a {@link ServletResponse} a
 * {@link CountingHttpServletResponse} is NOT a {@link CountingServletResponse}.
 * This was done because a lot of software depends on the type of servlet
 * response. Some require that it is an HTTP servlet response.
 * 
 * @author Danny
 */
public class CountingHttpServletResponse extends HttpServletResponseWrapper implements ByteCounter {

    // State Variables -------------------------------------------------------
    private CountingServletOutputStream countingServletOutputStream;
    private CountingPrintWriter countingPrintWriter;

    /**
     * Constructs a new CountingHttpServletResponse.
     * 
     * @param response The servlet response on which to count response bytes.
     *            Must not be {@code null}.
     */
    public CountingHttpServletResponse(HttpServletResponse response) {
        super(response);
    }

    /*- (non-Javadoc)
     * @see javax.servlet.HttpServletResponseWrapper#getOutputStream()
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
     * @see javax.servlet.HttpServletResponseWrapper#getWriter()
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
