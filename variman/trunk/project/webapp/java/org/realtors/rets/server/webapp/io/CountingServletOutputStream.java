/*
 * Variman RETS Server
 *
 * Author: RealGo
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.webapp.io;

import java.io.IOException;

import javax.servlet.ServletOutputStream;

import org.realtors.rets.server.io.ByteCounter;

/**
 * Counts the number of bytes written or printed to the underlying
 * {@link ServletOutputStream}.
 * 
 * @author Danny
 * @since 0.40.15
 */
public class CountingServletOutputStream extends ServletOutputStream implements ByteCounter {

    // Configuration Variables -----------------------------------------------
    private final ServletOutputStream servletOutputStream;

    // State Variables -------------------------------------------------------
    /** The count of bytes that have passed. */
    private long count;

    /**
     * Constructs a new CountingServletOutputStream.
     * 
     * @param servletOutputStream The servlet output stream to count the bytes
     *            from. Must not be {@code null}.
     */
    public CountingServletOutputStream(ServletOutputStream servletOutputStream) {
        super();
        this.servletOutputStream = servletOutputStream;
    }

    /*- (non-Javadoc)
     * @see java.io.OutputStream#write(int)
     */
    @Override
    public void write(int b) throws IOException {
        this.count += 1;
        this.servletOutputStream.write(b);
    }

    /*- (non-Javadoc)
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.count += len;
        super.write(b, off, len);
    }

    /*- (non-Javadoc)
     * @see java.io.OutputStream#write(byte[])
     */
    @Override
    public void write(byte[] b) throws IOException {
        this.count += b.length;
        super.write(b);
    }

    /*- (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#print(boolean)
     */
    @Override
    public void print(boolean b) throws IOException {
        String str = Boolean.toString(b);
        this.count += str.length();
        super.print(b);
    }

    /*- (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#print(char)
     */
    @Override
    public void print(char c) throws IOException {
        this.count += 1;
        super.print(c);
    }

    /*- (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#print(double)
     */
    @Override
    public void print(double d) throws IOException {
        String str = Double.toString(d);
        this.count += str.length();
        super.print(d);
    }

    /*- (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#print(float)
     */
    @Override
    public void print(float f) throws IOException {
        String str = Float.toString(f);
        this.count += str.length();
        super.print(f);
    }

    /*- (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#print(int)
     */
    @Override
    public void print(int i) throws IOException {
        String str = Integer.toString(i);
        this.count += str.length();
        super.print(i);
    }

    /*- (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#print(long)
     */
    @Override
    public void print(long l) throws IOException {
        String str = Long.toString(l);
        this.count += str.length();
        super.print(l);
    }

    /*- (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#print(java.lang.String)
     */
    @Override
    public void print(String str) throws IOException {
        if (str != null) {
            this.count += str.length();
        } else {
            // 4 characters in null.
            this.count += 4;
        }
        super.print(str);
    }

    /*- (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#println()
     */
    @Override
    public void println() throws IOException {
        this.count += 2;
        super.println();
    }

    /*- (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#println(boolean)
     */
    @Override
    public void println(boolean b) throws IOException {
        String str = Boolean.toString(b);
        this.count += str.length() + 2;
        super.println(b);
    }

    /*- (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#println(char)
     */
    @Override
    public void println(char c) throws IOException {
        this.count += 3;
        super.println(c);
    }

    /*- (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#println(double)
     */
    @Override
    public void println(double d) throws IOException {
        String str = Double.toString(d);
        this.count += str.length() + 2;
        super.println(d);
    }

    /*- (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#println(float)
     */
    @Override
    public void println(float f) throws IOException {
        String str = Float.toString(f);
        this.count += str.length() + 2;
        super.println(f);
    }

    /*- (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#println(int)
     */
    @Override
    public void println(int i) throws IOException {
        String str = Integer.toString(i);
        this.count += str.length() + 2;
        super.println(i);
    }

    /*- (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#println(long)
     */
    @Override
    public void println(long l) throws IOException {
        String str = Long.toString(l);
        this.count += str.length() + 2;
        super.println(l);
    }

    /*- (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#println(java.lang.String)
     */
    @Override
    public void println(String str) throws IOException {
        if (str != null) {
            this.count += str.length() + 2;
        } else {
            // 4 characters in null.
            this.count += (4 + 2);
        }
        super.println(str);
    }

    /*- (non-Javadoc)
     * @see java.io.OutputStream#close()
     */
    @Override
    public void close() throws IOException {
        super.close();
    }

    /*- (non-Javadoc)
     * @see java.io.OutputStream#flush()
     */
    @Override
    public void flush() throws IOException {
        super.flush();
    }

    /**
     * Returns the byte count.
     * 
     * @return The byte count. Never negative.
     */
    public long getByteCount() {
        return this.count;
    }

}
