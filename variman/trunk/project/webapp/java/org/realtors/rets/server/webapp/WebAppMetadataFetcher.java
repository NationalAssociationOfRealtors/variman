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
        return WebApp.getMetadataManager();
    }
}
