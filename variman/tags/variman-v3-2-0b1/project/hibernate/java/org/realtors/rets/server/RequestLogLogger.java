/*
 * Variman RETS Server
 *
 * Author: RealGo
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server;


/**
 * A repository of {@link RequestLogEntry}s.
 * 
 * @author Danny
 */
public interface RequestLogLogger {

    /**
     * Logs a new request log entry to this repository.
     * 
     * @param requestLogEntry The request log entry to be added. Must not be
     *         {@code null}.
     */
    public void logEntry(RequestLogEntry requestLogEntry);

}
