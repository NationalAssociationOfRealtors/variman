/* $Id: NoOpRequestLogLogger.java 55706 2009-10-22 18:08:23Z timbonicus $ */
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
