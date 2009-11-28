/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2009 The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import java.util.Date;

import org.realtors.rets.common.metadata.Metadata;

/**
 * The data access interface for {@link Metadata}.
 */
public interface MetadataDao {

    /**
     * @return The RETS metadata.
     */
    public Metadata getMetadata();

    /**
     * @param metadata The RETS metadata to save.
     */
    public void saveMetadata(Metadata metadata);

    /**
     * @return The datetime the metadata was last changed.
     */
    public Date getChangedDate();

}
