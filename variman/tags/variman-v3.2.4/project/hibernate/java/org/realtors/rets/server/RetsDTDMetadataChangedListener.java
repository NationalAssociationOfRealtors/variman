/*
 * Variman RETS Server
 *
 * Author: Mark Klein
 * Copyright (c) 2010 The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server;

import org.apache.log4j.Logger;
import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.metadata.MetadataChangedListener;

/**
 * Sync the DTD to metadata tables when the metadata has changed
 */
public class RetsDTDMetadataChangedListener implements MetadataChangedListener 
{
    private static final Logger LOG = Logger.getLogger(RetsDTDMetadataChangedListener.class);

    public void metadataChanged() 
    {
        try 
        {
            RetsDTD.metadataChanged();
        } 
        catch (Exception e) 
        {
            LOG.fatal("An error occurred while loading and synchronizing the metadata to the DTD.", e);
        }
    }

}
