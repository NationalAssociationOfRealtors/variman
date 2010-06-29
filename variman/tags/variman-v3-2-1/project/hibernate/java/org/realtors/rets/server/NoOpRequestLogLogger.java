/*
 * Variman RETS Server
 *
 * Author: RealGo
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server;

/**
 * Implementation of the {@link RequestLogLogger} which does nothing.
 * 
 * @author Danny
 */
public class NoOpRequestLogLogger implements RequestLogLogger {

    /*- (non-Javadoc)
     * @see org.realtors.rets.server.RequestLogLogger#logEntry(org.realtors.rets.server.RequestLogEntry)
     */
    public void logEntry(RequestLogEntry requestLogEntry) {
        // Do nothing.
    }

}
