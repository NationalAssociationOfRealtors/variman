/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;
import org.apache.log4j.Logger;
/**
 * A Runnable monitor that periodically checks if the RetsConfig for the server
 * has changed and fires an event to registered RetsConfigListeners.
 * 
 * @author timbo
 */
public class RetsConfigChangedMonitor implements Runnable {
    private static final Logger LOG = Logger.getLogger(RetsConfigChangedMonitor.class);
    private RetsConfigDao configDao;

    private List<RetsConfigChangedListener> retsConfigChangedListenerList = new ArrayList<RetsConfigChangedListener>();
    private Date lastCheckDate;
    private Object retsConfigChangedListenerListLock = new Object();

    public void addRetsConfigChangedListener(RetsConfigChangedListener retsConfigChangedListener) {
        if (retsConfigChangedListener == null) {
            throw new NullArgumentException("retsConfigChangedListener");
        }
        synchronized (this.retsConfigChangedListenerListLock) {
            if (!this.retsConfigChangedListenerList.contains(retsConfigChangedListener)) {
                this.retsConfigChangedListenerList.add(retsConfigChangedListener);
            }
        }
    }
    
    public void removeRetsConfigChangedListener(RetsConfigChangedListener retsConfigChangedListener) {
        if (retsConfigChangedListener == null) {
            return;
        }
        synchronized (this.retsConfigChangedListenerListLock) {
            this.retsConfigChangedListenerList.remove(retsConfigChangedListener);
        }
    }
    
    public void setRetsConfigChangedListeners(List<RetsConfigChangedListener> retsConfigChangedListenerList) {
        synchronized (this.retsConfigChangedListenerListLock) {
            if (retsConfigChangedListenerList != null) {
                // Make sure all RETS config-changed listeners are not null.
                for (RetsConfigChangedListener retsConfigChangedListener : retsConfigChangedListenerList) {
                    if (retsConfigChangedListener == null) {
                        throw new IllegalArgumentException("One of the RETS config-changed listeners is null.");
                    }
                }
            }
            this.retsConfigChangedListenerList = retsConfigChangedListenerList;
        }
    }
    
    public List getRetsConfigChangedListeners() {
        synchronized (this.retsConfigChangedListenerListLock) {
            return this.retsConfigChangedListenerList;
        }
    }
    
    protected void fireRetsConfigChangedEvent() {
        // Snag a copy of the current list so we can release the lock sooner
        RetsConfigChangedListener[] listeners = new RetsConfigChangedListener[1];
        synchronized (this.retsConfigChangedListenerListLock) {
            listeners = this.retsConfigChangedListenerList.toArray(listeners);
        }
        if (listeners == null) {
            return;
        }
        
        for (int i = 0; i < listeners.length; i++) {
                listeners[i].retsConfigChanged();
        }
    }
    
    public void setRetsConfigDao(RetsConfigDao dao) {
        this.configDao = dao;
    }
    
    public RetsConfigDao getRetsConfigDao() {
        return this.configDao;
    }
    
    /*- (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        Date configChanged = this.configDao.getConfigChangedDate();
        
        // Simply grab the date on first load for later comparison
        if (this.lastCheckDate == null) {
            this.lastCheckDate = configChanged;
        }
        else if (configChanged.after(this.lastCheckDate)) {
            this.lastCheckDate = configChanged;
            fireRetsConfigChangedEvent();
        }
    }
}
