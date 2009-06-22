/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp;

import org.realtors.rets.server.ManagerMetadataFetcher;
import org.realtors.rets.server.metadata.MetadataManager;

/**
 * Gets the metadata manager from the WebApp.
 */
public class WebAppMetadataFetcher extends ManagerMetadataFetcher
{
    protected MetadataManager getMetadataManager()
    {
        /*
         * Be sure a reference to the MetadataManager is not stored here in
         * case the web-app reloads. We want to be sure we get the new
         * MetadataManager if it changes.
         */
        return WebApp.getMetadataManager();
    }
    
    /**
     * @overrides surrogate to Java5 annotations.
     */
    public void setMetadataManager(MetadataManager metadataManager)
    {
        // Do nothing with the metadata manager since we always want the one
        // from WebApp.
    }
    
}
