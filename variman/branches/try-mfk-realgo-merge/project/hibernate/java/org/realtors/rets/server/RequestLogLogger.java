/* $Id: RequestLogLogger.java 47818 2009-05-22 22:53:58Z danny $ */
/* Copyright: Copyright (c) 2009 RealGo Inc. All rights reserved. */
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
