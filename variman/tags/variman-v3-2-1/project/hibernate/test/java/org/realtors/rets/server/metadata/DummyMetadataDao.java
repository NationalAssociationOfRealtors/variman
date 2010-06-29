/*
 * Variman RETS Server
 *
 * Author: Mark Klein
 * Copyright (c) 2010, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */
package org.realtors.rets.server.metadata;

import java.util.Date;

import org.realtors.rets.common.metadata.Metadata;
import org.realtors.rets.common.metadata.types.MSystem;

/**
 * An implementation of the {@link MetadataDao} interface used for testing.
 * 
 * @author Mark Klein
 */
public class DummyMetadataDao implements MetadataDao {

    private static Metadata mMetadata;
    
    static
    {
        mMetadata = new Metadata(new MSystem());
    }

    public Metadata getMetadata() 
    {
        return mMetadata;
    }

    public void saveMetadata(Metadata metadata) 
    {
        mMetadata = metadata;
    }

    public Date getChangedDate() 
    {
        return new Date();
    }
}
