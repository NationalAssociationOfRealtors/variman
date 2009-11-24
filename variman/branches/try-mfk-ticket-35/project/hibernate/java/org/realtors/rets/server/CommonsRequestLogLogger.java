/* $Id: CommonsRequestLogLogger.java 55706 2009-10-22 18:08:23Z timbonicus $ */
package org.realtors.rets.server;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * RequestLogLogger implementation that spits request log output into the Apache
 * Commons configured logger.
 * 
 * @author timbo
 */
public class CommonsRequestLogLogger implements RequestLogLogger {
    private String logClassName = CommonsRequestLogLogger.class.getSimpleName();
    private Log LOG = LogFactory.getLog(logClassName);
    private String logLevel = "INFO";

    /*- (non-Javadoc)
     * @see org.realtors.rets.server.RequestLogLogger#logEntry(org.realtors.rets.server.RequestLogEntry)
     */
    public void logEntry(RequestLogEntry requestLogEntry) {
        String level = logLevel.toUpperCase();
        if (level.equals("DEBUG") && LOG.isDebugEnabled()) {
            LOG.debug(getLogMessage(requestLogEntry));
        }
        else if (level.equals("INFO") && LOG.isInfoEnabled()) {
            LOG.info(getLogMessage(requestLogEntry));
        }
        else if (level.equals("ERROR") && LOG.isErrorEnabled()) {
            LOG.error(getLogMessage(requestLogEntry));
        }
        else if (level.equals("FATAL") && LOG.isFatalEnabled()) {
            LOG.fatal(getLogMessage(requestLogEntry));
        }
        else if (level.equals("TRACE") && LOG.isTraceEnabled()) {
            LOG.trace(getLogMessage(requestLogEntry));
        }
        else if (level.equals("WARN") && LOG.isWarnEnabled()) {
            LOG.warn(getLogMessage(requestLogEntry));
        }
    }
    
    // Construct JSON-like log output
    private String getLogMessage(RequestLogEntry requestLogEntry) {
        StringBuilder sb = new StringBuilder("Request: [");
        Iterator<String> it = requestLogEntry.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            sb.append(key+":"+requestLogEntry.get(key));
            if (it.hasNext()) {
                sb.append(",");
            }
            sb.append(System.getProperty("line.separator"));
        }
        sb.append(']');
        return sb.toString();
    }

    public void setLogLevel(String level) {
        this.logLevel = level;
    }
    
    public String getLogLevel() {
        return this.logLevel;
    }
    
    public void setLogClassName(String className) {
        this.logClassName = className;
    }
    
    public String getLogClassName() {
        return this.logClassName;
    }
}
