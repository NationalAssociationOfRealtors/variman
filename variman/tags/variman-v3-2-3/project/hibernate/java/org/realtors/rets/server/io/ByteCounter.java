/*
 * Variman RETS Server
 *
 * Author: RealGo
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.io;

/**
 * A byte counter counts the number of bytes and makes the count available via a
 * getter method.
 * 
 * @author Danny
 * @since 0.40.15
 */
public interface ByteCounter {

    /**
     * Returns the number of bytes counted.
     * 
     * @return The number of bytes counted.
     */
    public long getByteCount();

}
