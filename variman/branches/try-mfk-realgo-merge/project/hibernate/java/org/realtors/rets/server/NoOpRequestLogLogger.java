/* $Id: NoOpRequestLogLogger.java 47818 2009-05-22 22:53:58Z danny $ */
/* Copyright: Copyright (c) 2009 RealGo Inc. All rights reserved. */
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
