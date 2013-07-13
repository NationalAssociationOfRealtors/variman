/*
 * Variman RETS Server
 *
 * Author: RealGo
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.io;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.NullArgumentException;

/**
 * A {@link Writer} decorator which counts the number of bytes written.
 * 
 * @author Danny
 * @since 0.40.15
 */
public class CountingWriter extends Writer implements ByteCounter {

    // Configuration Variables -----------------------------------------------
    private final Writer writer;

    // State Variables -------------------------------------------------------
    /** The count of bytes that have passed. */
    private long byteCount;

    /**
     * Constructs a new CountingWriter.
     * 
     * @param writer The writer to count written bytes from. Must not be {@code
     *            null}.
     */
    public CountingWriter(Writer writer) {
        super();
        if (writer == null) {
            throw new NullArgumentException("writer");
        }
        this.writer = writer;
    }

    /**
     * Returns the byte count.
     * 
     * @return The byte count. Never negative.
     */
    public long getByteCount() {
        return this.byteCount;
    }

    /*- (non-Javadoc)
     * @see java.io.Writer#close()
     */
    @Override
    public void close() throws IOException {
        this.writer.close();
    }

    /*- (non-Javadoc)
     * @see java.io.Writer#flush()
     */
    @Override
    public void flush() throws IOException {
        this.writer.flush();
    }

    /*- (non-Javadoc)
     * @see java.io.Writer#write(char[], int, int)
     */
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        this.byteCount += len;
        this.writer.write(cbuf, off, len);
    }

}
