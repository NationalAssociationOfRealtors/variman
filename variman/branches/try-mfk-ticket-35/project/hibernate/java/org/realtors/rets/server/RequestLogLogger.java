/* $Id: RequestLogLogger.java 55694 2009-10-21 22:38:03Z timbonicus $ */
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
