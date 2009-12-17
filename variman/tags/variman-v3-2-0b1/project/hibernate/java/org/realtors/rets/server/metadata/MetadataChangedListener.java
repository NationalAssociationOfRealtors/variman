/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2009 The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import java.util.EventListener;

/**
 * An event listener which can listen when metadata changes.
 */
public interface MetadataChangedListener extends EventListener {

    /**
     * Called when the metadata has changed.
     */
    public void metadataChanged();

}
