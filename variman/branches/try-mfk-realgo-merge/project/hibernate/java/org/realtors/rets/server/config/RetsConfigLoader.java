/*
 * Variman RETS Server
 *
 * Author: Danny Hurlburt
 * Copyright (c) 2007, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.config;

import org.realtors.rets.server.RetsServerException;

/**
 * A <code>RetsConfigLoader</code> loads the server's configuration data and
 * makes it available as a {@link RetsConfig} instance.
 */
public interface RetsConfigLoader
{
    /**
     * Loads the server's configuration and return a {@link RetsConfig}
     * instance. If the {@link #invalidate()} method is called prior to calling
     * <code>load()</code>, then the returned configuration data will always
     * reflect the most up-to-date information.
     *
     * @return an {@link RetsConfig} instance. Never returns <code>null</code>.
     * @throws RetsServerException if unable to load the configuration data.
     * @see #invalidate()
     */
    public RetsConfig load() throws RetsServerException;
    
    /**
     * Invalidates any possible cache of the server's configuration data this
     * loader may have stored. To ensure any changes to the underlying
     * configuration data is realized, call <code>invalidate()</code> before
     * calling <code>load()</code>.
     * <p>
     * Implementations must be idempotent.</p>
     * 
     * @see #load()
     */
    public void invalidate();
}
