/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2006, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

import org.realtors.rets.server.RetsServerException;

/**
 * A <code>MetadataLoader</code> loads the metadata and makes it available as
 * an {@link MSystem} instance.
 */
public interface MetadataLoader
{
    /**
     * Loads the metadata and return an {@link MSystem} instance. If 
     * the {@link #invalidate()} method is called prior to calling
     * <code>load()</code>, then the returned metadata will always reflect the
     * most up-to-date information.
     *
     * @return an {@link MSystem} instance. Never returns <code>null</code>.
     * @throws RetsServerException if unable to load the metadata.
     * @see #invalidate()
     */
    public MSystem load() throws RetsServerException;

    /**
     * Invalidates any possible cache of the metadata this loader may have
     * stored. To ensure any changes to the underlying metadata is realized,
     * call <code>invalidate()</code> before calling <code>load()</code>.
     * 
     * @see #load()
     */
    public void invalidate();
    
}
