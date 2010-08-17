/*
 * Variman RETS Server
 *
 * Author: Mark Klein 
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

/**
 * RequestLogLogger implementation that spits request log output into the Apache
 * Log4j configured logger.
 */
public class Log4jRequestLogLogger implements RequestLogLogger 
{
    private Logger LOG = Logger.getLogger(Log4jRequestLogLogger.class);
    private Level logLevel = Level.INFO;;

    /*- (non-Javadoc)
     * @see org.realtors.rets.server.RequestLogLogger#logEntry(org.realtors.rets.server.RequestLogEntry)
     */
    public void logEntry(RequestLogEntry requestLogEntry) 
    {
        if (logLevel == Level.DEBUG)
        {
            LOG.debug(getLogMessage(requestLogEntry));
        }
        else if (logLevel == Level.INFO)
        {
            LOG.info(getLogMessage(requestLogEntry));
        }
        else if (logLevel == Level.ERROR)
        {
            LOG.error(getLogMessage(requestLogEntry));
        }
        else if (logLevel == Level.FATAL)
        {
            LOG.fatal(getLogMessage(requestLogEntry));
        }
        else if (logLevel == Level.TRACE)
        {
            LOG.trace(getLogMessage(requestLogEntry));
        }
        else if (logLevel == Level.WARN)
        {
            LOG.warn(getLogMessage(requestLogEntry));
        }
    }
    
    private String getLogMessage(RequestLogEntry requestLogEntry) 
    {
        StringBuilder sb = new StringBuilder("Request: [");
        Iterator<String> it = requestLogEntry.keySet().iterator();
        while (it.hasNext()) 
        {
            String key = it.next();
            sb.append(key+":"+requestLogEntry.get(key));
            if (it.hasNext()) 
            {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    public void setLogLevel(String level_) 
    {
        String level = level_.toUpperCase();
        if (level.equals("ALL"))
        {
            this.logLevel = Level.ALL;
        }
        else
        if (level.equals("DEBUG"))
        {
            this.logLevel = Level.DEBUG;
        }
        else 
        if (level.equals("ERROR"))
        {
            this.logLevel = Level.ERROR;
        }
        else 
        if (level.equals("FATAL"))
        {
            this.logLevel = Level.FATAL;
        }
        else 
        if (level.equals("INFO"))
        {
            this.logLevel = Level.INFO;
        }
        else
        if (level.equals("OFF"))
        {
            this.logLevel = Level.OFF;
        }
        else 
        if (level.equals("TRACE"))
        {
            this.logLevel = Level.TRACE;
        }
        else 
        if (level.equals("WARN"))
        {
            this.logLevel = Level.WARN;
        }
    }
    
    public String getLogLevel() 
    {
        if (this.logLevel ==  Level.ALL)     return "ALL";
        if (this.logLevel ==  Level.DEBUG)   return "DEBUG";
        if (this.logLevel ==  Level.ERROR)   return "ERROR";
        if (this.logLevel ==  Level.FATAL)   return "FATAL";
        if (this.logLevel ==  Level.INFO)    return "INFO";
        if (this.logLevel ==  Level.OFF)     return "OFF";
        if (this.logLevel ==  Level.TRACE)   return "TRACE";
        if (this.logLevel ==  Level.WARN)    return "WARN";
        return "";
    }
}
