/*
 * Variman RETS Server
 *
 * Author: RealGo
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.io;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;

/**
 * A {@link PrintWriter} decorator which counts the number of bytes written.
 * 
 * @author Danny
 * @since 0.40.15
 */
public class CountingPrintWriter extends PrintWriter implements ByteCounter {

    // Configuration Variables -----------------------------------------------
    private final CountingWriter countingWriter;

    /**
     * Constructs a new CountingPrintWriter.
     * 
     * @param writer The writer this counting print writer will write to and
     *            count the number of bytes written. Must not be {@code null}.
     */
    public CountingPrintWriter(Writer writer) {
        this(writer, false);
    }

    /**
     * Constructs a new CountingPrintWriter.
     * 
     * @param writer The writer this counting print writer will write to and
     *            count the number of bytes written. Must not be {@code null}.
     * @param autoFlush Flag indicating whether the {@link #println},
     *            {@link #printf}, or {@link #format} methods will flush the
     *            output buffer.
     */
    public CountingPrintWriter(Writer writer, boolean autoFlush) {
        super(new CountingWriter(writer), autoFlush);
        this.countingWriter = (CountingWriter)this.out;
    }

    /**
     * Returns the byte count.
     * 
     * @return The byte count.
     */
    public long getByteCount() {
        long byteCount = this.countingWriter.getByteCount();
        return byteCount;
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#append(char)
     */
    @Override
    public PrintWriter append(char c) {
        return super.append(c);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#append(java.lang.CharSequence, int, int)
     */
    @Override
    public PrintWriter append(CharSequence csq, int start, int end) {
        return super.append(csq, start, end);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#append(java.lang.CharSequence)
     */
    @Override
    public PrintWriter append(CharSequence csq) {
        return super.append(csq);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#checkError()
     */
    @Override
    public boolean checkError() {
        return super.checkError();
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#close()
     */
    @Override
    public void close() {
        super.close();
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#flush()
     */
    @Override
    public void flush() {
        super.flush();
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#format(java.util.Locale, java.lang.String, java.lang.Object[])
     */
    @Override
    public PrintWriter format(Locale l, String format, Object... args) {
        return super.format(l, format, args);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#format(java.lang.String, java.lang.Object[])
     */
    @Override
    public PrintWriter format(String format, Object... args) {
        return super.format(format, args);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#print(boolean)
     */
    @Override
    public void print(boolean b) {
        super.print(b);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#print(char)
     */
    @Override
    public void print(char c) {
        super.print(c);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#print(char[])
     */
    @Override
    public void print(char[] s) {
        super.print(s);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#print(double)
     */
    @Override
    public void print(double d) {
        super.print(d);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#print(float)
     */
    @Override
    public void print(float f) {
        super.print(f);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#print(int)
     */
    @Override
    public void print(int i) {
        super.print(i);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#print(long)
     */
    @Override
    public void print(long l) {
        super.print(l);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#print(java.lang.Object)
     */
    @Override
    public void print(Object obj) {
        super.print(obj);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#print(java.lang.String)
     */
    @Override
    public void print(String s) {
        super.print(s);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#printf(java.util.Locale, java.lang.String, java.lang.Object[])
     */
    @Override
    public PrintWriter printf(Locale l, String format, Object... args) {
        return super.printf(l, format, args);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#printf(java.lang.String, java.lang.Object[])
     */
    @Override
    public PrintWriter printf(String format, Object... args) {
        return super.printf(format, args);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#println()
     */
    @Override
    public void println() {
        super.println();
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#println(boolean)
     */
    @Override
    public void println(boolean x) {
        super.println(x);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#println(char)
     */
    @Override
    public void println(char x) {
        super.println(x);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#println(char[])
     */
    @Override
    public void println(char[] x) {
        super.println(x);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#println(double)
     */
    @Override
    public void println(double x) {
        super.println(x);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#println(float)
     */
    @Override
    public void println(float x) {
        super.println(x);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#println(int)
     */
    @Override
    public void println(int x) {
        super.println(x);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#println(long)
     */
    @Override
    public void println(long x) {
        super.println(x);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#println(java.lang.Object)
     */
    @Override
    public void println(Object x) {
        super.println(x);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#println(java.lang.String)
     */
    @Override
    public void println(String x) {
        super.println(x);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#setError()
     */
    @Override
    protected void setError() {
        super.setError();
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#write(char[], int, int)
     */
    @Override
    public void write(char[] buf, int off, int len) {
        super.write(buf, off, len);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#write(char[])
     */
    @Override
    public void write(char[] buf) {
        super.write(buf);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#write(int)
     */
    @Override
    public void write(int c) {
        super.write(c);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#write(java.lang.String, int, int)
     */
    @Override
    public void write(String s, int off, int len) {
        super.write(s, off, len);
    }

    /*- (non-Javadoc)
     * @see java.io.PrintWriter#write(java.lang.String)
     */
    @Override
    public void write(String s) {
        super.write(s);
    }

}
