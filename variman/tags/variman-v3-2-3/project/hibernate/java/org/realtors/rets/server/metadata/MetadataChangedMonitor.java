/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2009 The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;

/**
 * Monitors whether the RETS metadata has changed. If it has, then all the
 * metadata-changed listeners are notified.
 */
public class MetadataChangedMonitor implements Runnable {

    // Configuration Variables -----------------------------------------------
    private MetadataDao metadataDao;
    private List<MetadataChangedListener> metadataChangedListenerList;

    // State Variables -------------------------------------------------------
    private Date lastCheckDate = new Date(System.currentTimeMillis() - (1000L * 10));
    private Object metadataChangedListenerListLock = new Object();

    /**
     * @return The metadata DAO to use to determine whether the metadata has
     *         changed.
     */
    public MetadataDao getMetadataDao() {
        return this.metadataDao;
    }

    /**
     * Sets metadata DAO to use to determine whether the metadata has changed.
     * 
     * @param metadataDao The metadata DAO to use to determine whether the
     *            metadata has changed.
     */
    public void setMetadataDao(MetadataDao metadataDao) {
        this.metadataDao = metadataDao;
    }

    /**
     * Adds a metadata-changed listener to the list of listeners.
     * 
     * @param metadataChangedListener The metadata-changed listener to add. Must
     *            not be {@code null}.
     */
    public void addMetadataChangedListener(MetadataChangedListener metadataChangedListener) {
        if (metadataChangedListener == null) {
            throw new NullArgumentException("metadataChangedListener");
        }
        synchronized (this.metadataChangedListenerListLock) {
            if (!this.metadataChangedListenerList.contains(metadataChangedListener)) {
                this.metadataChangedListenerList.add(metadataChangedListener);
            }
        }
    }

    /**
     * Removes a metadata-changed listener from the list of listeners.
     * 
     * @param metadataChangedListener The metadata-changed listener to remove.
     */
    public void removeMetadataChangedListener(MetadataChangedListener metadataChangedListener) {
        if (metadataChangedListener == null) {
            return;
        }
        synchronized (this.metadataChangedListenerListLock) {
            this.metadataChangedListenerList.remove(metadataChangedListener);
        }
    }

    /**
     * @return The list of metadata-changed listeners.
     */
    protected List<MetadataChangedListener> getMetadataChangedListeners() {
        synchronized (this.metadataChangedListenerListLock) {
            if (this.metadataChangedListenerList == null) {
                this.metadataChangedListenerList = new ArrayList<MetadataChangedListener>();
            }
            return this.metadataChangedListenerList;
        }
    }

    /**
     * Sets the list of metadata-changed listeners.
     * 
     * @param metadataChangedListenerList The list of metadata-changed listeners
     *            to set. Must not contain {@code null} listeners.
     */
    public void setMetadataChangedListeners(List<MetadataChangedListener> metadataChangedListenerList) {
        synchronized (this.metadataChangedListenerListLock) {
            if (metadataChangedListenerList != null) {
                // Make sure all metadata-changed listeners are not null.
                for (MetadataChangedListener metadataChangedListener : metadataChangedListenerList) {
                    if (metadataChangedListener == null) {
                        throw new IllegalArgumentException("One of the metadata-changed listeners is null.");
                    }
                }
            }
            this.metadataChangedListenerList = metadataChangedListenerList;
        }
    }

    /**
     * @return The last check date.
     */
    protected Date getLastCheckDate() {
        return this.lastCheckDate;
    }

    /**
     * Sets the last check date.
     * 
     * @param lastCheckDate The date the metadata was last checked for changes.
     */
    protected void setLastCheckDate(Date lastCheckDate) {
        this.lastCheckDate = lastCheckDate;
    }

    /*- (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        Date newLastCheckDate = new Date();
        boolean hasChanged = hasChanged();
        if (hasChanged) {
            notifyListeners();
        }
        setLastCheckDate(newLastCheckDate);
    }

    /**
     * Determines whether the metadata has changed since the last time checked.
     * 
     * @return {@code true} if the metadata has changed since the last time
     *         checked, otherwise {@code false}.
     */
    protected boolean hasChanged() {
        boolean needsUpdating = false;
        Date changedDate = getChangedDate();
        Date lastCheckDate = getLastCheckDate();
        if (changedDate.after(lastCheckDate)) {
            needsUpdating = true;
        }
        return needsUpdating;
    }

    /**
     * @return The date the metadata last changed.
     */
    protected Date getChangedDate() {
        MetadataDao metadataDao = getMetadataDao();
        Date changedDate = metadataDao.getChangedDate();
        return changedDate;
    }

    /**
     * Notifies all the metadata-changed listeners that the metadata has
     * changed.
     */
    protected void notifyListeners() {
        Object[] metadataChangedListenerArr;
        synchronized (this.metadataChangedListenerListLock) {
            metadataChangedListenerArr = getMetadataChangedListeners().toArray();
        }
        MetadataChangedListener metadataChangedListener = null;
        for (int idx = 0; idx < metadataChangedListenerArr.length; ++idx) {
            metadataChangedListener = (MetadataChangedListener)metadataChangedListenerArr[idx];
            metadataChangedListener.metadataChanged();
        }
    }

}
