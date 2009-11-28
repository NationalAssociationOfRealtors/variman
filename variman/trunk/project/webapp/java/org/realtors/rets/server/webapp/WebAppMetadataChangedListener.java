/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2009 The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.webapp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.metadata.MetadataChangedListener;

/**
 * Loads the metadata into memory when it has changed.
 */
public class WebAppMetadataChangedListener implements MetadataChangedListener {

    private static final Log LOGGER = LogFactory.getLog(WebAppMetadataChangedListener.class);

    /*- (non-Javadoc)
     * @see org.realtors.rets.server.metadata.MetadataChangedListener#metadataChanged()
     */
    public void metadataChanged() {
        try {
            WebApp.loadConfiguration();
        } catch (RetsServerException e) {
            if (LOGGER.isFatalEnabled()) {
                LOGGER.fatal("An error occurred while loading the metadata.", e);
            }
        }
    }

}
