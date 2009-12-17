/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2009 The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.webapp;

import org.apache.log4j.Logger;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.metadata.MetadataChangedListener;

/**
 * Loads the metadata into memory when it has changed.
 */
public class WebAppMetadataChangedListener implements MetadataChangedListener {

    private static final Logger LOG = Logger.getLogger(WebAppMetadataChangedListener.class);

    /*- (non-Javadoc)
     * @see org.realtors.rets.server.metadata.MetadataChangedListener#metadataChanged()
     */
    public void metadataChanged() {
        try {
            WebApp.loadConfiguration();
        } catch (RetsServerException e) {
            LOG.fatal("An error occurred while loading the metadata.", e);
        }
    }

}
